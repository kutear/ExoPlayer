package com.google.android.exoplayer2.gldemo

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.util.Log
import com.kutear.gl.render.capture.grafika.gles.GlUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class FBORender : VideoProcessingGLSurfaceView.VideoProcessor {
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

    init {
        mVertices = ByteBuffer.allocateDirect(mVerticesData.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mVertices.put(mVerticesData).position(0)
        mTexCoords = ByteBuffer.allocateDirect(mTexCoordsData.size * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        mTexCoords.put(mTexCoordsData).position(0)
    }

    override fun initialize() {
        compileAndLinkProgram()
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
        val fShaderStr = """
precision mediump float;					  
uniform sampler2D u_Texture; 
varying vec2 v_texCoords; 
void main()                                  
{                                            
  gl_FragColor = texture2D(u_Texture, v_texCoords) ;
}                                            
"""
        val linked = IntArray(1)

        // Create the program object
        val programObject = GlUtil.createProgram(vShaderStr, fShaderStr)

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

    override fun setSurfaceSize(width: Int, height: Int) {
        createOffscreenTexture(width, height)
    }

    private var frameBufferTexture = -1
    private var frameBuffer = -1

    fun getFrameBuffer() = frameBuffer
    fun getFrameTexture() = frameBufferTexture

    private fun createOffscreenTexture(width: Int, height: Int) {
        val textures = IntArray(1)
        GLES20.glGenTextures(textures.size, textures, 0)
        frameBufferTexture = textures[0]

        val frameBuffers = IntArray(1)
        GLES20.glGenFramebuffers(frameBuffers.size, frameBuffers, 0)
        frameBuffer = frameBuffers[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTexture)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer)
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameBufferTexture, 0)
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
    }

    override fun draw(frameTexture: Int, frameTimestampUs: Long) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(mProgramObject)
        GlUtil.checkGlError("glUseProgram")
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, mVertices)
        GLES20.glEnableVertexAttribArray(0)
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, mTexCoords)
        GLES20.glEnableVertexAttribArray(1)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameTexture)
        val loc = GLES20.glGetUniformLocation(mProgramObject, "u_Texture")
        GLES20.glUniform1i(loc, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GlUtil.checkGlError("glDrawArrays")
    }
}