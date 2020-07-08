package com.tanzhi.qmediaplayer

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.Group
import com.tanzhi.qmediaplayer.floatwindow.FloatWindow
import com.tanzhi.qmediaplayer.render.IRenderView
import java.lang.Integer.min
import kotlin.math.abs

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/22 17:03
 * @description: 播放控制器, 添加控制按钮, 控制[VideoView]播放
 **/

class MediaController(val context: Context) {

    private lateinit var parent: ViewGroup
    private lateinit var videoView: VideoView
    private lateinit var contentView: View
    private val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    // 当前MediaController是否显示
    var isShowing = false

    // 是否在拖动进度条
    private var isDragging = false


    private lateinit var playPauseBtn: ImageButton
    private lateinit var progressSeekbar: SeekBar
    private lateinit var endTimeTv: TextView
    private lateinit var currentTimeTv: TextView

    // 悬浮窗播放
    private lateinit var floatWindow: FloatWindow

    private val audioManager by lazy {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }

    companion object {
        const val DefaultTimeout = 3000L

        // 滑动灵敏度
        const val MOVE_DETECT_THRESHOLD = 80
    }

    /**
     * 把MediaController绑定到VideoView
     */
    fun bindVideoView(videoView: VideoView) {
        this@MediaController.videoView = videoView
        parent = videoView.parent as ViewGroup
        contentView = makeControllerView().apply {
            setOnTouchListener(onTouchListener)
            post(showProgress)
        }
    }

    private fun makeControllerView() : View {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // TODO: 2020/5/23 orientation inflate port or land
        val contentView = inflate.inflate(
                if (Util.isOrientationPort(context))  R.layout.media_controller_port else R.layout.media_controller_land,
                null)
        initControllView(contentView)
        return contentView
    }

    fun changeOrientation() {
        parent.removeView(contentView)
        contentView = makeControllerView().apply {
            setOnTouchListener(onTouchListener)
            post(showProgress)
        }
    }

    // 锁定状态
    private var lock: Boolean = false
    // 点击依次改变缩放比率, 总共有6种缩放比率
    private var aspectRatioCount = 0
    private fun initControllView(view: View) {
        view.findViewById<TextView>(R.id.videoTitleTv).run {
            text = videoView.videoTitle
            requestFocus()
            isSelected = true
        }
        playPauseBtn = view.findViewById<ImageButton>(R.id.playPauseBtn)
        playPauseBtn.setOnClickListener {
            doPlayPause()
        }
        progressSeekbar = view.findViewById<SeekBar>(R.id.progressBar).apply {
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        currentTimeTv = view.findViewById(R.id.currentTimeTv)
        endTimeTv = view.findViewById(R.id.endTimeTv)
        // 缩放
        view.findViewById<ImageButton>(R.id.scaleBtn).setOnClickListener {
            videoView.aspectRatio = (aspectRatioCount++) % 6
        }
        // 锁屏
        view.findViewById<ImageButton>(R.id.lockBtn).setOnClickListener {
            lock = !lock
            (it as ImageButton).setImageResource(if (lock) R.drawable.ic_lock else R.drawable.ic_lock_open)
            view.findViewById<Group>(R.id.lockControllerGroup).visibility = if (lock) View.GONE else View.VISIBLE
        }
        // 截屏
        view.findViewById<ImageButton>(R.id.scissorsBtn).setOnClickListener {
            videoView.takeShotPic(highShot = true, videoShotListener = object:IRenderView.VideoShotListener {
                override fun getBitmap(bitmap: Bitmap) {
                    // TODO: 2020/7/6
                }
            })
        }
        // 小悬浮窗
        view.findViewById<ImageButton>(R.id.floatWindowBtn).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(context)) {
                    if (!this::floatWindow.isInitialized) {
                        floatWindow = FloatWindow(context,
                                videoView.videoUri,
                                videoView.videoCurrentPosition,
                                videoView.videoDuration
                        )
                    }
                    floatWindow.show()
                } else {
                    requestDrawOverlayPermission.invoke()
                }
            }
        }
    }

    private fun doPlayPause() {
        if (videoView.isPlaying) {
            videoView.pause()
            playPauseBtn.setImageResource(R.drawable.ic_pause)
            // 视频停止后, 进度条停止和控制栏自动消失
            contentView.removeCallbacks(showProgress)
            contentView.removeCallbacks(fadeOut)
        } else {
            videoView.start()
            playPauseBtn.setImageResource(R.drawable.ic_play)
            show()
        }
    }

    fun hide() {
        if (isShowing) {
            Util.hideSystemUI(context)
            Util.hideStatusBar(context)
            parent.removeView(contentView)
            contentView.removeCallbacks(showProgress)
            isShowing = false

            dismissProgressDialog()
            dismissBrightnessDialog()
            dismissVolumeDialog()
        }
    }

    fun show() { show(DefaultTimeout)}

    private fun show(timeout: Long) {
        if (!isShowing) {
            isShowing = true
            parent.addView(contentView, layoutParams)
        }
        Util.showSystemUI(context)
        Util.showStatusBar(context)
        contentView.post(showProgress)
        if (timeout != 0L) {
            contentView.removeCallbacks(fadeOut)
            contentView.postDelayed(fadeOut, timeout)
        }
    }

    private var downX = 0f
    private var downY = 0f
    private var changeVolume = false
    private var changePosition = false
    private var changeBrightness = false
    private var gestureDownPosition = 0L // 触摸屏幕时的视频播放进度
    private var gestureDownBrightness = 0f // 触摸屏幕时的视频亮度
    private var gestureDownVolume = 0 // 触摸屏幕时的声音大小
    private var amplification = 1 // 放大系数
    private val volumeAmplification = amplification * 0.5 // 声音范围0-15, 变化效果太明显, 缩小
    private val brightnessAmplification = amplification * 3 //  亮度范围0-1, 变化效果不明显, 放大
    private val onTouchListener = object : View.OnTouchListener {
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    downY = event.y
                    changeVolume = false
                    changePosition = false
                    changeBrightness = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val parentWidth = contentView.width
                    val parentHeight = contentView.height
                    val deltaX = event.x - downX
                    var deltaY = event.y - downY
                    val absDeltaX = abs(deltaX)
                    val absDeltaY = abs(deltaY)
                    if (!changeVolume && !changePosition && !changeBrightness) {
                        // 水平滑动, 快进或者快退视频
                        if (absDeltaX > absDeltaY && absDeltaX >= MOVE_DETECT_THRESHOLD) {
                            changePosition = true
                            gestureDownPosition = videoView.videoCurrentPosition
                        }
                        // 竖直滑动, 改变亮度或者音量
                        else if (absDeltaX < absDeltaY && absDeltaY >= MOVE_DETECT_THRESHOLD) {
                            // 左边改变亮度
                            if (downX < parentWidth / 2) {
                                changeBrightness = true
                                val lp = Util.getWindow(context).attributes
                                if (lp.screenBrightness < 0) {
                                    try {
                                        gestureDownBrightness = Settings.System.getFloat(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
                                    } catch (e: Settings.SettingNotFoundException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    gestureDownBrightness = lp.screenBrightness
                                }
                            } else {
                                changeVolume = true
                                gestureDownVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                            }
                        }
                    }
                    if (changePosition) {
                        val duration = videoView.videoDuration
                        var seekTimePosition = (gestureDownPosition + deltaX * duration / parentWidth).toLong()
                        if (seekTimePosition > duration) {
                            seekTimePosition = duration
                        }
                        showProgressDialog( deltaX.toInt(), seekTimePosition, duration )
                    }
                    if (changeBrightness) {
                        deltaY = -deltaY
                        val deltaBrightness = brightnessAmplification * deltaY  / parentHeight
                        var afterChangeBrightness = gestureDownBrightness + deltaBrightness
                        if (afterChangeBrightness <= 0) {
                            afterChangeBrightness = 0f
                        } else if (afterChangeBrightness >= 1){
                            afterChangeBrightness = 1f
                        }
                        val params = Util.getWindow(context).attributes
                        params.screenBrightness = afterChangeBrightness
                        Util.getWindow(context).attributes = params
                        showBrightnessDialog(afterChangeBrightness )
                    }
                    if (changeVolume) {
                        deltaY = -deltaY
                        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                        val deltaVolume = maxVolume * deltaY * volumeAmplification / parentHeight
                        var afterChangeVolume = (gestureDownVolume + deltaVolume).toInt()
                        if (afterChangeVolume <= 0) {
                            afterChangeVolume = 0
                        } else {
                            afterChangeVolume = min(afterChangeVolume, maxVolume)
                        }
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, afterChangeVolume, 0)
                        showVolumeDialog(afterChangeVolume * 100/ maxVolume )
                    }

                }
                MotionEvent.ACTION_UP -> {
                    dismissProgressDialog()
                    dismissBrightnessDialog()
                    dismissVolumeDialog()
                    if (changePosition) {
                        // todo seek position
                    }
                }
            }
            return true
        }
    }

    private var volumeDialog: Dialog? = null
    private lateinit var dialogVolumeIv: ImageView
    private lateinit var dialogVolumeTv: TextView
    private lateinit var dialogVolumeProgressBar: ProgressBar
    private fun showVolumeDialog(volumePercent: Int) {
        if (volumeDialog == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_volume, null)
            dialogVolumeIv = view.findViewById(R.id.dialogVolumeIv)
            dialogVolumeTv = view.findViewById(R.id.dialogVolumeTv)
            dialogVolumeProgressBar = view.findViewById(R.id.dialogVolumeProgressBar)

            volumeDialog = createDialogWithView(view)
        }
        if (volumePercent <= 0) {
            dialogVolumeIv.setImageResource(R.drawable.ic_volume_off)
        } else {
            dialogVolumeIv.setImageResource(R.drawable.ic_volume)
        }
        dialogVolumeTv.text = "${volumePercent}%"
        dialogVolumeProgressBar.progress = volumePercent
        if (!volumeDialog!!.isShowing) volumeDialog?.show()
    }

    private fun dismissVolumeDialog() {
        volumeDialog?.dismiss()
    }

    private var brightnessDialog: Dialog? = null
    private lateinit var dialogBrightnessIv: ImageView
    private lateinit var dialogBrightnessTv: TextView
    private lateinit var dialogBrightnessProgressBar: ProgressBar
    private fun showBrightnessDialog(brightnessPercent: Float) {
        if (brightnessDialog == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_brightness, null)
            dialogBrightnessIv = view.findViewById<ImageView>(R.id.dialogBrightnessIv)
            dialogBrightnessTv = view.findViewById<TextView>(R.id.dialogBrightnessTv)
            dialogBrightnessProgressBar = view.findViewById(R.id.dialogBrightnessProgressBar)
            brightnessDialog = createDialogWithView(view)
        }
        if (brightnessDialog?.isShowing != true) {
            brightnessDialog?.show()
        }

        val brightness: Int = (brightnessPercent * 100).toInt()
        dialogBrightnessTv.text = "${brightness}%"
        dialogBrightnessProgressBar.progress = brightness

    }

    private fun dismissBrightnessDialog() {
        brightnessDialog?.dismiss()
    }

    private var progressDialog: Dialog? = null
    private lateinit var progressDialogProgressIv: ImageView
    private lateinit var progressDialogSeekTimeTv: TextView
    private lateinit var progressDialogDurationTv: TextView
    private lateinit var progressDialogProgressBar: ProgressBar
    private fun showProgressDialog(deltaX: Int, seekTimePosition: Long, totalTimeDuration: Long) {
        if (progressDialog == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
            progressDialogProgressIv = view.findViewById(R.id.dialogProgressIv)
            progressDialogDurationTv = view.findViewById(R.id.dialogProgressDurationTv)
            progressDialogSeekTimeTv = view.findViewById(R.id.dialogProgressSeekTimeTv)
            progressDialogProgressBar = view.findViewById(R.id.dialogProgressProgressBar)
            progressDialog = createDialogWithView(view)
        }
        if (progressDialog?.isShowing != true) {
            progressDialog?.show()
        }
        progressDialogSeekTimeTv.text = Util.stringForTime(seekTimePosition)
        progressDialogDurationTv.text = "/" + Util.stringForTime(totalTimeDuration)
        val progress = if (totalTimeDuration <= 0) 0 else (seekTimePosition * 100 / totalTimeDuration).toInt()
        progressSeekbar.progress = progress
        progressDialogProgressBar.progress = progress
        videoView.seekTo(seekTimePosition)
        if (deltaX > 0) {
            progressDialogProgressIv.setBackgroundResource(R.drawable.ic_fast_forward)
        } else {
            progressDialogProgressIv.setBackgroundResource(R.drawable.ic_fast_rewind)
        }
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }


    private fun createDialogWithView(view: View): Dialog {
        val dialog = Dialog(context, R.style.PlayDialog)
        dialog.setContentView(view)
        dialog.window?.run {
            addFlags(Window.FEATURE_ACTION_BAR)
            addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            // setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        return dialog
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            // seekBar.progress会触发该回调
            // 如果不是用户手动调节progress, 则不处理
            if (!fromUser) {
                return
            }
            val duration = videoView.videoDuration
            val newPosition = duration * progress / 100
            logD(TAG, "onProgressChanged(), newPosition=${newPosition}, progressBar=${progress}")
            videoView.seekTo(newPosition)
            currentTimeTv.text = Util.stringForTime(newPosition)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            isDragging = true
            contentView.removeCallbacks(showProgress)
            contentView.removeCallbacks(fadeOut)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            isDragging = false
            show()
        }
    }

    private val showProgress = object :Runnable {
        override fun run() {
            val pos = setProgress()
            if (!isDragging && isShowing && videoView.isPlaying) {
                contentView.postDelayed(this , 1000L - (pos % 1000))
            }
        }

    }

    private val fadeOut = Runnable {
        hide()
    }

    private fun setProgress() : Int{
        val position = videoView.videoCurrentPosition
        val duration = videoView.videoDuration
        // TODO: 2020/6/29 fixme 
        if (duration > 0) {
            progressSeekbar.progress = (position * 100 / duration).toInt()
        }
        currentTimeTv.text = Util.stringForTime(position)
        endTimeTv.text = Util.stringForTime(duration)
        return position.toInt()
    }

    // TODO: 2020/5/23
    // private val layoutChangeListener =
    //         OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
    //             updateFloatingWindowLayout()
    //             if (isShowing) {
    //                 windowManager.updateViewLayout(decor, decorLayoutParams)
    //             }
    //         }


    var requestDrawOverlayPermission: () -> Unit = {}
    val requestDrawOverlayPermissionCallback: () -> Unit = {
        if (!this::floatWindow.isInitialized) {
            floatWindow = FloatWindow(context,
                    videoView.videoUri,
                    videoView.videoCurrentPosition,
            videoView.videoDuration)
        }
        floatWindow.show()
    }

}

