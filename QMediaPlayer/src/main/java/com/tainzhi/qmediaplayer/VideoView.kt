package com.tainzhi.qmediaplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tainzhi.qmediaplayer.Constant.PlayState.STATE_ERROR
import com.tainzhi.qmediaplayer.Constant.PlayState.STATE_IDLE
import com.tainzhi.qmediaplayer.Constant.PlayState.STATE_PAUSE
import com.tainzhi.qmediaplayer.Constant.PlayState.STATE_PLAYING
import com.tainzhi.qmediaplayer.Constant.PlayState.STATE_PREPARED
import com.tainzhi.qmediaplayer.controller.IController
import com.tainzhi.qmediaplayer.render.GLRenderView
import com.tainzhi.qmediaplayer.render.IRenderView
import com.tainzhi.qmediaplayer.render.SurfaceRenderView
import com.tainzhi.qmediaplayer.render.TextureRenderView
import com.tainzhi.qmediaplayer.render.glrender.effect.ShaderInterface

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 * Description: 基本 VideoView, 没有各种操作控件 Controller
 */

class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) , LifecycleObserver {

    companion object {
        const val TAG = "VideoView"

    }
    private var iMediaPlayer: IMediaInterface? = null

    var mediaController: IController? = null
        set(value) {
            field = value
            attachMediaController()
        }
    // 上一次通过翻转屏幕, 自动全屏时间
    var lastAutoFullScreenTime = 0

    var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    private var mRenderView: IRenderView? = null


    var videoWidth = 0
    var videoHeight = 0

    var videoSarNum = 0
    var videoSarDen = 0

    var videoTitle = ""

    // FIXME: 2020/5/27 最好添加判断, 只要在播放时才返回当前进度
    val videoCurrentPosition
        get() = iMediaPlayer?.currentPosition ?: 0
    val videoDuration
        get() = iMediaPlayer?.duration ?: 0

    var videoRotationDegree = 0

    var screenOrientation = 0 // 0 或者90

    var videoUri: Uri? = null
        set(value) {
            field = value
            openVideo()
        }

    @Constant.PlayerTypeMode
    var mediaPlayerType = Constant.PlayerType.SYSTEM_PLAYER

    // TODO: 2020/6/21 GL_SURFACE_VIEW 没有实现onSurfaceDestoryed
    var renderType = Constant.RenderType.SURFACE_VIEW
        set(@Constant.RenderTypeMode value) {
            field = value
            setRender()
        }

    @Constant.ScreenTypeMode
    var screenType = Constant.ScreenType.FULL_SCREEN
        set(value) {
            field = value
            if (value == Constant.ScreenType.FULL_SCREEN)
                setFullScreen()
        }

    var aspectRatio = Constant.AspectRatio.AR_ASPECT_FILL_PARENT
        set(value) {
            field = value
            setScreenAspectRatio(value)
        }
    // 是否循环播放
    var loop = true

    val isPlaying : Boolean
        get() = iMediaPlayer?.isPlaying ?: false

    fun setEffect(effect: ShaderInterface) {
        mRenderView?.renderEffect = effect
    }

    fun takeShotPic(highShot: Boolean, videoShotListener: IRenderView.VideoShotListener) {
        mRenderView?.takeShot(videoShotListener, highShot)
    }

    // 播放器状态
    private var state = STATE_IDLE


    /**
     * GLSurfaceView onResume
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (renderType == Constant.RenderType.GL_SURFACE_VIEW) {
            (mRenderView as GLRenderView).onResume()
        }
        startPlay()
    }

    /**
     * GLSurfaceView onPause
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (renderType == Constant.RenderType.GL_SURFACE_VIEW) {
            (mRenderView as GLRenderView).onPause()
        }
        pausePlay()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        iMediaPlayer?.release()
    }

    // 重新加载网络uri, 即刷新
    fun resetDataSource() {
        iMediaPlayer?.resetDataSource(videoUri!!)
    }


    private fun openVideo() {
        setRender()
        setMediaPlayer()
        Util.scanForActivity(context)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 初始化渲染器
     */
    private fun setRender() {
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
            Constant.RenderType.GL_SURFACE_VIEW -> {
                setRenderView(GLRenderView(context))
            }
        }
    }

    private fun setRenderView(renderView: IRenderView) {
        if (mRenderView != null) {
            // IMediaPlayer.setDisplay(null)
            // iMediaPlayer.setDisplay(null)

            val renderUIView = mRenderView as View
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

        val renderUIView = mRenderView as View
        renderUIView.layoutParams = LayoutParams(
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
                IMediaIjk(this)
            }
            Constant.PlayerType.EXO_PLAYER -> {
                // mediaPlayerClass = IMediaExo::class.java
                IMediaExo(this)
            }
            Constant.PlayerType.FFmpeg_PLAYER -> {
                IMediaFFmpeg(this)
            }
            else -> null
        }
        state = STATE_IDLE
    }

    /**
     * 设置controller
     */
    private fun attachMediaController() {
        if (mediaController != null) {
            mediaController?.bindVideoView(this@VideoView)
        }
    }

    // 设置全屏
    private fun setFullScreen() {

    }

    /**
     * @param aspectRatio 宽高比率
     */
    private fun setScreenAspectRatio(aspectRatio: Int) {
        mRenderView?.setAspectRatio(aspectRatio)
    }

    /**
     * 直接全屏(横屏)播放视频
     *
     * @param context 播放activity
     * @param uri uri of video
     * @param videoName name of video
     */
    fun startFullScreenDirectly(context: Context, uri: Uri, videoName: String = "") {
        fullScreenHideAll(context)
        // val viewGroup: ViewGroup = Util.scanForActivity(context)?.window?.decorView as ViewGroup
        // viewGroup.removeAllViews()
        // val layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        // viewGroup.addView(this, layoutParams)

        videoUri = uri
    }
    

    private fun fullScreenHideAll(context: Context) {
        if (screenOrientation == 0) {
            Util.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }  else {
            Util.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
        Util.hideSystemUI(context)
        Util.hideActionBar(context)
        Util.hideStatusBar(context)
    }

    fun autoFullScreen(x: Float) {

        if ((state == STATE_PLAYING || state == STATE_PAUSE)
                // && (screenType != Constant.ScreenType.FULL_SCREEN)
                && (screenType != Constant.ScreenType.TINY_SCREEN)) {
            if (x > 0) {
                Util.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            } else {
                Util.setRequestedOrientation(context, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
            }
            gotoFullScreen()
        }
    }

    private fun gotoFullScreen() {
        val parentView: ViewGroup = parent as ViewGroup
        // if (screenType == Constant.ScreenType.FULL_SCREEN) {
        //     val childLayoutParams = this@VideoView.layoutParams
        //     (parentView).removeView(this@VideoView)
        //     // ((Util.scanForActivity(context))?.window?.decorView as ViewGroup)
        //     //         .addView(this@VideoView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        //     parentView.addView(this@VideoView, childLayoutParams)
        // } else {
        //     // todo
        //     parentView.addView(this)
        // }
        fullScreenHideAll(context)
    }

    fun startPlay() {
        if (state == STATE_PREPARED || state == STATE_PAUSE) {
            state = STATE_PLAYING
            iMediaPlayer?.start()
        }
        mediaController?.dismissLoading()
    }

    fun pausePlay() {
        if (state == STATE_PLAYING) {
            state = STATE_PAUSE
            iMediaPlayer?.pause()
        }
    }

    private var seekWhenPrepared = 0L
    fun seekTo(progress: Long) {
        if (isPlaying) {
            iMediaPlayer?.seekTo(progress)
            seekWhenPrepared = 0
        } else {
            seekWhenPrepared = progress
        }
    }

    fun onPrepared() {
        Log.i(TAG, "onPrepared ")
        state = STATE_PREPARED
        if (seekWhenPrepared != 0L) {
            iMediaPlayer?.seekTo(seekWhenPrepared)
        }
        startPlay()
        state = STATE_PLAYING
    }

    fun setBufferProgress(percent: Int) {
        // TODO: 2020/5/19
    }

    fun onSeekCompleted() {
        // TODO: 2020/5/19
    }

    fun onVideoSizeChanged(width: Int, height: Int) {
        // FIXME: 2020/5/27  
        setScreenOrientation(width, height)
        videoWidth = width
        videoHeight = height
        if (width != 0 && height != 0) {
            mRenderView?.let {
                it.setVideoSize(width, height)
                // TODO: 2020/5/20 for IjkMediaPlayer
                // it.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
            }
            requestLayout()
        }
        // 改变orientation
        mediaController?.changeOrientation()
    }

    fun onInfo(what: Int, extra: Int) {
        // TODO: 2020/5/19
    }

    fun onError(what: Int, extra: Int) {
        // TODO: 2020/5/19
        state = STATE_ERROR
    }

    /**
     * @param width 视频宽
     * @param height 视频高
     *
     * 如果width < height, 则该视频是竖屏视频, 需要竖屏全屏播放
     */
    private fun setScreenOrientation(width: Int, height: Int) {
        if (width > 0 && height > 0 && width < height) {
            screenOrientation = 90
            Util.setRequestedOrientation(Util.scanForActivity(context)!!
                    , ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else {
            screenOrientation = 0
            Util.setRequestedOrientation(Util.scanForActivity(context)!!
                    , ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
    }

    fun onAutoCompletion() {
        logD()
        Runtime.getRuntime().gc()
        iMediaPlayer?.release()
        Util.scanForActivity(context)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN &&
                isInPlaybackState() &&
                mediaController != null
        ) {
            toggleMediaControllerVisibility()
        }
        return false
    }

    private fun toggleMediaControllerVisibility() {
        if (mediaController!!.isShowing) {
            mediaController!!.hide()
        } else {
            mediaController!!.show()
        }
    }


    private fun isInPlaybackState() = iMediaPlayer != null &&
            state != Constant.PlayState.STATE_ERROR &&
            state != Constant.PlayState.STATE_IDLE &&
            state != Constant.PlayState.STATE_PREPARING

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE &&
                keyCode != KeyEvent.KEYCODE_MENU &&
                keyCode != KeyEvent.KEYCODE_CALL &&
                keyCode != KeyEvent.KEYCODE_ENDCALL
        if (isInPlaybackState() && isKeyCodeSupported && mediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (iMediaPlayer!!.isPlaying) {
                    pausePlay()
                    mediaController!!.show()
                } else {
                    iMediaPlayer?.start()
                    mediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!iMediaPlayer!!.isPlaying) {
                    startPlay()
                    mediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (iMediaPlayer!!.isPlaying) {
                    pausePlay()
                    mediaController!!.show()
                }
                return true
            } else {
                toggleMediaControllerVisibility()
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    private val mSHCallback = object : IRenderView.IRenderCallback {
        override fun onSurfaceCreated(holder: IRenderView.ISurfaceHolder, width: Int, height: Int) {
            if (holder.renderView != mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
            }
            mSurfaceHolder = holder
            // iMediaPlayer?.let { bindSurfaceHolder(it, holder) }
            if (iMediaPlayer == null) return
            if (iMediaPlayer!!.initialized()) {
                bindSurfaceHolder(iMediaPlayer, mSurfaceHolder)
            } else {
                iMediaPlayer?.prepare()
            }
        }

        override fun onSurfaceChanged(holder: IRenderView.ISurfaceHolder, format: Int, width: Int, height: Int) {
            if (holder.renderView != mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
                return
            }
            iMediaPlayer?.start()
        }

        override fun onSurfaceDestroyed(holder: IRenderView.ISurfaceHolder) {
            logD()
        }
    }



}

class AutoFullScreenListener(val videoView: VideoView): SensorEventListener {
    private var lastAutoFullScreenTime = 0L

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[SensorManager.DATA_X]
        if (x < -12 || x > 12) {
            if (System.currentTimeMillis() - lastAutoFullScreenTime > 2000) {
                // TODO: 2020/5/21 翻转还有问题
                // videoView.autoFullScreen(x)
                lastAutoFullScreenTime = System.currentTimeMillis()
            }
        }
    }

}
