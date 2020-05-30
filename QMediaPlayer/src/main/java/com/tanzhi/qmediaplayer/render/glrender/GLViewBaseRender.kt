package com.tanzhi.qmediaplayer.render.glrender

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Handler
import com.tanzhi.qmediaplayer.logD
import com.tanzhi.qmediaplayer.render.GLRenderViewListener

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/29 11:09
 * @description:
 **/

abstract class GLViewBaseRender(val glRenderViewListener: GLRenderViewListener) : GLSurfaceView.Renderer {
    // 是否高清截图
    var highShot = false
    lateinit var surfaceView: GLSurfaceView

    var mvpMatrix = FloatArray(16)
    var stMatrix = FloatArray(16)

    var currentViewWidth = 0
    var currentViewHeight = 0
    var currentVideoWidth = 0
    var currentVideoHeight = 0
    protected var changeProgram = false
    protected var changeProgramSupportError = false
    protected var handler = Handler()

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

    }

}