package com.tanzhi.qmediaplayer.render.glrender

import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.Matrix
import android.view.Surface
import com.tanzhi.qmediaplayer.logD
import com.tanzhi.qmediaplayer.render.GLRenderViewListener
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/1 10:50
 * @description:
 **/

class GLViewSimpleRender(private val glRenderViewListener: GLRenderViewListener? = null) : GLViewBaseRender(glRenderViewListener) , SurfaceTexture.OnFrameAvailableListener {

    private val triangleVerticesData = floatArrayOf( // X, Y, Z, U, V
            -1.0f, -1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            -1.0f, 0.0f,
            1.0f, 0.0f,
            -1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f)
    protected val vertexShader =
            """
                uniform mat4 uMVPMatrix;
                uniform mat4 uSTMatrix;
                attribute vec4 aPosition;
                attribute vec4 aTextureCoord;
                varying vec2 vTextureCoord;
                void main() {
                  gl_Position = uMVPMatrix * aPosition;
                  vTextureCoord = (uSTMatrix * aTextureCoord).xy;
                }
            """
    var fragmentShader: String = ""
        get() = effect.shader
    var updateSurface = false
    var surfaceTexture: SurfaceTexture? = null
    val triangleVertices: FloatBuffer
    var program = 0
    val textureID = IntArray(2)
    var muMVPMatrixHandle = 0
    var muSTMatrixHandle = 0
    var maPositionHandle = 0
    var maTextureHandle = 0

    companion object {
        private const val FLOAT_SIZE_BYTES = 4
        private const val TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES
        private const val TRIANGLE_VERTICES_DATA_POS_OFFSET = 0
        private const val TRIANGLE_VERTICES_DATA_UV_OFFSET = 3
        protected const val GL_TEXTURE_EXTERNAL_OES = 0x8D65
    }


    init {
        triangleVertices = ByteBuffer
                .allocateDirect(
                        triangleVerticesData.size * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        triangleVertices.put(triangleVerticesData).position(0)
        Matrix.setIdentityM(stMatrix, 0)
        Matrix.setIdentityM(mvpMatrix, 0)
    }

    override fun releaseAll() {
        TODO("Not yet implemented")
    }

    override fun onDrawFrame(gl: GL10) {
        synchronized(this) {
            if (updateSurface) {
                surfaceTexture!!.updateTexImage()
                surfaceTexture!!.getTransformMatrix(stMatrix)
                updateSurface = false
            }
        }
        initDrawFrame()
        bindDrawFrameTexture()
        initPointerAndDraw()
        takeBitmap(gl)
        GLES20.glFinish()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        program = createProgram(vertexShader, fragmentShader)
        if (program == 0) return
        maPositionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        if (maPositionHandle == -1) logD("Could not get attrib location for aPosition")

        maTextureHandle = GLES20.glGetAttribLocation(program, "aTextureCoord")
        if (maTextureHandle == -1) logD("could not get attrib location for aTextureCoord")

        muMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        if (muMVPMatrixHandle == -1) logD("could not get attrib location for uMVPMatrix")

        muSTMatrixHandle = GLES20.glGetUniformLocation(program, "uSTMatrix")
        if (muSTMatrixHandle == -1) logD("could not get attrib location for  uSTMatrix")

        GLES20.glGenTextures(2, textureID, 0)
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureID[0])

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        surfaceTexture = SurfaceTexture(textureID[0]).apply {
            setOnFrameAvailableListener(this@GLViewSimpleRender)
        }
        sendSurfaceForPlayer(Surface(surfaceTexture))

    }

    private fun initDrawFrame() {
        if (changeProgram) {
            program = createProgram(vertexShader, fragmentShader)
            changeProgram = false
        }
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glClear(
                GLES20.GL_DEPTH_BUFFER_BIT or
                        GLES20.GL_COLOR_BUFFER_BIT
        )
        GLES20.glUseProgram(program)
    }

    private fun bindDrawFrameTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textureID[0])
    }

    private fun initPointerAndDraw() {
        triangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET)
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
                false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices)
        GLES20.glEnableVertexAttribArray(maPositionHandle)

        triangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET)
        GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT,
                false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES, triangleVertices)
        GLES20.glEnableVertexAttribArray(maTextureHandle)

        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, stMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    protected fun takeBitmap(glUnused: GL10)
    {
        // TODO: 2020/6/1
    }

    @Synchronized
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        updateSurface = true
    }
}