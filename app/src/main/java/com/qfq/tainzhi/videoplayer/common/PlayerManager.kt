package com.qfq.tainzhi.videoplayer.common

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.media.AudioManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.media.IRenderView
import com.qfq.tainzhi.videoplayer.media.IjkVideoView
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
/**
 * Created by tcking on 15/10/27.
 */
class PlayerManager constructor(activity: Activity?) {
    private val activity: Activity?
    private val videoView: IjkVideoView?
    private val audioManager: AudioManager?
    private val mMaxVolume: Int
    private val playerSupport: Boolean = false
    private var url: String? = null
    private val STATUS_ERROR: Int = -1
    private val STATUS_IDLE: Int = 0
    private val STATUS_LOADING: Int = 1
    private val STATUS_PLAYING: Int = 2
    private val STATUS_PAUSE: Int = 3
    private val STATUS_COMPLETED: Int = 4
    private var pauseTime: Long = 0
    private var status: Int = STATUS_IDLE
    private var isLive: Boolean = false //是否为直播
    private val orientationEventListener: OrientationEventListener?
    private val defaultTimeout: Int = 3000
    private val screenWidthPixels: Int
    private val isShowing: Boolean = false
    private val portrait: Boolean
    private var brightness: Float = -1f
    private var volume: Int = -1
    private var newPosition: Long = -1
    private var defaultRetryTime: Long = 5000
    private var playerStateListener: PlayerStateListener? = null
    fun setPlayerStateListener(playerStateListener: PlayerStateListener?) {
        this.playerStateListener = playerStateListener
    }
    
    private var onErrorListener: OnErrorListener? = object : OnErrorListener {
        public override fun onError(what: Int, extra: Int) {}
    }
    private var oncomplete: Runnable? = object : Runnable {
        public override fun run() {}
    }
    private var onInfoListener: OnInfoListener? = object : OnInfoListener {
        public override fun onInfo(what: Int, extra: Int) {}
    }
    private var onControlPanelVisibilityChangeListener: OnControlPanelVisibilityChangeListener? = object : OnControlPanelVisibilityChangeListener {
        public override fun change(isShowing: Boolean) {}
    }
    
    /**
     * try to play when error(only for live video)
     * @param defaultRetryTime millisecond,0 will stop retry,default is 5000 millisecond
     */
    fun setDefaultRetryTime(defaultRetryTime: Long) {
        this.defaultRetryTime = defaultRetryTime
    }
    
    private var currentPosition: Int = 0
    private var fullScreenOnly: Boolean = false
    private val duration: Long = 0
    private val instantSeeking: Boolean = false
    private val isDragging: Boolean = false
    private fun statusChange(newStatus: Int) {
        status = newStatus
        if (!isLive && newStatus == STATUS_COMPLETED) {
            Logger.d("statusChange STATUS_COMPLETED...")
            if (playerStateListener != null) {
                playerStateListener.onComplete()
            }
        } else if (newStatus == STATUS_ERROR) {
            Logger.d("statusChange STATUS_ERROR...")
            if (playerStateListener != null) {
                playerStateListener.onError()
            }
        } else if (newStatus == STATUS_LOADING) {
            //            $.id(R.id.app_video_loading).visible();
            if (playerStateListener != null) {
                playerStateListener.onLoading()
            }
            Logger.d("statusChange STATUS_LOADING...")
        } else if (newStatus == STATUS_PLAYING) {
            Logger.d("statusChange STATUS_PLAYING...")
            if (playerStateListener != null) {
                playerStateListener.onPlay()
            }
        }
    }
    
    fun onPause() {
        pauseTime = System.currentTimeMillis()
        if (status == STATUS_PLAYING) {
            videoView.pause()
            if (!isLive) {
                currentPosition = videoView.getCurrentPosition()
            }
        }
    }
    
    fun onResume() {
        pauseTime = 0
        if (status == STATUS_PLAYING) {
            if (isLive) {
                videoView.seekTo(0)
            } else {
                if (currentPosition > 0) {
                    videoView.seekTo(currentPosition)
                }
            }
            videoView.start()
        }
    }
    
    private fun tryFullScreen(fullScreen: Boolean) {
        if (activity is AppCompatActivity) {
            val supportActionBar: ActionBar? = (activity as AppCompatActivity?).getSupportActionBar()
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide()
                } else {
                    supportActionBar.show()
                }
            }
        }
        setFullScreen(fullScreen)
    }
    
    private fun setFullScreen(fullScreen: Boolean) {
        if (activity != null) {
            val attrs: WindowManager.LayoutParams? = activity.getWindow().getAttributes()
            if (fullScreen) {
                attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                activity.getWindow().setAttributes(attrs)
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            } else {
                attrs.flags = attrs.flags and (WindowManager.LayoutParams.FLAG_FULLSCREEN.inv())
                activity.getWindow().setAttributes(attrs)
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
        }
    }
    
    fun onDestroy() {
        //        orientationEventListener.disable();
        videoView.stopPlayback()
    }
    
    fun play(url: String?) {
        this.url = url
        if (playerSupport) {
            videoView.setVideoPath(url)
            videoView.start()
        }
    }
    
    private fun generateTime(time: Long): String? {
        val totalSeconds: Int = (time / 1000) as Int
        val seconds: Int = totalSeconds % 60
        val minutes: Int = (totalSeconds / 60) % 60
        val hours: Int = totalSeconds / 3600
        return if (hours > 0) String.format("%02d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds)
    }
    
    private fun getScreenOrientation(): Int {
        val rotation: Int = activity.getWindowManager().getDefaultDisplay().getRotation()
        val dm: DisplayMetrics? = DisplayMetrics()
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm)
        val width: Int = dm.widthPixels
        val height: Int = dm.heightPixels
        val orientation: Int
        // if the device's natural orientation is portrait:
        if (((rotation == Surface.ROTATION_0
                        || rotation == Surface.ROTATION_180)) && height > width ||
                ((rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270)) && width > height) {
            when (rotation) {
                Surface.ROTATION_0 -> orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_90 -> orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_180 -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                Surface.ROTATION_270 -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                else -> orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        } else {
            when (rotation) {
                Surface.ROTATION_0 -> orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                Surface.ROTATION_90 -> orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                Surface.ROTATION_180 -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                Surface.ROTATION_270 -> orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                else -> orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
        return orientation
    }
    
    /**
     * 滑动改变声音大小
     *
     * @param percent
     */
    private fun onVolumeSlide(percent: Float) {
        if (volume == -1) {
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            if (volume < 0) volume = 0
        }
        var index: Int = (percent * mMaxVolume) as Int + volume
        if (index > mMaxVolume) index = mMaxVolume else if (index < 0) index = 0
        
        // 变更声音
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0)
        
        // 变更进度条
        val i: Int = (index * 1.0 / mMaxVolume * 100) as Int
        var s: String? = i.toString() + "%"
        if (i == 0) {
            s = "off"
        }
        Logger.d("onVolumeSlide:" + s)
    }
    
    private fun onProgressSlide(percent: Float) {
        val position: Long = videoView.getCurrentPosition().toLong()
        val duration: Long = videoView.getDuration().toLong()
        val deltaMax: Long = Math.min(100 * 1000.toLong(), duration - position)
        var delta: Long = (deltaMax * percent) as Long
        newPosition = delta + position
        if (newPosition > duration) {
            newPosition = duration
        } else if (newPosition <= 0) {
            newPosition = 0
            delta = -position
        }
        val showDelta: Int = delta as Int / 1000
        if (showDelta != 0) {
            val text: String? = if (showDelta > 0) ("+" + showDelta) else "" + showDelta
            Logger.d("onProgressSlide:" + text)
        }
    }
    
    /**
     * 滑动改变亮度
     *
     * @param percent
     */
    private fun onBrightnessSlide(percent: Float) {
        if (brightness < 0) {
            brightness = activity.getWindow().getAttributes().screenBrightness
            if (brightness <= 0.00f) {
                brightness = 0.50f
            } else if (brightness < 0.01f) {
                brightness = 0.01f
            }
        }
        Logger.d("brightness:" + brightness + ",percent:" + percent)
        val lpa: WindowManager.LayoutParams? = activity.getWindow().getAttributes()
        lpa.screenBrightness = brightness + percent
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f
        }
        activity.getWindow().setAttributes(lpa)
    }
    
    fun setFullScreenOnly(fullScreenOnly: Boolean) {
        this.fullScreenOnly = fullScreenOnly
        tryFullScreen(fullScreenOnly)
        if (fullScreenOnly) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        }
    }
    
    /**
     * <pre>
     * fitParent:可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
     * fillParent:可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
     * wrapContent:将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
     * fitXY:不剪裁,非等比例拉伸画面填满整个View
     * 16:9:不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
     * 4:3:不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
    </pre> *
     * @param scaleType
     */
    fun setScaleType(scaleType: String?) {
        if ((SCALETYPE_FITPARENT == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_ASPECT_FIT_PARENT)
        } else if ((SCALETYPE_FILLPARENT == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_ASPECT_FILL_PARENT)
        } else if ((SCALETYPE_WRAPCONTENT == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_ASPECT_WRAP_CONTENT)
        } else if ((SCALETYPE_FITXY == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_MATCH_PARENT)
        } else if ((SCALETYPE_16_9 == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_16_9_FIT_PARENT)
        } else if ((SCALETYPE_4_3 == scaleType)) {
            videoView.setAspectRatio(IRenderView.Companion.AR_4_3_FIT_PARENT)
        }
    }
    
    fun start() {
        videoView.start()
    }
    
    fun pause() {
        videoView.pause()
    }
    
    fun onBackPressed(): Boolean {
        if (!fullScreenOnly && getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            return true
        }
        return false
    }
    
    internal inner class Query constructor(private val activity: Activity?) {
        private var view: View? = null
        fun id(id: Int): Query? {
            view = activity.findViewById(id)
            return this
        }
        
        fun image(resId: Int): Query? {
            if (view is ImageView) {
                (view as ImageView?).setImageResource(resId)
            }
            return this
        }
        
        fun visible(): Query? {
            if (view != null) {
                view.setVisibility(View.VISIBLE)
            }
            return this
        }
        
        fun gone(): Query? {
            if (view != null) {
                view.setVisibility(View.GONE)
            }
            return this
        }
        
        fun invisible(): Query? {
            if (view != null) {
                view.setVisibility(View.INVISIBLE)
            }
            return this
        }
        
        fun clicked(handler: View.OnClickListener?): Query? {
            if (view != null) {
                view.setOnClickListener(handler)
            }
            return this
        }
        
        fun text(text: CharSequence?): Query? {
            if (view != null && view is TextView) {
                (view as TextView?).setText(text)
            }
            return this
        }
        
        fun visibility(visible: Int): Query? {
            if (view != null) {
                view.setVisibility(visible)
            }
            return this
        }
        
        private fun size(width: Boolean, n: Int, dip: Boolean) {
            var n: Int = n
            if (view != null) {
                val lp: ViewGroup.LayoutParams? = view.getLayoutParams()
                if (n > 0 && dip) {
                    n = dip2pixel(activity, n.toFloat())
                }
                if (width) {
                    lp.width = n
                } else {
                    lp.height = n
                }
                view.setLayoutParams(lp)
            }
        }
        
        fun height(height: Int, dip: Boolean) {
            size(false, height, dip)
        }
        
        fun dip2pixel(context: Context?, n: Float): Int {
            val value: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n, context.getResources().getDisplayMetrics()) as Int
            return value
        }
        
        fun pixel2dip(context: Context?, n: Float): Float {
            val resources: Resources? = context.getResources()
            val metrics: DisplayMetrics? = resources.getDisplayMetrics()
            val dp: Float = n / (metrics.densityDpi / 160f)
            return dp
        }
        
    }
    
    inner class PlayerGestureListener constructor() : SimpleOnGestureListener() {
        private var firstTouch: Boolean = false
        private var volumeControl: Boolean = false
        private var toSeek: Boolean = false
        
        /**
         * 双击
         */
        public override fun onDoubleTap(e: MotionEvent?): Boolean {
            videoView.toggleAspectRatio()
            return true
        }
        
        public override fun onDown(e: MotionEvent?): Boolean {
            firstTouch = true
            return super.onDown(e)
        }
        
        /**
         * 滑动
         */
        public override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            val mOldX: Float = e1.getX()
            val mOldY: Float = e1.getY()
            val deltaY: Float = mOldY - e2.getY()
            val deltaX: Float = mOldX - e2.getX()
            if (firstTouch) {
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY)
                volumeControl = mOldX > screenWidthPixels * 0.5f
                firstTouch = false
            }
            if (toSeek) {
                if (!isLive) {
                    onProgressSlide(-deltaX / videoView.getWidth())
                }
            } else {
                val percent: Float = deltaY / videoView.getHeight()
                if (volumeControl) {
                    onVolumeSlide(percent)
                } else {
                    onBrightnessSlide(percent)
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
        
        public override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return true
        }
    }
    
    /**
     * is player support this device
     * @return
     */
    fun isPlayerSupport(): Boolean {
        return playerSupport
    }
    
    /**
     * 是否正在播放
     * @return
     */
    fun isPlaying(): Boolean {
        return if (videoView != null) videoView.isPlaying() else false
    }
    
    fun stop() {
        videoView.stopPlayback()
    }
    
    fun getCurrentPosition(): Int {
        return videoView.getCurrentPosition()
    }
    
    /**
     * get video duration
     * @return
     */
    fun getDuration(): Int {
        return videoView.getDuration()
    }
    
    fun playInFullScreen(fullScreen: Boolean): PlayerManager? {
        if (fullScreen) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
        return this
    }
    
    open interface OnErrorListener {
        open fun onError(what: Int, extra: Int)
    }
    
    open interface OnControlPanelVisibilityChangeListener {
        open fun change(isShowing: Boolean)
    }
    
    open interface OnInfoListener {
        open fun onInfo(what: Int, extra: Int)
    }
    
    fun onError(onErrorListener: OnErrorListener?): PlayerManager? {
        this.onErrorListener = onErrorListener
        return this
    }
    
    fun onComplete(complete: Runnable?): PlayerManager? {
        oncomplete = complete
        return this
    }
    
    fun onInfo(onInfoListener: OnInfoListener?): PlayerManager? {
        this.onInfoListener = onInfoListener
        return this
    }
    
    fun onControlPanelVisibilityChang(listener: OnControlPanelVisibilityChangeListener?): PlayerManager? {
        onControlPanelVisibilityChangeListener = listener
        return this
    }
    
    /**
     * set is live (can't seek forward)
     * @param isLive
     * @return
     */
    fun live(isLive: Boolean): PlayerManager? {
        this.isLive = isLive
        return this
    }
    
    fun toggleAspectRatio(): PlayerManager? {
        if (videoView != null) {
            videoView.toggleAspectRatio()
        }
        return this
    }
    
    fun onControlPanelVisibilityChange(listener: OnControlPanelVisibilityChangeListener?): PlayerManager? {
        onControlPanelVisibilityChangeListener = listener
        return this
    }
    
    open interface PlayerStateListener {
        open fun onComplete()
        open fun onError()
        open fun onLoading()
        open fun onPlay()
    }
    
    companion object {
        /**
         * 可能会剪裁,保持原视频的大小，显示在中心,当原视频的大小超过view的大小超过部分裁剪处理
         */
        val SCALETYPE_FITPARENT: String? = "fitParent"
        
        /**
         * 可能会剪裁,等比例放大视频，直到填满View为止,超过View的部分作裁剪处理
         */
        val SCALETYPE_FILLPARENT: String? = "fillParent"
        
        /**
         * 将视频的内容完整居中显示，如果视频大于view,则按比例缩视频直到完全显示在view中
         */
        val SCALETYPE_WRAPCONTENT: String? = "wrapContent"
        
        /**
         * 不剪裁,非等比例拉伸画面填满整个View
         */
        val SCALETYPE_FITXY: String? = "fitXY"
        
        /**
         * 不剪裁,非等比例拉伸画面到16:9,并完全显示在View中
         */
        val SCALETYPE_16_9: String? = "16:9"
        
        /**
         * 不剪裁,非等比例拉伸画面到4:3,并完全显示在View中
         */
        val SCALETYPE_4_3: String? = "4:3"
    }
    
    init {
        try {
            IjkMediaPlayer.loadLibrariesOnce(null)
            IjkMediaPlayer.native_profileBegin("libijkplayer.so")
            playerSupport = true
        } catch (e: Throwable) {
            Log.e("GiraffePlayer", "loadLibraries error", e)
        }
        this.activity = activity
        screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels
        videoView = activity.findViewById<View?>(R.id.video_view) as IjkVideoView?
        videoView.setOnCompletionListener(object : IMediaPlayer.OnCompletionListener {
            public override fun onCompletion(mp: IMediaPlayer?) {
                statusChange(STATUS_COMPLETED)
                oncomplete.run()
            }
        })
        videoView.setOnErrorListener(object : IMediaPlayer.OnErrorListener {
            public override fun onError(mp: IMediaPlayer?, what: Int, extra: Int): Boolean {
                statusChange(STATUS_ERROR)
                onErrorListener.onError(what, extra)
                return true
            }
        })
        videoView.setOnInfoListener(object : IMediaPlayer.OnInfoListener {
            public override fun onInfo(mp: IMediaPlayer?, what: Int, extra: Int): Boolean {
                when (what) {
                    IMediaPlayer.MEDIA_INFO_BUFFERING_START -> statusChange(STATUS_LOADING)
                    IMediaPlayer.MEDIA_INFO_BUFFERING_END -> statusChange(STATUS_PLAYING)
                    IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH -> {
                    }
                    IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> statusChange(STATUS_PLAYING)
                }
                onInfoListener.onInfo(what, extra)
                return false
            }
        })
        audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        mMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val gestureDetector: GestureDetector? = GestureDetector(activity, PlayerGestureListener())
        if (fullScreenOnly) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
        portrait = getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (!playerSupport) {
            Logger.e("播放器不支持此设备")
        }
        
        //woc 这个没实现
        orientationEventListener = object : OrientationEventListener(activity) {
            public override fun onOrientationChanged(orientation: Int) {}
        }
    }
}