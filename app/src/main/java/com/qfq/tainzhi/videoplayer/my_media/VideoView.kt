package com.qfq.tainzhi.videoplayer.my_media

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 * Description: 基本 VideoView, 没有各种操作控件 Controller
 */

class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val TAG = "VideoView"
    }
    private var mediaPlayerClass: Class<*>? = null
    private var iMediaPlayer: IMediaInterface? = null

    var mediaPlayerType = Constant.PlayerType.SYSTEM_PLAYER

    var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    private var mRenderView: IRenderView? = null
    var enableSurfaceView = true
    var enableTextureView = false

    var videoUri: Uri? = null
        set(value) {
            field = value
            openVideo()
        }

    var videoWidth = 0
    var videoHeight = 0

    var videoSarNum = 0
    var videoSarDen = 0

    var aspectRatio = Constant.AspectRatio.AR_ASPECT_FILL_PARENT

    var videoRotationDegree = 0

    private val renderType = Constant.RenderType.SURFACE_VIEW

    private fun openVideo() {
        initRender()
        setMediaPlayer()
    }

    /**
     * 初始化渲染器
     */
    private fun initRender() {
        when(renderType) {
            Constant.RenderType.SURFACE_VIEW -> {
                setRenderView(SurfaceRenderView(context))
            }
            Constant.RenderType.TEXTURE_VIEW -> {
                val renderView = TextureRenderView(context)
                if (iMediaPlayer != null) {
                    // renderView.surfaceHolder.bindToMediaPlayer(mMediaPlayer);
                    // renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                    // renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
                    // renderView.setAspectRatio(mCurrentAspectRatio);
                }
                setRenderView(renderView)
            }
        }
    }

    private fun setRenderView(renderView: IRenderView) {
        if (mRenderView != null) {
            // IMediaPlayer.setDisplay(null)
            // iMediaPlayer.setDisplay(null)

            val renderUIView = mRenderView?.view
            mRenderView?.removeRenderCallback(mSHCallback)
            mRenderView = null
            removeView(renderUIView)
        }
        if (renderView == null) return
        mRenderView = renderView
        renderView.setAspectRatio(aspectRatio)
        if (videoWidth > 0 && videoHeight > 0)
            renderView.setVideoSize(videoWidth, videoHeight)
        if (videoSarNum > 0 && videoSarDen > 0)
            renderView.setVideoSampleAspectRatio(videoSarNum, videoSarDen)

        val renderUIView = mRenderView?.view
        renderUIView?.layoutParams = LayoutParams(
               LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER)
        addView(renderUIView)

        mRenderView?.addRenderCallback(mSHCallback)
        mRenderView?.setVideoRotation(videoRotationDegree)
    }

    /**
     * 初始化MediaPlayer
     */
    private fun setMediaPlayer() {
        iMediaPlayer = when (mediaPlayerType) {
            Constant.PlayerType.SYSTEM_PLAYER -> {
                // mediaPlayerClass = IMediaSystem::class.java
                IMediaSystem(this)
            }
            Constant.PlayerType.IJK_PLAYER -> {
                // mediaPlayerClass = IMediaIjk::class.java
                IMediaSystem(this)
            }
            Constant.PlayerType.EXO_PLAYER -> {
                // mediaPlayerClass = IMediaExo::class.java
                IMediaSystem(this)
            }
            else -> null
        }
    }

    fun onPrepared() {
        Log.i(TAG, "onPrepared ")
        iMediaPlayer?.start()
        // TODO: 2020/5/19
    }

    fun setBufferProgress(percent: Int) {
        // TODO: 2020/5/19
    }

    fun onSeekCompleted() {
        // TODO: 2020/5/19
    }

    fun onVideoSizeChanged(width: Int, height: Int) {
        // TODO: 2020/5/19
    }

    fun onInfo(what: Int, extra: Int) {
        // TODO: 2020/5/19
    }

    fun onError(what: Int, extra: Int) {
        // TODO: 2020/5/19
    }

    fun onAutoCompletion() {
        // TODO: 2020/5/19
    }



    private fun bindSurfaceHolder(mp: IMediaInterface?, holder: IRenderView.ISurfaceHolder?) {
        if (mp == null) return

        if (holder == null) {
            // TODO: 2020/5/19
            // mp.setDisplay(null)
            return
        }

        holder.bindToMediaPlayer(mp)
    }

    private val mSHCallback = object: IRenderView.IRenderCallback {
        override fun onSurfaceCreated(holder: IRenderView.ISurfaceHolder, width: Int, height: Int) {
            if (holder.renderView != mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
            }
            mSurfaceHolder = holder
            // iMediaPlayer?.let { bindSurfaceHolder(it, holder) }
            if (iMediaPlayer != null) {
                bindSurfaceHolder(iMediaPlayer, mSurfaceHolder)
            } else {
                iMediaPlayer?.prepare()
            }
        }

        override fun onSurfaceChanged(holder: IRenderView.ISurfaceHolder, format: Int, width: Int, height: Int) {
            TODO("Not yet implemented")
        }

        override fun onSurfaceDestroyed(holder: IRenderView.ISurfaceHolder) {
            TODO("Not yet implemented")
        }
    }
}
