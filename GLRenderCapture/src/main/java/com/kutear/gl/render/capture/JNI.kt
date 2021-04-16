package com.kutear.gl.render.capture

class JNI {
    companion object {
        external fun attach()

        external fun start()

        external fun stop()
    }
}