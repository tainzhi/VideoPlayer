package com.qfq.tainzhi.videoplayer.my_media

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 15:29
 * @description:
 */
class SurfaceRenderView : SurfaceView, IRenderView {
    private val TAG = this.javaClass.simpleName
    private val measureHelper: MeasureHelper
    private var surfaceCallback: SurfaceCallback? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        measureHelper = MeasureHelper(this)
        surfaceCallback = SurfaceCallback(this)
        holder.addCallback(surfaceCallback)
//        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL)
    }

    override val view: View
        get() = this

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
        Log.e(TAG, "SurfaceView doesn't support rotation ($degree)!\n")
    }

    override fun setAspectRatio(aspectRatio: Int) {
        measureHelper.setAspectRatio(aspectRatio)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureHelper.measuredWidth, measureHelper.measuredHeight)
    }

    private class InternalSurfaceHolder(private val surfaceRenderView: SurfaceRenderView,
                                        override val surfaceHolder: SurfaceHolder?) : IRenderView.ISurfaceHolder {
        override fun bindToMediaPlayer(mp: MediaPlayer?) {
            mp?.setDisplay(surfaceHolder)
        }

        override fun bindToMediaPlayer(mp: IMediaPlayer?) {
            if (mp != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                        mp is ISurfaceTextureHolder) {
                    val textureHolder = mp as ISurfaceTextureHolder
                    textureHolder.surfaceTexture = null
                }
                mp.setDisplay(surfaceHolder)
            }
        }

        override fun bindToMediaPlayer(mp: ExoPlayer?) {
            (mp as SimpleExoPlayer?)!!.setVideoSurfaceHolder(surfaceHolder)
        }

        override val renderView: IRenderView
            get() = surfaceRenderView

        override val surfaceTexture: SurfaceTexture?
            get() = null

        override fun openSurface(): Surface? {
            return surfaceHolder?.surface
        }

    }

    override fun addRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback!!.addRenderCallback(callback)
    }

    override fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback!!.removeRenderCallback(callback)
    }

    private class SurfaceCallback(surfaceView: SurfaceRenderView) : SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private var isFormatChanged = false
        private var format = 0
        private var width = 0
        private var height = 0
        private val weakSurfaceView: WeakReference<SurfaceRenderView>
        private val renderCallbackMap: MutableMap<IRenderView.IRenderCallback, Any> = ConcurrentHashMap()
        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            renderCallbackMap[callback] = callback
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (mSurfaceHolder != null) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!,
                            mSurfaceHolder)
                }
                callback.onSurfaceCreated(surfaceHolder, width, height)
            }
            if (isFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakSurfaceView.get()!!, mSurfaceHolder)
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
                    mSurfaceHolder)
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
                    mSurfaceHolder)
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
                    mSurfaceHolder)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceDestroyed(surfaceHolder)
            }
        }

        init {
            weakSurfaceView = WeakReference(surfaceView)
        }
    }

    //-------------------------
// Accessibility
//-------------------------
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