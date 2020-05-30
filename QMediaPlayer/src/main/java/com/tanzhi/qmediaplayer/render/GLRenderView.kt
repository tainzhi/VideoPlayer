package com.tanzhi.qmediaplayer.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.Surface
import android.view.SurfaceHolder
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.tanzhi.qmediaplayer.IMediaInterface
import com.tanzhi.qmediaplayer.render.glrender.GLViewBaseRender
import com.tanzhi.qmediaplayer.render.glrender.NoEffect
import com.tanzhi.qmediaplayer.render.glrender.ShaderInterface
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/29 16:32
 * @description: GLSurfaceView渲染
 **/

class GLRenderView : GLSurfaceView, IRenderView, GLRenderViewListener {

    private val measureHelper by lazy { MeasureHelper(this) }
    private val surfaceCallback by lazy { SurfaceCallback(this) }
    private lateinit var mRender : GLViewBaseRender

    constructor(context: Context, renderer: GLViewBaseRender) : super(context) {
        mRender =renderer
        setRenderer(renderer)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override var effect: ShaderInterface = NoEffect()
        set(value) {
            field = value
            mRender.effect = value
        }

    override fun shouldWaitForResize(): Boolean {
        return false
    }

    // ------------------------
    // layout & measure
    // ------------------------
    override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            measureHelper.setVideoSize(videoWidth, videoHeight)
            requestLayout()
        }
    }

    override fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            measureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
            requestLayout()
        }
    }

    override fun setVideoRotation(degree: Int) {
        measureHelper.setVideoRotation(degree)
        rotation = degree.toFloat()
    }

    override fun setAspectRatio(aspectRatio: Int) {
        measureHelper.setVideoRotation(aspectRatio)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureHelper.measuredWidth, measureHelper.measuredHeight)
    }

    override fun addRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback.addRenderCallback(callback)
    }

    override fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback.removerenderCallback(callback)
    }

    inner class InternalSurfaceHolder(glRenderView: GLRenderView, surface: Surface) : IRenderView.ISurfaceHolder {
        override fun bindToMediaPlayer(mp: IMediaInterface) {
            TODO("Not yet implemented")
        }

        override val renderView: IRenderView
            get() = TODO("Not yet implemented")
        override val surfaceHolder: SurfaceHolder?
            get() = TODO("Not yet implemented")

        override fun openSurface(): Surface? {
            TODO("Not yet implemented")
        }

        override val surfaceTexture: SurfaceTexture?
            get() = TODO("Not yet implemented")

    }

    inner class SurfaceCallback(renderView: GLRenderView) {

        private var isFormatChanged = false
        private var width = 0
        private var height = 0
        private val weakRenderView: WeakReference<GLRenderView> = WeakReference(renderView)
        private val renderCallbackMap: MutableMap<IRenderView.IRenderCallback, Any> = ConcurrentHashMap()

        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            renderCallbackMap[callback] = callback
            // var surfaceHolder: IRenderView.ISurfaceHolder? = null
            // if (surfaceTexture != null) {
            //     if (surfaceHolder == null) {
            //         surfaceHolder = InternalSurfaceHolder(weakRenderView.get(), surfaceTexture)
            //     }
            //     callback.onSurfaceCreated(surfaceHolder, width, height)
            // }
            // if (isFormatChanged) {
            //     if (surfaceHolder == null) {
            //         surfaceHolder = InternalSurfaceHolder(weakRenderView.get(), surfaceTexture)
            //     }
            //     callback.onSurfaceChanged(surfaceHolder, 0, width, height)
            // }
        }

        fun postSurface(surface: Surface) {
            for (callback in renderCallbackMap.keys) {
                val surfaceHolder = InternalSurfaceHolder(this@GLRenderView, surface)
                callback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }

        fun removerenderCallback(callback: IRenderView.IRenderCallback?) {
            renderCallbackMap.remove(callback)
        }

    }

    override fun onSurfaceAvailable(surface: Surface) {
        surfaceCallback.postSurface(surface)
    }

    //--------------------
    // Accessibility
    //--------------------
    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = GLRenderView::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = GLRenderView::class.java.name
    }

}

interface GLRenderViewListener {
    fun onSurfaceAvailable(surface: Surface)
}