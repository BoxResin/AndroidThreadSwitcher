[![Download](https://api.bintray.com/packages/boxresin/maven/AndroidThreadSwitcher/images/download.svg) ](https://bintray.com/boxresin/maven/AndroidThreadSwitcher/_latestVersion)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/BoxResin/JavaHTTP/master/LICENSE)

![AndroidThreadSwitcher Logo](https://user-images.githubusercontent.com/13031505/28096488-190a365e-66e4-11e7-974c-06262603bf46.png)

## About
`AndroidThreadSwitcher` helps you to switch between UI thread and Worker thread easily. It would free you from a lot of boilerplate codes and the [Callback Hell](http://callbackhell.com/).

## Getting Started

Add the following to `app`'s `build.gradle` file:

```gradle
dependencies { 
    implementation 'boxresin.library:AndroidThreadSwitcher:1.2.0'
}
```

## Usage in Kotlin
```kotlin
ThreadSwitcher.newChain() // Create an empty chain.
        // Concat a UI chain to the empty chain.
        .onUI {
            // Some tasks to do on a UI Thread
            "result" // Pass any value to the next chain.
        }
        // Concat a Worker chain to the previous chain.
        .onWorker { result: String -> // Receive the value from the previous chain.
            // Some tasks to do on Worker Thread
        }
        .onUI {
            // Some tasks to do on a UI Thread
        }
        .onWorker {
            // Some tasks to do on a Worker Thread
            "data"
        }

        // Start to perform all chains from the top to bottom.
        .start(
        // onSuccess callback would be invoked when all chains are finished without any exception.
        onSuccess = { result: String -> // The type of parameter is settled by the return value of last chain.
        },

        // onError callback would be invoked when an exception occurred during performancing chains.
        onError = { e: Throwable -> // The exception
        })
```

## Usage in Java 8+
```java
ThreadSwitcher.newChain() // Create an empty chain.
        // Concat a UI chain to the empty chain.
        .onUI(v -> // v is Void type.
        {
            // Some tasks to do on a UI Thread
            return "result"; // Pass any value to the next chain.
        })
        // Concat a Worker chain to the previous chain.
        .onWorker((String result) -> // Receive the value from the previous chain.
        {
            // Some tasks to do on Worker Thread
            return Unit.INSTANCE; // Unit type of Kotlin
        })
        .onUI(unit -> // Unit type of Kotlin
        {
            // Some tasks to do on a UI Thread
            return Unit.INSTANCE;
        })
        .onWorker(unit ->
        {
            // Some tasks to do on a Worker Thread
            return "data";
        })

        // Start to perform all chains from the top to bottom.
        .start(
            // This function would be invoked when all chains are finished without any exception.
            (String result) -> { // The type of parameter is settled by the return value of last chain.
                return Unit.INSTANCE;
            },
            // This function would be invoked when an exception occurred during performancing chains.
            (Throwable e) -> {
                return Unit.INSTANCE;
            }
        );
```


## License
[link](/LICENSE)
