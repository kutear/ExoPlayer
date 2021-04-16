package com.kutear.gl.render.capture

class GLCaptureImpl : GLCapture {

    override fun startCapture() {
        JNI.start()
    }

    override fun stopCapture() {
        JNI.stop()
    }

    override fun onFrameUpdate(texture: Int) {

    }

    override fun addGLCaptureListener(listener: GLCaptureListener) {

    }
}