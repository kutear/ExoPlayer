package com.kutear.gl.render.capture

interface GLCapture {
    companion object {
        private const val LIBRARY = "gl_capture"
        fun create(): GLCapture {
            return GLCaptureImpl()
        }

        init {
            System.loadLibrary(LIBRARY)
        }
    }

    fun startCapture()

    fun stopCapture()

    fun onFrameUpdate(texture: Int)

    fun addGLCaptureListener(listener: GLCaptureListener)
}