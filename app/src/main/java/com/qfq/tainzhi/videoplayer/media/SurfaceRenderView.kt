package com.qfq.tainzhi.videoplayer.media

import android.annotation.TargetApi
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.qfq.tainzhi.videoplayer.my_media.MeasureHelper
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
class SurfaceRenderView : SurfaceView, IRenderView {
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
        getHolder().addCallback(mSurfaceCallback)
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_NORMAL)
    }
    
    public override fun getView(): View? {
        return this
    }
    
    public override fun shouldWaitForResize(): Boolean {
        return true
    }
    
    //--------------------
    // Layout & Measure
    //--------------------
    public override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight)
            getHolder().setFixedSize(videoWidth, videoHeight)
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
        Log.e("", "SurfaceView doesn't support rotation (" + degree + ")!\n")
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
    // SurfaceViewHolder
    //--------------------
    private class InternalSurfaceHolder constructor(
            surfaceView: SurfaceRenderView,
            surfaceHolder: SurfaceHolder?) : IRenderView.ISurfaceHolder {
        private val mSurfaceView: SurfaceRenderView?
        private val mSurfaceHolder: SurfaceHolder?
        public override fun bindToMediaPlayer(mp: IMediaPlayer?) {
            if (mp != null) {
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) &&
                        (mp is ISurfaceTextureHolder)) {
                    val textureHolder: ISurfaceTextureHolder? = mp as ISurfaceTextureHolder?
                    textureHolder.setSurfaceTexture(null)
                }
                mp.setDisplay(mSurfaceHolder)
            }
        }
        
        public override fun getRenderView(): IRenderView {
            return mSurfaceView
        }
        
        public override fun getSurfaceHolder(): SurfaceHolder? {
            return mSurfaceHolder
        }
        
        public override fun getSurfaceTexture(): SurfaceTexture? {
            return null
        }
        
        public override fun openSurface(): Surface? {
            if (mSurfaceHolder == null) return null
            return mSurfaceHolder.getSurface()
        }
        
        init {
            mSurfaceView = surfaceView
            mSurfaceHolder = surfaceHolder
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
    
    private class SurfaceCallback constructor(surfaceView: SurfaceRenderView) : SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private var mIsFormatChanged: Boolean = false
        private var mFormat: Int = 0
        private var mWidth: Int = 0
        private var mHeight: Int = 0
        private val mWeakSurfaceView: WeakReference<SurfaceRenderView?>?
        private val mRenderCallbackMap: MutableMap<IRenderView.IRenderCallback?, Any?>? = ConcurrentHashMap()
        fun addRenderCallback(callback: IRenderView.IRenderCallback) {
            mRenderCallbackMap.put(callback, callback)
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (mSurfaceHolder != null) {
                if (surfaceHolder == null) surfaceHolder = InternalSurfaceHolder(mWeakSurfaceView.get(), mSurfaceHolder)
                callback.onSurfaceCreated(surfaceHolder, mWidth, mHeight)
            }
            if (mIsFormatChanged) {
                if (surfaceHolder == null) surfaceHolder = InternalSurfaceHolder(mWeakSurfaceView.get(), mSurfaceHolder)
                callback.onSurfaceChanged(surfaceHolder, mFormat, mWidth, mHeight)
            }
        }
        
        fun removeRenderCallback(callback: IRenderView.IRenderCallback) {
            mRenderCallbackMap.remove(callback)
        }
        
        public override fun surfaceCreated(holder: SurfaceHolder?) {
            mSurfaceHolder = holder
            mIsFormatChanged = false
            mFormat = 0
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakSurfaceView.get(), mSurfaceHolder)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }
        
        public override fun surfaceDestroyed(holder: SurfaceHolder?) {
            mSurfaceHolder = null
            mIsFormatChanged = false
            mFormat = 0
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakSurfaceView.get(), mSurfaceHolder)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceDestroyed(surfaceHolder)
            }
        }
        
        public override fun surfaceChanged(
                holder: SurfaceHolder?, format: Int,
                width: Int, height: Int) {
            mSurfaceHolder = holder
            mIsFormatChanged = true
            mFormat = format
            mWidth = width
            mHeight = height
            
            // mMeasureHelper.setVideoSize(width, height);
            val surfaceHolder: IRenderView.ISurfaceHolder? = InternalSurfaceHolder(mWeakSurfaceView.get(), mSurfaceHolder)
            for (renderCallback: IRenderView.IRenderCallback? in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceChanged(surfaceHolder, format, width, height)
            }
        }
        
        init {
            mWeakSurfaceView = WeakReference(surfaceView)
        }
    }
    
    //--------------------
    // Accessibility
    //--------------------
    public override fun onInitializeAccessibilityEvent(event: AccessibilityEvent?) {
        super.onInitializeAccessibilityEvent(event)
        event.setClassName(SurfaceRenderView::class.java.getName())
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {
        super.onInitializeAccessibilityNodeInfo(info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            info.setClassName(SurfaceRenderView::class.java.getName())
        }
    }
}