package com.qfq.tainzhi.videoplayer.media

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.MediaController.MediaPlayerControl
import tv.danmaku.ijk.media.player.AndroidMediaPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.TextureMediaPlayer
import java.io.IOException
import java.util.*

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
class IjkVideoView : FrameLayout, MediaPlayerControl {
    private val TAG: String? = "IjkVideoView"
    
    // settable by the client
    private var mUri: Uri? = null
    private var mHeaders: MutableMap<String?, String?>? = null
    fun getCurrentState(): Int {
        return mCurrentState
    }
    
    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private var mCurrentState: Int = STATE_IDLE
    private var mTargetState: Int = STATE_IDLE
    
    // All the stuff we need for playing and showing a video
    private var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    private var mMediaPlayer: IMediaPlayer? = null
    
    // private int         mAudioSession;
    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0
    private var mSurfaceWidth: Int = 0
    private var mSurfaceHeight: Int = 0
    private var mVideoRotationDegree: Int = 0
    private var mMediaController: IMediaController? = null
    private var mOnCompletionListener: IMediaPlayer.OnCompletionListener? = null
    private var mOnPreparedListener: IMediaPlayer.OnPreparedListener? = null
    private var mCurrentBufferPercentage: Int = 0
    private var mOnErrorListener: IMediaPlayer.OnErrorListener? = null
    private var mOnInfoListener: IMediaPlayer.OnInfoListener? = null
    private var mSeekWhenPrepared // recording the seek position while preparing
            : Long = 0
    private val mCanPause: Boolean = true
    private val mCanSeekBack: Boolean = false
    private val mCanSeekForward: Boolean = false
    /** Subtitle rendering widget overlaid on top of the video.  */ // private RenderingWidget mSubtitleWidget;
    /**
     * Listener for changes to subtitle data, used to redraw when needed.
     */
    // private RenderingWidget.OnChangedListener mSubtitlesChangedListener;
    private var mAppContext: Context? = null
    private var mRenderView: IRenderView? = null
    private var mVideoSarNum: Int = 0
    private var mVideoSarDen: Int = 0
    private val usingAndroidPlayer: Boolean = false
    private val usingMediaCodec: Boolean = false
    private val usingMediaCodecAutoRotate: Boolean = false
    private val usingOpenSLES: Boolean = false
    private val pixelFormat: String? = "" //Auto Select=,RGB 565=fcc-rv16,RGB 888X=fcc-rv32,YV12=fcc-yv12,默认为RGB 888X
    private val enableBackgroundPlay: Boolean = false
    private val enableSurfaceView: Boolean = true
    private val enableTextureView: Boolean = false
    private val enableNoView: Boolean = false
    
    constructor(context: Context?) : super(context) {
        initVideoView(context)
    }
    
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initVideoView(context)
    }
    
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initVideoView(context)
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initVideoView(context)
    }
    
    // REMOVED: onMeasure
    // REMOVED: onInitializeAccessibilityEvent
    // REMOVED: onInitializeAccessibilityNodeInfo
    // REMOVED: resolveAdjustedSize
    private fun initVideoView(context: Context?) {
        mAppContext = context.getApplicationContext()
        initBackground()
        initRenders()
        mVideoWidth = 0
        mVideoHeight = 0
        // REMOVED: getHolder().addCallback(mSHCallback);
        // REMOVED: getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true)
        setFocusableInTouchMode(true)
        requestFocus()
        // REMOVED: mPendingSubtitleTracks = new Vector<Pair<InputStream, MediaFormat>>();
        mCurrentState = STATE_IDLE
        mTargetState = STATE_IDLE
    }
    
    fun setRenderView(renderView: IRenderView?) {
        if (mRenderView != null) {
            if (mMediaPlayer != null) mMediaPlayer.setDisplay(null)
            val renderUIView: View? = mRenderView.getView()
            mRenderView.removeRenderCallback(mSHCallback)
            mRenderView = null
            removeView(renderUIView)
        }
        if (renderView == null) return
        mRenderView = renderView
        renderView.setAspectRatio(mCurrentAspectRatio)
        if (mVideoWidth > 0 && mVideoHeight > 0) renderView.setVideoSize(mVideoWidth, mVideoHeight)
        if (mVideoSarNum > 0 && mVideoSarDen > 0) renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
        val renderUIView: View? = mRenderView.getView()
        val lp: LayoutParams? = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER)
        renderUIView.setLayoutParams(lp)
        addView(renderUIView)
        mRenderView.addRenderCallback(mSHCallback)
        mRenderView.setVideoRotation(mVideoRotationDegree)
    }
    
    fun setRender(render: Int) {
        when (render) {
            RENDER_NONE -> setRenderView(null)
            RENDER_TEXTURE_VIEW -> {
                val renderView: TextureRenderView? = TextureRenderView(getContext())
                if (mMediaPlayer != null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer)
                    renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight())
                    renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen())
                    renderView.setAspectRatio(mCurrentAspectRatio)
                }
                setRenderView(renderView)
            }
            RENDER_SURFACE_VIEW -> {
                val renderView: SurfaceRenderView? = SurfaceRenderView(getContext())
                setRenderView(renderView)
            }
            else -> Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render))
        }
    }
    
    /**
     * Sets video path.
     *
     * @param path the path of the video.
     */
    fun setVideoPath(path: String?) {
        setVideoURI(Uri.parse(path))
    }
    
    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    fun setVideoURI(uri: Uri?) {
        setVideoURI(uri, null)
    }
    
    /**
     * Sets video URI using specific headers.
     *
     * @param uri     the URI of the video.
     * @param headers the headers for the URI request.
     * Note that the cross domain redirection is allowed by default, but that can be
     * changed with key/value pairs through the headers parameter with
     * "android-allow-cross-domain-redirect" as the key and "0" or "1" as the value
     * to disallow or allow cross domain redirection.
     */
    private fun setVideoURI(uri: Uri?, headers: MutableMap<String?, String?>?) {
        mUri = uri
        mHeaders = headers
        mSeekWhenPrepared = 0
        openVideo()
        requestLayout()
        invalidate()
    }
    
    // REMOVED: addSubtitleSource
    // REMOVED: mPendingSubtitleTracks
    fun stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop()
            mMediaPlayer.release()
            mMediaPlayer = null
            mCurrentState = STATE_IDLE
            mTargetState = STATE_IDLE
            val am: AudioManager? = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
            am.abandonAudioFocus(null)
        }
    }
    
    private fun openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return
        }
        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false)
        val am: AudioManager? = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        try {
            if (usingAndroidPlayer) {
                mMediaPlayer = AndroidMediaPlayer()
            } else {
                var ijkMediaPlayer: IjkMediaPlayer? = null
                if (mUri != null) {
                    ijkMediaPlayer = IjkMediaPlayer()
                    IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                    if (usingMediaCodec) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
                        if (usingMediaCodecAutoRotate) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0)
                        }
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                    }
                    if (usingOpenSLES) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                    }
                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat)
                    }
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000000)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
                }
                mMediaPlayer = ijkMediaPlayer
            }
            if (enableBackgroundPlay) {
                mMediaPlayer = TextureMediaPlayer(mMediaPlayer)
            }
            
            // TODO: create SubtitleController in MediaPlayer, but we need
            // a context for the subtitle renderers
            val context: Context? = getContext()
            // REMOVED: SubtitleController
            
            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(mPreparedListener)
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener)
            mMediaPlayer.setOnCompletionListener(mCompletionListener)
            mMediaPlayer.setOnErrorListener(mErrorListener)
            mMediaPlayer.setOnInfoListener(mInfoListener)
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener)
            mCurrentBufferPercentage = 0
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer.setDataSource(mAppContext, mUri, mHeaders)
            } else {
                mMediaPlayer.setDataSource(mUri.toString())
            }
            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder)
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer.setScreenOnWhilePlaying(true)
            mMediaPlayer.prepareAsync()
            
            // REMOVED: mPendingSubtitleTracks
            
            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING
            attachMediaController()
        } catch (ex: IOException) {
            Log.w(TAG, "Unable to open content: " + mUri, ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
            return
        } catch (ex: IllegalArgumentException) {
            Log.w(TAG, "Unable to open content: " + mUri, ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
            return
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }
    
    fun setMediaController(controller: IMediaController?) {
        if (mMediaController != null) {
            mMediaController.hide()
        }
        mMediaController = controller
        attachMediaController()
    }
    
    private fun attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this)
            val anchorView: View? = if (getParent() is View) getParent() as View? else this
            mMediaController.setAnchorView(anchorView)
            mMediaController.setEnabled(isInPlaybackState())
        }
    }
    
    var mSizeChangedListener: IMediaPlayer.OnVideoSizeChangedListener? = object : IMediaPlayer.OnVideoSizeChangedListener {
        public override fun onVideoSizeChanged(mp: IMediaPlayer?, width: Int, height: Int, sarNum: Int, sarDen: Int) {
            mVideoWidth = mp.getVideoWidth()
            mVideoHeight = mp.getVideoHeight()
            mVideoSarNum = mp.getVideoSarNum()
            mVideoSarDen = mp.getVideoSarDen()
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                if (mRenderView != null) {
                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight)
                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                }
                // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                requestLayout()
            }
        }
    }
    var mPreparedListener: IMediaPlayer.OnPreparedListener? = object : IMediaPlayer.OnPreparedListener {
        public override fun onPrepared(mp: IMediaPlayer?) {
            mCurrentState = STATE_PREPARED
            
            // Get the capabilities of the player for this stream
            // REMOVED: Metadata
            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer)
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true)
            }
            mVideoWidth = mp.getVideoWidth()
            mVideoHeight = mp.getVideoHeight()
            val seekToPosition: Long = mSeekWhenPrepared // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0L) {
                seekTo(seekToPosition as Int)
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                if (mRenderView != null) {
                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight)
                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                    if (!mRenderView.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                        // We didn't actually change the size (it was already at the size
                        // we need), so we won't get a "surface changed" callback, so
                        // start the video here instead of in the callback.
                        if (mTargetState == STATE_PLAYING) {
                            start()
                            if (mMediaController != null) {
                                mMediaController.show()
                            }
                        } else if (!isPlaying() &&
                                (seekToPosition != 0L || getCurrentPosition() > 0)) {
                            if (mMediaController != null) {
                                // Show the media controls when we're paused into a video and make 'em stick.
                                mMediaController.show(0)
                            }
                        }
                    }
                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    start()
                }
            }
        }
    }
    private val mCompletionListener: IMediaPlayer.OnCompletionListener? = object : IMediaPlayer.OnCompletionListener {
        public override fun onCompletion(mp: IMediaPlayer?) {
            mCurrentState = STATE_PLAYBACK_COMPLETED
            mTargetState = STATE_PLAYBACK_COMPLETED
            if (mMediaController != null) {
                mMediaController.hide()
            }
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mMediaPlayer)
            }
        }
    }
    private val mInfoListener: IMediaPlayer.OnInfoListener? = object : IMediaPlayer.OnInfoListener {
        public override fun onInfo(mp: IMediaPlayer?, arg1: Int, arg2: Int): Boolean {
            if (mOnInfoListener != null) {
                mOnInfoListener.onInfo(mp, arg1, arg2)
            }
            when (arg1) {
                IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> {
                    mVideoRotationDegree = arg2
                    Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2)
                    if (mRenderView != null) mRenderView.setVideoRotation(arg2)
                }
            }
            return true
        }
    }
    private val mErrorListener: IMediaPlayer.OnErrorListener? = object : IMediaPlayer.OnErrorListener {
        public override fun onError(mp: IMediaPlayer?, framework_err: Int, impl_err: Int): Boolean {
            Log.d(TAG, "Error: " + framework_err + "," + impl_err)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            if (mMediaController != null) {
                mMediaController.hide()
            }
            
            /* If an error handler has been supplied, use it and finish. */if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                    return true
                }
            }
            
            /* Otherwise, pop up an error dialog so the user knows that
                     * something bad has happened. Only try and pop up the dialog
                     * if we're attached to a window. When we're going away and no
                     * longer have a window, don't bother showing the user an error.
                     */if (getWindowToken() != null) {
                val r: Resources? = mAppContext.getResources()
                var message: String? = "Unknown error"
                if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
                    message = "Invalid progressive playback"
                }
                AlertDialog.Builder(getContext())
                        .setMessage(message)
                        .setPositiveButton("error",
                                           object : DialogInterface.OnClickListener {
                                               public override fun onClick(dialog: DialogInterface?, whichButton: Int) {
                                                   /* If we get here, there is no onError listener, so
                                                 * at least inform them that the video is over.
                                                 */
                                                   if (mOnCompletionListener != null) {
                                                       mOnCompletionListener.onCompletion(mMediaPlayer)
                                                   }
                                               }
                                           })
                        .setCancelable(false)
                        .show()
            }
            return true
        }
    }
    private val mBufferingUpdateListener: IMediaPlayer.OnBufferingUpdateListener? = object : IMediaPlayer.OnBufferingUpdateListener {
        public override fun onBufferingUpdate(mp: IMediaPlayer?, percent: Int) {
            mCurrentBufferPercentage = percent
        }
    }
    
    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    fun setOnPreparedListener(l: IMediaPlayer.OnPreparedListener?) {
        mOnPreparedListener = l
    }
    
    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    fun setOnCompletionListener(l: IMediaPlayer.OnCompletionListener?) {
        mOnCompletionListener = l
    }
    
    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    fun setOnErrorListener(l: IMediaPlayer.OnErrorListener?) {
        mOnErrorListener = l
    }
    
    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    fun setOnInfoListener(l: IMediaPlayer.OnInfoListener?) {
        mOnInfoListener = l
    }
    
    // REMOVED: mSHCallback
    private fun bindSurfaceHolder(mp: IMediaPlayer?, holder: IRenderView.ISurfaceHolder?) {
        if (mp == null) return
        if (holder == null) {
            mp.setDisplay(null)
            return
        }
        holder.bindToMediaPlayer(mp)
    }
    
    var mSHCallback: IRenderView.IRenderCallback? = object : IRenderView.IRenderCallback {
        public override fun onSurfaceChanged(holder: IRenderView.ISurfaceHolder, format: Int, w: Int, h: Int) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceChanged: unmatched render callback\n")
                return
            }
            mSurfaceWidth = w
            mSurfaceHeight = h
            val isValidState: Boolean = (mTargetState == STATE_PLAYING)
            val hasValidSize: Boolean = !mRenderView.shouldWaitForResize() || (mVideoWidth == w && mVideoHeight == h)
            if ((mMediaPlayer != null) && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0L) {
                    seekTo(mSeekWhenPrepared as Int)
                }
                start()
            }
        }
        
        public override fun onSurfaceCreated(holder: IRenderView.ISurfaceHolder, width: Int, height: Int) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
                return
            }
            mSurfaceHolder = holder
            if (mMediaPlayer != null) bindSurfaceHolder(mMediaPlayer, holder) else openVideo()
        }
        
        public override fun onSurfaceDestroyed(holder: IRenderView.ISurfaceHolder) {
            if (holder.getRenderView() !== mRenderView) {
                Log.e(TAG, "onSurfaceDestroyed: unmatched render callback\n")
                return
            }
            
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null
            // REMOVED: if (mMediaController != null) mMediaController.hide();
            // REMOVED: release(true);
            releaseWithoutStop()
        }
    }
    
    fun releaseWithoutStop() {
        if (mMediaPlayer != null) mMediaPlayer.setDisplay(null)
    }
    
    /*
     * release the media player in any state
     */
    fun release(cleartargetstate: Boolean) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset()
            mMediaPlayer.release()
            mMediaPlayer = null
            // REMOVED: mPendingSubtitleTracks.clear();
            mCurrentState = STATE_IDLE
            if (cleartargetstate) {
                mTargetState = STATE_IDLE
            }
            val am: AudioManager? = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
            am.abandonAudioFocus(null)
        }
    }
    
    public override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }
    
    public override fun onTrackballEvent(ev: MotionEvent?): Boolean {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }
    
    public override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val isKeyCodeSupported: Boolean = (keyCode != KeyEvent.KEYCODE_BACK) && (
                keyCode != KeyEvent.KEYCODE_VOLUME_UP) && (
                keyCode != KeyEvent.KEYCODE_VOLUME_DOWN) && (
                keyCode != KeyEvent.KEYCODE_VOLUME_MUTE) && (
                keyCode != KeyEvent.KEYCODE_MENU) && (
                keyCode != KeyEvent.KEYCODE_CALL) && (
                keyCode != KeyEvent.KEYCODE_ENDCALL)
        if (isInPlaybackState() && isKeyCodeSupported && (mMediaController != null)) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause()
                    mMediaController.show()
                } else {
                    start()
                    mMediaController.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer.isPlaying()) {
                    start()
                    mMediaController.hide()
                }
                return true
            } else if ((keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                            || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE)) {
                if (mMediaPlayer.isPlaying()) {
                    pause()
                    mMediaController.show()
                }
                return true
            } else {
                toggleMediaControlsVisiblity()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    
    private fun toggleMediaControlsVisiblity() {
        if (mMediaController.isShowing()) {
            mMediaController.hide()
        } else {
            mMediaController.show()
        }
    }
    
    public override fun start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start()
            mCurrentState = STATE_PLAYING
        }
        mTargetState = STATE_PLAYING
    }
    
    public override fun pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause()
                mCurrentState = STATE_PAUSED
            }
        }
        mTargetState = STATE_PAUSED
    }
    
    fun suspend() {
        release(false)
    }
    
    fun resume() {
        openVideo()
    }
    
    public override fun getDuration(): Int {
        if (isInPlaybackState()) {
            return mMediaPlayer.getDuration() as Int
        }
        return -1
    }
    
    public override fun getCurrentPosition(): Int {
        if (isInPlaybackState()) {
            return mMediaPlayer.getCurrentPosition() as Int
        }
        return 0
    }
    
    public override fun seekTo(msec: Int) {
        if (isInPlaybackState()) {
            mMediaPlayer.seekTo(msec.toLong())
            mSeekWhenPrepared = 0
        } else {
            mSeekWhenPrepared = msec.toLong()
        }
    }
    
    public override fun isPlaying(): Boolean {
        return isInPlaybackState() && mMediaPlayer.isPlaying()
    }
    
    public override fun getBufferPercentage(): Int {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercentage
        }
        return 0
    }
    
    private fun isInPlaybackState(): Boolean {
        return ((mMediaPlayer != null) && (
                mCurrentState != STATE_ERROR) && (
                mCurrentState != STATE_IDLE) && (
                mCurrentState != STATE_PREPARING))
    }
    
    public override fun canPause(): Boolean {
        return mCanPause
    }
    
    public override fun canSeekBackward(): Boolean {
        return mCanSeekBack
    }
    
    public override fun canSeekForward(): Boolean {
        return mCanSeekForward
    }
    
    public override fun getAudioSessionId(): Int {
        return 0
    }
    
    private var mCurrentAspectRatioIndex: Int = 0
    private var mCurrentAspectRatio: Int = s_allAspectRatio.get(mCurrentAspectRatioIndex)
    fun toggleAspectRatio(): Int {
        mCurrentAspectRatioIndex++
        mCurrentAspectRatioIndex %= s_allAspectRatio.size
        mCurrentAspectRatio = s_allAspectRatio.get(mCurrentAspectRatioIndex)
        if (mRenderView != null) mRenderView.setAspectRatio(mCurrentAspectRatio)
        return mCurrentAspectRatio
    }
    
    private val mAllRenders: MutableList<Int?>? = ArrayList()
    private var mCurrentRenderIndex: Int = 0
    private var mCurrentRender: Int = RENDER_NONE
    private fun initRenders() {
        mAllRenders.clear()
        if (enableSurfaceView) mAllRenders.add(RENDER_SURFACE_VIEW)
        if (enableTextureView && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) mAllRenders.add(RENDER_TEXTURE_VIEW)
        if (enableNoView) mAllRenders.add(RENDER_NONE)
        if (mAllRenders.isEmpty()) mAllRenders.add(RENDER_SURFACE_VIEW)
        mCurrentRender = mAllRenders.get(mCurrentRenderIndex)
        setRender(mCurrentRender)
    }
    
    fun toggleRender(): Int {
        mCurrentRenderIndex++
        mCurrentRenderIndex %= mAllRenders.size
        mCurrentRender = mAllRenders.get(mCurrentRenderIndex)
        setRender(mCurrentRender)
        return mCurrentRender
    }
    
    //-------------------------
    // Extend: Background
    //-------------------------
    private fun initBackground() {
        if (enableBackgroundPlay) {
            //            MediaPlayerService.intentToStart(getContext());
            //            mMediaPlayer = MediaPlayerService.getMediaPlayer();
        }
    }
    
    fun setAspectRatio(aspectRatio: Int) {
        for (i in s_allAspectRatio.indices) {
            if (s_allAspectRatio.get(i) == aspectRatio) {
                mCurrentAspectRatioIndex = i
                if (mRenderView != null) {
                    mRenderView.setAspectRatio(mCurrentAspectRatio)
                }
                break
            }
        }
    }
    
    companion object {
        // all possible internal states
        val STATE_ERROR: Int = -1
        val STATE_IDLE: Int = 0
        val STATE_PREPARING: Int = 1
        val STATE_PREPARED: Int = 2
        val STATE_PLAYING: Int = 3
        val STATE_PAUSED: Int = 4
        val STATE_PLAYBACK_COMPLETED: Int = 5
        
        // REMOVED: getAudioSessionId();
        // REMOVED: onAttachedToWindow();
        // REMOVED: onDetachedFromWindow();
        // REMOVED: onLayout();
        // REMOVED: draw();
        // REMOVED: measureAndLayoutSubtitleWidget();
        // REMOVED: setSubtitleWidget();
        // REMOVED: getSubtitleLooper();
        //-------------------------
        // Extend: Aspect Ratio
        //-------------------------
        private val s_allAspectRatio: IntArray? = intArrayOf(
                IRenderView.Companion.AR_ASPECT_FIT_PARENT,
                IRenderView.Companion.AR_ASPECT_FILL_PARENT,
                IRenderView.Companion.AR_ASPECT_WRAP_CONTENT,
                IRenderView.Companion.AR_MATCH_PARENT,
                IRenderView.Companion.AR_16_9_FIT_PARENT,
                IRenderView.Companion.AR_4_3_FIT_PARENT)
        
        //-------------------------
        // Extend: Render
        //-------------------------
        val RENDER_NONE: Int = 0
        val RENDER_SURFACE_VIEW: Int = 1
        val RENDER_TEXTURE_VIEW: Int = 2
    }
}