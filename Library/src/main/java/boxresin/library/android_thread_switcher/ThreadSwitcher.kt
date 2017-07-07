package boxresin.library.android_thread_switcher

import android.os.Handler
import android.support.annotation.UiThread
import kotlin.concurrent.thread

/** A utility class to make it easy to switch between UI and Worker thread. */
object ThreadSwitcher
{
    private lateinit var uiHandler: Handler

    /** You must call it on the UI thread before using ThreadSwitcher. */
    @UiThread
    fun initialize()
    {
        uiHandler = Handler()
    }

    fun newChain() = Chain { Unit }

    class Chain<out Param>(private val priorTask: () -> Param)
    {
        /** Adds a task that will be executed on a Worker thread. */
        fun <Return> onWorker(task: (Param) -> Return): Chain<Return>
                = Chain { task(priorTask()) }

        /** Adds a task that will be executed on the UI thread. */
        fun <Return> onUI(task: (Param) -> Return): Chain<Return>
                = Chain {
                    var tempResult: Return? = null
                    val param = priorTask()
                    uiHandler.post {
                        synchronized(this, {
                            tempResult = task(param)
                            (this as Object).notify()
                        })
                    }
                    synchronized(this, {
                        if (tempResult == null)
                            (this as Object).wait()
                    })
                    tempResult!!
                }

        /** A method to start the task */
        fun start(onSuccess: ((result: Param) -> Unit)? = null,
                  onError: ((exception: Throwable) -> Unit)? = null)
        {
            thread {
                try
                {
                    val result = priorTask()
                    uiHandler.post { onSuccess?.invoke(result) }
                }
                catch(e: Throwable) { uiHandler.post { onError?.invoke(e) } }
            }
        }
    }
}
