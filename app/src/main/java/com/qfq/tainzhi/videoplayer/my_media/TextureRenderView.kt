package com.qfq.tainzhi.videoplayer.my_media

import android.annotation.TargetApi
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.orhanobut.logger.Logger
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import tv.danmaku.ijk.media.player.ISurfaceTextureHost
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 14:14
 * @description:
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class TextureRenderView : TextureView, IRenderView {
    private var measureHelper: MeasureHelper? = null
    private var surfaceCallback: SurfaceCallback? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    private fun initView(context: Context) {
        measureHelper = MeasureHelper(this)
        surfaceCallback = SurfaceCallback(this)
        surfaceTextureListener = surfaceCallback
    }

    override val view: View
        get() = this

    override fun shouldWaitForResize(): Boolean {
        return false
    }

    //------------------------
// layout & measure
//------------------------
    override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            measureHelper!!.setVideoSize(videoWidth, videoHeight)
            requestLayout()
        }
    }

    override fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            measureHelper!!.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
            requestLayout()
        }
    }

    override fun setVideoRotation(degree: Int) {
        measureHelper!!.setVideoRotation(degree)
        rotation = degree.toFloat()
    }

    override fun setAspectRatio(aspectRatio: Int) {
        measureHelper!!.setVideoRotation(aspectRatio)
        requestLayout()
    }

    override fun addRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback!!.addRenderCallback(callback)
    }

    override fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
        surfaceCallback!!.removeReanderCallback(callback)
    }

    //-----------------
// TextureViewHolder
//-----------------
    val surfaceHolder: IRenderView.ISurfaceHolder
        get() = InternalSurfaceHolder(this, surfaceCallback!!.surfaceTexture, surfaceCallback)

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = TextureRenderView::class.java.name
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = TextureRenderView::class.java.name
    }

    override fun onDetachedFromWindow() {
        surfaceCallback!!.willDetachFromWindow()
        super.onDetachedFromWindow()
        surfaceCallback!!.didDetachFromWindow()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Logger.d(String.format("getWidth=%d getHeight=%d", width, height))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureHelper!!.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureHelper!!.measuredWidth, measureHelper!!.measuredHeight)
        // super.onMeasure(480, 720);
    }

    //--------------------
// Accessibility
//--------------------
    private class InternalSurfaceHolder(private val textureRenderView: TextureRenderView,
                                        override val surfaceTexture: SurfaceTexture?,
                                        private val iSurfaceTextureHost: ISurfaceTextureHost?) : IRenderView.ISurfaceHolder {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        // override fun bindToMediaPlayer(mp: MediaPlayer) {
        //     if (mp == null) return
        //     mp.setSurface(openSurface())
        // }
        //
        // override fun bindToMediaPlayer(mp: IMediaPlayer) {
        //     if (mp == null) return
        //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
        //             mp is ISurfaceTextureHolder) {
        //         val textureHolder = mp as ISurfaceTextureHolder
        //         textureRenderView.surfaceCallback!!.setOwnSurfaceTexture(false)
        //         val surfaceTexture = textureHolder.surfaceTexture
        //         if (surfaceTexture != null) {
        //             textureRenderView.surfaceTexture = surfaceTexture
        //         } else {
        //             textureHolder.surfaceTexture = this.surfaceTexture
        //             textureHolder.setSurfaceTextureHost(textureRenderView.surfaceCallback)
        //         }
        //     } else {
        //         mp.setSurface(openSurface())
        //     }
        //
        // }
        //
        // override fun bindToMediaPlayer(mp: ExoPlayer) {
        //     (mp as SimpleExoPlayer?)!!.setVideoSurface(openSurface())
        // }


        override fun bindToMediaPlayer(mp: IMediaInterface) {
            when(mp) {
                is IMediaSystem -> {
                    mp.setDisplay(openSurface())
                }
                is IMediaIjk -> {
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) && (mp is ISurfaceTextureHolder)) {
                        textureRenderView.surfaceCallback?.setOwnSurfaceTexture(false)
                        val textureHolder = mp as ISurfaceTextureHolder
                        if (textureHolder != null) {
                            textureRenderView.surfaceTexture = surfaceTexture
                        } else {
                            (mp as ISurfaceTextureHolder).run {
                                surfaceTexture = surfaceTexture
                                setSurfaceTextureHost(textureRenderView.surfaceCallback)
                            }
                        }
                    }
                    mp.setDisplay(openSurface())
                }
                is IMediaExo -> {
                    mp.setDisplay(openSurface())
                }
            }
        }


        override val renderView: IRenderView
            get() = textureRenderView

        override val surfaceHolder: SurfaceHolder?
            get() = null

        override fun openSurface(): Surface {
            return surfaceTexture.let { Surface(it) }
        }

    }

    private class SurfaceCallback(reanderView: TextureRenderView) : SurfaceTextureListener, ISurfaceTextureHost {
        var surfaceTexture: SurfaceTexture? = null
        private var isFormatChanged = false
        private var width = 0
        private var height = 0
        private var ownSurfaceTexture = true
        private var willDetachFromWindow = false
        private var didDetachFromWindow = false
        private val weakReandView: WeakReference<TextureRenderView>
        private val renderCallbackMap: MutableMap<IRenderView.IRenderCallback, Any> = ConcurrentHashMap()
        fun setOwnSurfaceTexture(ownSurfaceTexture: Boolean) {
            this.ownSurfaceTexture = ownSurfaceTexture
        }

        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            renderCallbackMap[callback] = callback
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (surfaceTexture != null) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakReandView.get()!!, surfaceTexture
                            , this)
                }
                callback.onSurfaceCreated(surfaceHolder, width, height)
            }
            if (isFormatChanged) {
                if (surfaceHolder == null) {
                    surfaceHolder = InternalSurfaceHolder(weakReandView.get()!!, surfaceTexture
                            , this)
                }
                callback.onSurfaceChanged(surfaceHolder, 0, width, height)
            }
        }

        fun removeReanderCallback(callback: IRenderView.IRenderCallback?) {
            renderCallbackMap.remove(callback)
        }

        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            Logger.d("")
            this.surfaceTexture = surfaceTexture
            isFormatChanged = false
            width = 0
            height = 0
            val surfaceHolder: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakReandView.get()!!,
                    surfaceTexture, this)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, i: Int, i1: Int) {
            surfaceTexture = surface
            isFormatChanged = true
            width = i
            height = i1
            val surfaceHolder: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakReandView.get()!!,
                    surface, this)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceChanged(surfaceHolder, 0, width, height)
            }
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            surfaceTexture = surface
            isFormatChanged = false
            width = 0
            height = 0
            val surfaceHolder: IRenderView.ISurfaceHolder = InternalSurfaceHolder(weakReandView.get()!!, surface
                    , this)
            for (callback in renderCallbackMap.keys) {
                callback.onSurfaceDestroyed(surfaceHolder)
            }
            return ownSurfaceTexture
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
        //-------------------------
// ISurfaceTextureHost
//-------------------------
        override fun releaseSurfaceTexture(surfaceTexture: SurfaceTexture) {
            if (surfaceTexture == null) {
                Log.d(TAG, "releaseSurfaceTexture: null")
            } else if (didDetachFromWindow) {
                if (surfaceTexture !== this.surfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!ownSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture")
                    surfaceTexture.release()
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView")
                }
            } else if (willDetachFromWindow) {
                if (surfaceTexture !== this.surfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!ownSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView")
                    setOwnSurfaceTexture(true)
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView")
                }
            } else {
                if (surfaceTexture !== this.surfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!ownSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView")
                    setOwnSurfaceTexture(true)
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView")
                }
            }
        }

        fun willDetachFromWindow() {
            Log.d(TAG, "willDetachFromWindow()")
            willDetachFromWindow = true
        }

        fun didDetachFromWindow() {
            Log.d(TAG, "didDetachFromWindow()")
            didDetachFromWindow = true
        }

        init {
            weakReandView = WeakReference(reanderView)
        }
    }

    companion object {
        private const val TAG = "TextureRenderView"
    }
}