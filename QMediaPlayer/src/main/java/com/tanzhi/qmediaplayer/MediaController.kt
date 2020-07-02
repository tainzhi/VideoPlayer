package com.tanzhi.qmediaplayer

import android.app.Dialog
import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import android.view.*
import android.widget.*
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

    private val audioManager by lazy {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
    }

    companion object {
        const val DefaultTimeout = 3000

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
        // TODO: 2020/5/23 orientaion inflate port or land
        val contentView = inflate.inflate(R.layout.media_controller_port, null)
        initControllView(contentView)
        return contentView
    }

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
    }

    private fun doPlayPause() {
        if (videoView.isPlaying) {
            videoView.pause()
            playPauseBtn.setImageResource(R.drawable.ic_pause)
        } else {
            videoView.start()
            playPauseBtn.setImageResource(R.drawable.ic_play)
        }
    }

    fun hide() {
        if (isShowing) {
            parent.removeView(contentView)
            contentView.removeCallbacks(showProgress)
            isShowing = false
        }
    }

    fun show() {
        if (!isShowing) {
            isShowing = true
            parent.addView(contentView, layoutParams)
            contentView.post(showProgress)
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
                                    gestureDownBrightness = lp.screenBrightness * 255
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
                        val deltaBrightness = 255 * deltaY * 3 / parentHeight
                        val params = Util.getWindow(context).attributes
                        if ((gestureDownBrightness + deltaBrightness) / 255 >= 1) {
                            params.screenBrightness = 1f
                        } else if((gestureDownBrightness + deltaBrightness) / 255 <= 0) {
                            params.screenBrightness = 0.01f
                        } else {
                            params.screenBrightness =  (gestureDownBrightness + deltaBrightness) / 255
                        }
                        Util.getWindow(context).attributes = params
                        val brightnessPercent = (gestureDownBrightness * 100 / 255 + deltaY * 3 * 100 /parentHeight)
                        showBrightnessDialog(brightnessPercent.toInt())
                        downY = y
                    }
                    if (changeVolume) {
                        deltaY = -deltaY
                        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                        val deltaVolume = maxVolume * deltaY * 3 / parentHeight
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (gestureDownVolume + deltaVolume).toInt(), 0)
                        val volumePercent = gestureDownVolume * 100 / maxVolume + deltaY * 3 * 100 / parentHeight
                        showVolumeDialog(-deltaY, volumePercent)
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
    private fun showVolumeDialog(deltaY: Float, volumePercent: Float) {
        if (volumeDialog == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_volume, null)
            dialogVolumeIv = view.findViewById<ImageView>(R.id.dialogVolumeIv)
            dialogVolumeTv = view.findViewById<TextView>(R.id.dialogVolumeTv)
            dialogVolumeProgressBar = view.findViewById<ProgressBar>(R.id.dialogVolumeProgressBar)

            volumeDialog = createDialogWithView(view)
        }
        if (volumePercent <= 0) {
            dialogVolumeIv.setImageResource(R.drawable.ic_volume_off)
        } else {
            dialogVolumeIv.setImageResource(R.drawable.ic_volume)
        }
        var volume = 0
        if (volumePercent > 100) {
            volume = 100
        } else if (volumePercent < 0) {
            volume = 0
        }
        dialogVolumeTv.text = "${volume}%"
        dialogVolumeProgressBar.progress = volume
        if (!volumeDialog!!.isShowing) volumeDialog?.show()
    }

    private fun dismissVolumeDialog() {
        volumeDialog?.dismiss()
    }

    private var brightnessDialog: Dialog? = null
    private lateinit var dialogBrightnessIv: ImageView
    private lateinit var dialogBrightnessTv: TextView
    private lateinit var dialogBrightnessProgressBar: ProgressBar
    private fun showBrightnessDialog(brightnessPercent: Int) {
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
        var brightness = 0
        if (brightnessPercent > 100) {
            brightness = 100
        } else if (brightnessPercent < 0) {
            brightness = 0
        }
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
        progressSeekbar.progress = if (totalTimeDuration <= 0) 0 else (seekTimePosition * 100 / totalTimeDuration).toInt()
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
            setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        return dialog
    }

    private val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val duration = videoView.videoDuration
            val newPosition = duration * progress / 100
            videoView.seekTo(newPosition)
            currentTimeTv.text = Util.stringForTime(newPosition)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            isDragging = true
            contentView.removeCallbacks(showProgress)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            isDragging = false
            contentView.post(showProgress)
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

    private val fadeOut = Runnable { hide() }

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


}