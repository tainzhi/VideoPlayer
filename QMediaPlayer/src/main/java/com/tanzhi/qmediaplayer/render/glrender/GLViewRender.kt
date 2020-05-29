package com.tanzhi.qmediaplayer.render.glrender

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/29 11:09
 * @description:
 **/

class GLViewRender : GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    // 是否需要高清截图
    var highShot = false

    private lateinit var glSurfaceView: GLSurfaceView

    private val mvpMatrix = FloatArray(16)
    private val stMatrix = FloatArray(16)
    var currentViewWidth = 0
    var currentViewHeight = 0
    var currentVideoWidth = 0
    var currentVideoHeight = 0

    override fun onDrawFrame(gl: GL10?) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        TODO("Not yet implemented")
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        TODO("Not yet implemented")
    }
}