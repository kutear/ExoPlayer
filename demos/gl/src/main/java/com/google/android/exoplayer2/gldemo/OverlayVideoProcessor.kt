package com.google.android.exoplayer2.gldemo

import android.content.Context
import android.opengl.GLES11Ext.GL_TEXTURE_EXTERNAL_OES
import android.opengl.GLES20
import android.util.Log
import com.google.android.exoplayer2.gldemo.VideoProcessingGLSurfaceView.VideoProcessor
import com.kutear.gl.render.capture.grafika.gles.GlUtil
import com.kutear.gl.render.capture.grafika.gles.GlUtil.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


// mock this as a game engine
class OverlayVideoProcessor(val context: Context) : VideoProcessor {
    private companion object {
        private const val TAG = "OverlayVideoProcessor"
    }

    private val mVertices: FloatBuffer
    private val mTexCoords: FloatBuffer
    private val mVerticesData = floatArrayOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            -0.5f, 0.5f, 0f,
            0.5f, 0.5f, 0f
    )
    private val mTexCoordsData = floatArrayOf(
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f
    )
    private var mProgramObject = 0
    private val fboRender = FBORender()

    init {
        mVertices = ByteBuffer.allocateDirect(mVerticesData.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertices.put(mVerticesData).position(0)
        mTexCoords = ByteBuffer.allocateDirect(mTexCoordsData.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTexCoords.put(mTexCoordsData).position(0)
    }



    private fun compileAndLinkProgram() {
        val vShaderStr = """attribute vec4 a_position;    
attribute vec2 a_texCoords; 
varying vec2 v_texCoords; 
void main()                  
{                            
   gl_Position = a_position;  
    v_texCoords = a_texCoords; 
}                            
"""
        val fShaderStr = """#extension GL_OES_EGL_image_external : require
precision mediump float;					  
uniform samplerExternalOES u_Texture; 
varying vec2 v_texCoords; 
void main()                                  
{                                            
  gl_FragColor = texture2D(u_Texture, v_texCoords) ;
}                                            
"""
        val linked = IntArray(1)

        // Create the program object
        val programObject = createProgram(vShaderStr, fShaderStr)

        // Bind vPosition to attribute 0
        GLES20.glBindAttribLocation(programObject, 0, "a_position")
        GLES20.glBindAttribLocation(programObject, 1, "a_texCoords")

        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0)
        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:")
            Log.e(TAG, GLES20.glGetProgramInfoLog(programObject))
            GLES20.glDeleteProgram(programObject)
            return
        }
        mProgramObject = programObject
    }

    override fun initialize() {
        Log.d(TAG, "initialize")
        compileAndLinkProgram()
        fboRender.initialize()
    }

    override fun setSurfaceSize(width: Int, height: Int) {
        Log.d(TAG, "setSurfaceSize")
        fboRender.setSurfaceSize(width, height)
    }

    override fun draw(frameTexture: Int, frameTimestampUs: Long) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboRender.getFrameBuffer())

        draw(frameTexture) // mock game engine draw call

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        fboRender.draw(fboRender.getFrameTexture(), frameTimestampUs)

//        sendToOther(frameBufferTexture)
    }

    // such as mediaCodec
    private fun sendToOther(texture: Int) {
        // TODO
    }

    private fun draw(texture: Int) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(mProgramObject)
        GlUtil.checkGlError("glUseProgram")
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices)
        GLES20.glEnableVertexAttribArray(0)
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, mTexCoords)
        GLES20.glEnableVertexAttribArray(1)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture)
        val loc = GLES20.glGetUniformLocation(mProgramObject, "u_Texture")
        GLES20.glUniform1i(loc, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GlUtil.checkGlError("glDrawArrays")
    }
}