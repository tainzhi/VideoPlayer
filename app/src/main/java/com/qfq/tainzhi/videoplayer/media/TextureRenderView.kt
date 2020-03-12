package com.qfq.tainzhi.videoplayer.media

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
import com.qfq.tainzhi.videoplayer.my_media.MeasureHelper
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import tv.danmaku.ijk.media.player.ISurfaceTextureHost
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class TextureRenderView : TextureView, IRenderView {
    private var mMeasureHelper: MeasureHelper? = null
    
    constructor(context: Context?) : super(context) {
        initView(context)
    }
    
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }
    
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }
    
    private fun initView(context: Context?) {
        mMeasureHelper = MeasureHelper(this)
        mSurfaceCallback = SurfaceCallback(this)
        setSurfaceTextureListener(mSurfaceCallback)
    }
    
    public override fun getView(): View? {
        return this
    }
    
    public override fun shouldWaitForResize(): Boolean {
        return false
    }
    
    override fun onDetachedFromWindow() {
        mSurfaceCallback.willDetachFromWindow()
        super.onDetachedFromWindow()
        mSurfaceCallback.didDetachFromWindow()
    }
    
    //--------------------
    // Layout & Measure
    //--------------------
    public override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight)
            requestLayout()
        }
    }
    
    public override fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
            requestLayout()
        }
    }
    
    public override fun setVideoRotation(degree: Int) {
        mMeasureHelper.setVideoRotation(degree)
        setRotation(degree.toFloat())
    }
    
    public override fun setAspectRatio(aspectRatio: Int) {
        mMeasureHelper.setAspectRatio(aspectRatio)
        requestLayout()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mMeasureHelper.measuredWidth, mMeasureHelper.measuredHeight)
    }
    
    //--------------------
    // TextureViewHolder
    //--------------------
    fun getSurfaceHolder(): IRenderView.ISurfaceHolder? {
        return InternalSurfaceHolder(this, mSurfaceCallback.mSurfaceTexture, mSurfaceCallback)
    }
    
    private class InternalSurfaceHolder constructor(
            textureView: TextureRenderView,
            surfaceTexture: SurfaceTexture?,
            surfaceTextureHost: ISurfaceTextureHost) : IRenderView.ISurfaceHolder {
        private val mTextureView: TextureRenderView?
        private val mSurfaceTexture: SurfaceTexture?
        private val mSurfaceTextureHost: ISurfaceTextureHost?
        
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public override fun bindToMediaPlayer(mp: IMediaPlayer?) {
            if (mp == null) return
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
                    (mp is ISurfaceTextureHolder)) {
                val textureHolder: ISurfaceTextureHolder? = mp as ISurfaceTextureHolder?
                mTextureView.mSurfaceCallback.setOwnSurfaceTexture(false)
                val surfaceTexture: SurfaceTexture? = textureHolder.getSurfaceTexture()
                if (surfaceTexture != null) {
                    mTextureView.setSurfaceTexture(surfaceTexture)
                } else {
                    textureHolder.setSurfaceTexture(mSurfaceTexture)
                    textureHolder.setSurfaceTextureHost(mTextureView.mSurfaceCallback)
                }
            } else {
                mp.setSurface(openSurface())
            }
        }
        
        public override fun getRenderView(): IRenderView {
            return mTextureView
        }
        
        public override fun getSurfaceHolder(): SurfaceHolder? {
            return null
        }
        
        public override fun getSurfaceTexture(): SurfaceTexture? {
            return mSurfaceTexture
        }
        
        public override fun openSurface(): Surface? {
            if (mSurfaceTexture == null) return null
            return Surface(mSurfaceTexture)
        }
        
        init {
            mTextureView = textureView
            mSurfaceTexture = surfaceTexture
            mSurfaceTextureHost = surfaceTextureHost
        }
    }
    
    //-------------------------
    // SurfaceHolder.Callback
    //-------------------------
    public override fun addRenderCallback(callback: IRenderView.IRenderCallback?) {
        mSurfaceCallback.addRenderCallback(callback)
    }
    
    public override fun removeRenderCallback(callback: IRenderView.IRenderCallback?) {
        mSurfaceCallback.removeRenderCallback(callback)
    }
    
    private var mSurfaceCallback: SurfaceCallback? = null
    
    private class SurfaceCallback constructor(renderView: TextureRenderView) : SurfaceTextureListener, ISurfaceTextureHost {
        private var mSurfaceTexture: SurfaceTexture? = null
        private var mIsFormatChanged: Boolean = false
        private var mWidth: Int = 0
        private var mHeight: Int = 0
        private var mOwnSurfaceTexture: Boolean = true
        private var mWillDetachFromWindow: Boolean = false
        private var mDidDetachFromWindow: Boolean = false
        private val mWeakRenderView: WeakReference<TextureRenderView?>?
        private val mRenderCallbackMap: MutableMap<IRenderView.IRenderCallback?, Any?>? = ConcurrentHashMap()
        fun setOwnSurfaceTexture(ownSurfaceTexture: Boolean) {
            mOwnSurfaceTexture = ownSurfaceTexture
        }
        
        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            mRenderCallbackMap.put(callback, callback)
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (mSurfaceTexture != null) {
                if (surfaceHolder == null) surfaceHolder = InternalSurfaceHolder(mWeakRenderView.get(), mSurfaceTexture, this)
                callback.onSurfaceCreated(surfaceHolder, mWidth, mHeight)
            }
            if (mIsFormatChanged) {
                if (surfaceHolder == null) surfaceHolder = InternalSurfaceHolder(mWeakRenderView.get(), mSurfaceTexture, this)
                callback.onSurfaceChanged(surfaceHolder, 0, mWidth, mHeight)
            }
        }
        
        fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
            mRenderCallbackMap.remove(callback)
        }
        
        public override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            mSurfaceTexture = surface
            mIsFormatChanged = false
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakRenderView.get(), surface, this)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }
        
        public override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            mSurfaceTexture = surface
            mIsFormatChanged = true
            mWidth = width
            mHeight = height
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakRenderView.get(), surface, this)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceChanged(surfaceHolder, 0, width, height)
            }
        }
        
        public override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            mSurfaceTexture = surface
            mIsFormatChanged = false
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakRenderView.get(), surface, this)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceDestroyed(surfaceHolder)
            }
            Log.d(TAG, "onSurfaceTextureDestroyed: destroy: " + mOwnSurfaceTexture)
            return mOwnSurfaceTexture
        }
        
        public override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {}
        
        //-------------------------
        // ISurfaceTextureHost
        //-------------------------
        public override fun releaseSurfaceTexture(surfaceTexture: SurfaceTexture?) {
            if (surfaceTexture == null) {
                Log.d(TAG, "releaseSurfaceTexture: null")
            } else if (mDidDetachFromWindow) {
                if (surfaceTexture !== mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): release detached SurfaceTexture")
                    surfaceTexture.release()
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: didDetachFromWindow(): already released by TextureView")
                }
            } else if (mWillDetachFromWindow) {
                if (surfaceTexture !== mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): re-attach SurfaceTexture to TextureView")
                    setOwnSurfaceTexture(true)
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: willDetachFromWindow(): will released by TextureView")
                }
            } else {
                if (surfaceTexture !== mSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: release different SurfaceTexture")
                    surfaceTexture.release()
                } else if (!mOwnSurfaceTexture) {
                    Log.d(TAG, "releaseSurfaceTexture: alive: re-attach SurfaceTexture to TextureView")
                    setOwnSurfaceTexture(true)
                } else {
                    Log.d(TAG, "releaseSurfaceTexture: alive: will released by TextureView")
                }
            }
        }
        
        fun willDetachFromWindow() {
            Log.d(TAG, "willDetachFromWindow()")
            mWillDetachFromWindow = true
        }
        
        fun didDetachFromWindow() {
            Log.d(TAG, "didDetachFromWindow()")
            mDidDetachFromWindow = true
        }
        
        init {
            mWeakRenderView = WeakReference(renderView)
        }
    }
    
    //--------------------
    // Accessibility
    //--------------------
    public override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        super.onInitializeAccessibilityEvent(event)
        event.setClassName(TextureRenderView::class.java.getName())
    }
    
    public override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.setClassName(TextureRenderView::class.java.getName())
    }
    
    companion object {
        private val TAG: String? = "TextureRenderView"
    }
}