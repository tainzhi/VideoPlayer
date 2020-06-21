package com.tanzhi.qmediaplayer.render

import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.tanzhi.qmediaplayer.*
import com.tanzhi.qmediaplayer.render.glrender.GLViewRender
import com.tanzhi.qmediaplayer.render.glrender.effect.ShaderInterface
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 15:29
 * @description:
 */
class SurfaceRenderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), IRenderView {
    private val measureHelper: MeasureHelper by lazy{ MeasureHelper(this) }
    private val surfaceCallback: SurfaceCallback by lazy {SurfaceCallback(this) }

    init {
        holder.addCallback(surfaceCallback)
    }

    override var render: GLViewRender? = null
        set(value) {
            logD("SurfaceView cannot set render ")
            field = null
        }

    override var renderEffect: ShaderInterface? = null
        set(value) {
            logD("SurfaceView cannot set render effect")
            field = null
        }

    override fun takeShot(videoShotListener: IRenderView.VideoShotListener, highShot: Boolean) {
        logI("Surfaceview cannot take shot")
    }

    override fun shouldWaitForResize(): Boolean {
        return true
    }

    override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            measureHelper.setVideoSize(videoWidth, videoHeight)
            holder.setFixedSize(videoWidth, videoHeight)
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
        logD("degree=${degree}")
    }

    override fun setAspectRatio(aspectRatio: Int) {
        measureHelper.setAspectRatio(aspectRatio)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureHelper.measuredWidth, measureHelper.measuredHeight)
    }

    inner class InternalSurfaceHolder(private val surfaceRenderView: SurfaceRenderView,
                                        override val surfaceHolder: SurfaceHolder) : IRenderView.ISurfaceHolder {

        override fun bindToMediaPlayer(mp: IMediaInterface) {
            when (mp) {
                is IMediaSystem -> {
                    mp.setDisplay(surfaceHolder)
                }
                is IMediaIjk -> {
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) && (mp is ISurfaceTextureHolder)) {
                        (mp as ISurfaceTextureHolder).surfaceTexture = null
                    }
                    mp.setDisplay(surfaceHolder)
                }
                is IMediaExo -> {
                    mp.setDisplay(surfaceHolder)
                }
            }
        }

        override val renderView: IRenderView
            get() = surfaceRenderView

        override val surfaceTexture: SurfaceTexture?
            get() = null

        override fun openSurface(): Surface? {
            return surfaceHolder.surface
        }

    }

    override fun addRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback.addRenderCallback(callback)
    }

    override fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback.removeRenderCallback(callback)
    }

    inner class SurfaceCallback(surfaceView: SurfaceRenderView) : SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private var isFormatChanged = false
        private var format = 0
        private var width = 0
        private var height = 0
        private val weakSurfaceView: WeakReference<SurfaceRenderView> = WeakReference(surfaceView)
        private val renderCallbackMap: MutableMap<IRenderView.IRenderCallback, Any> = ConcurrentHashMap()
        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            renderCallbackMap[callback] = callback
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (mSurfaceHolder != null) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!,
                            mSurfaceHolder!!)
                }
                callback.onSurfaceCreated(surfaceHolder, width, height)
            }
            if (isFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!, mSurfaceHolder!!)
                }
                callback.onSurfaceChanged(surfaceHolder, format, width, height)
            }
        }

        fun removeRenderCallback(callback: IRenderView.IRenderCallback?) {
            renderCallbackMap.remove(callback)
        }

        override fun surfaceCreated(holder: SurfaceHolder) {
            mSurfaceHolder = holder
            isFormatChanged = false
            format = 0
            width = 0
            height = 0
            val surfaceHolder: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!,
                    mSurfaceHolder!!)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }

        override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
            mSurfaceHolder = surfaceHolder
            isFormatChanged = true
            format = i
            width = i1
            height = i2
            val surfaceHolder1: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!,
                    mSurfaceHolder!!)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceChanged(surfaceHolder1, i, i1, i2)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            mSurfaceHolder = null
            isFormatChanged = false
            format = 0
            width = 0
            height = 0
            val surfaceHolder: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!,
                    mSurfaceHolder!!)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceDestroyed(surfaceHolder)
            }
        }

    }

    // -------------------------
    // Accessibility
    // -------------------------
    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = SurfaceRenderView::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            info.className = SurfaceRenderView::class.java.name
        }
    }
}