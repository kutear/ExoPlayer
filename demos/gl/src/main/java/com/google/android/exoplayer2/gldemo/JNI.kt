package com.google.android.exoplayer2.gldemo

object JNI {
    init {
        System.loadLibrary("native")
    }

    external fun sayHello()

}