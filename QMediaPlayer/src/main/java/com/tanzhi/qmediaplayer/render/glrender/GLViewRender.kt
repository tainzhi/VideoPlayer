package com.tanzhi.qmediaplayer.render.glrender

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/30 13:43
 * @description:
 **/

class GLViewRender : GLViewBaseRender{

    // 是否需要高清截图
    var highShot = false

    private lateinit var glSurfaceView: GLSurfaceView

    private val mvpMatrix = FloatArray(16)
    private val stMatrix = FloatArray(16)
    var currentViewWidth = 0
    var currentViewHeight = 0
    var currentVideoWidth = 0
    var currentVideoHeight = 0

    var effect: ShaderInterface
        set(value) {
            // TODO: 2020/5/30
        }

    companion object {
        private const val FLOAT_SIZE_BYTES = 4
        private const val TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES
        private const val TRIANGLE_VERTICES_DATA_POS_OFFSET = 0
        private const val TRIANGLE_VERTICES_DATA_UV_OFFSET = 3
        protected const val GL_TEXTURE_EXTERNAL_OES = 0x8D65
    }

    init {

    }

    override fun onDrawFrame(gl: GL10?) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glRenderViewListener.onSurfaceAvailable()
    }
}