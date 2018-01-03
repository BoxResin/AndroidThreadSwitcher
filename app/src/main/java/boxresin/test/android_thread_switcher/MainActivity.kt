package boxresin.test.android_thread_switcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import boxresin.library.android_thread_switcher.ThreadSwitcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ThreadSwitcher.newChain()
                .onWorker { Thread.sleep(2000) }
                .onUI {
                    Toast.makeText(this, "Hello.", Toast.LENGTH_SHORT).show()
                    button.text = ":/"
                }
                .onWorker { Thread.sleep(5000) }
                .onUI {
                    Toast.makeText(this, "What's", Toast.LENGTH_SHORT).show()
                    button.text = ":)"
                }
                .onWorker { Thread.sleep(1200) }
                .onUI { Toast.makeText(this, "your", Toast.LENGTH_SHORT).show() }
                .onWorker { Thread.sleep(3000) }
                .onUI { Toast.makeText(this, "name?", Toast.LENGTH_SHORT).show() }
                .start()
    }
}
