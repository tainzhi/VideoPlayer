package com.tanzhi.qmediaplayer.render.glrender

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLException
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.view.Surface
import com.tanzhi.qmediaplayer.logD
import com.tanzhi.qmediaplayer.render.GLRenderViewListener
import com.tanzhi.qmediaplayer.render.glrender.effect.NoEffect
import java.nio.IntBuffer
import javax.microedition.khronos.opengles.GL10

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/29 11:09
 * @description:
 **/

abstract class GLViewBaseRender(private val glRenderViewListener: GLRenderViewListener? = null) : GLSurfaceView.Renderer {
    // 是否高清截图
    var highShot = false
    lateinit var surfaceView: GLSurfaceView

    var effect: ShaderInterface = NoEffect()

    var mvpMatrix = FloatArray(16)
    var stMatrix = FloatArray(16)

    var currentViewWidth = 0
    var currentViewHeight = 0
    var currentVideoWidth = 0
    var currentVideoHeight = 0
    protected var changeProgram = false
    protected var changeProgramSupportError = false
    protected var handler = Handler()

    protected fun sendSurfaceForPlayer(surface: Surface) {
        handler.post {
            glRenderViewListener?.onSurfaceAvailable(surface)
        }
    }

    abstract fun releaseAll()

    protected fun loadShader(shaderType: Int, source: String): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (shader != 0) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)

            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                logD("Could not compile shader $shaderType")
                GLES20.glCompileShader(shader)
                shader = 0
            }

        }
        return shader
    }

    protected fun createProgram(vertexSource: String, fragmentSource: String) : Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) return 0
        val pixelSahder = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (pixelSahder == 0) return 0

        var program = GLES20.glCreateProgram()
        if (program == 0) {
            GLES20.glAttachShader(program, vertexShader)
            GLES20.glAttachShader(program, pixelSahder)
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    protected fun createBitmapFromGLSurface(x: Int, y: Int, w: Int, h: Int, gl: GL10): Bitmap? {
        val bitmapBuffer = IntArray(w * h)
        val bitmapSource = IntArray(w * h)
        val intBuffer = IntBuffer.wrap(bitmapBuffer)
        intBuffer.position(0)
        try {
            gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                    intBuffer)
            var offset1: Int
            var offset2: Int
            for (i in 0 until h) {
                offset1 = i * w
                offset2 = (h - i - 1) * w
                for (j in 0 until w) {
                    val texturePixel = bitmapBuffer[offset1 + j]
                    val blue = texturePixel shr 16 and 0xff
                    val red = texturePixel shl 16 and 0x00ff0000
                    val pixel = texturePixel and -0xff0100 or red or blue
                    bitmapSource[offset2 + j] = pixel
                }
            }
        } catch (e: GLException) {
            return null
        }
        return if (highShot) {
            Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.RGB_565)
        }
    }


    fun initRenderSize() {
        if (currentViewWidth != 0 && currentViewHeight != 0) {
            Matrix.scaleM(mvpMatrix, 0, currentViewWidth.toFloat() / surfaceView.width,
                    currentViewHeight.toFloat() / surfaceView.height, 1f)
        }
    }
}