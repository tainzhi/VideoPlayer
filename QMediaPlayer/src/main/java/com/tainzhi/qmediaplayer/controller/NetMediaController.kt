package com.tainzhi.qmediaplayer.controller

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.tainzhi.android.danmu.advancedanmu.DanMuHelper
import com.tainzhi.android.danmu.advancedanmu.model.DanmuEntity
import com.tainzhi.android.danmu.advancedanmu.view.DanmuContainerView
import com.tainzhi.qmediaplayer.R
import com.tainzhi.qmediaplayer.Util
import com.tainzhi.qmediaplayer.VideoView
import com.tainzhi.qmediaplayer.floatwindow.FloatWindow
import com.tainzhi.qmediaplayer.render.IRenderView
import java.lang.Integer.min
import kotlin.math.abs
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/22 17:03
 * @description: 播放控制器, 添加控制按钮, 控制[VideoView]播放
 * 网络视频播放器, 默认横屏, 没有水平进度条, 有旋转刷新进度圆环, 可以展示弹幕, 选择线路和视频分辨率
 * 默认显示控制栏
 **/

class NetMediaController(val context: Context) : IController(context) {

    private lateinit var parent: ViewGroup
    private lateinit var videoView: VideoView
    private lateinit var contentView: View
    private val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    private lateinit var playPauseBtn: ImageButton
    // 悬浮窗播放
    private lateinit var floatWindow: FloatWindow
    private lateinit var loadingProgressBar: ProgressBar

    companion object {
        const val DefaultTimeout = 3000L

        // 滑动灵敏度
        const val MOVE_DETECT_THRESHOLD = 80
    }

    /**
     * 把MediaController绑定到VideoView
     */
    override fun bindVideoView(viewGroup: ViewGroup) {
        this@NetMediaController.videoView = viewGroup as VideoView
        parent = viewGroup.parent as ViewGroup
        contentView = makeControllerView().apply {
            setOnTouchListener(onTouchListener)
        }
        // 默认显示控制栏
        isShowing = true
        parent.addView(contentView, layoutParams)
    }


    private fun makeControllerView() : View {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val contentView = inflate.inflate(R.layout.media_controller_net_land,null)
        initControllView(contentView)
        return contentView
    }

    // 网络视频默认横屏, 不需要改变orientation
    override fun changeOrientation() {
    }

    // 锁定状态
    private var lock: Boolean = false
    // 点击依次改变缩放比率, 总共有6种缩放比率
    private var aspectRatioCount = 0
    // 弹幕是否打开
    private var isDanmuOn = false
    private fun initControllView(view: View) {
        view.findViewById<TextView>(R.id.videoTitleTv).run {
            text = videoView.videoTitle
            requestFocus()
            isSelected = true
        }
        view.findViewById<ImageButton>(R.id.backIv).setOnClickListener { mediaControllerCloseCallback.invoke() }
        playPauseBtn = view.findViewById<ImageButton>(R.id.playPauseBtn)
        playPauseBtn.setOnClickListener {
            doPlayPause()
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
                                videoView.screenOrientation,
                                backToFullScreenCallback = backToFullScreenCallback
                        )
                    }
                    floatWindow.show()
                    mediaControllerFloatWindowCallback.invoke()
                } else {
                    requestDrawOverlayPermission.invoke()
                }
            }
        }
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)
        view.findViewById<ImageButton>(R.id.refreshButton).setOnClickListener {
            showLoading()
            videoView.resetDataSource()
        }
        view.findViewById<ImageButton>(R.id.danmuBtn).setOnClickListener {
            isDanmuOn = !isDanmuOn
            (it as ImageButton).setImageResource(if (isDanmuOn) R.drawable.ic_danmu_on else R.drawable.ic_danmu_off)
            if (isDanmuOn) {
                parent.addView(danmuContainerView, danmuLayoutParams)
                danmuHelper.addDanmu(
                    DanmuEntity(
                        arrayOf(
                            "https://q.qlogo.cn/qqapp/100229475/E573B01150734A02F25D8E9C76AFD138/100",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602513751308&di=4750ba99977f4478144d0790b6857069&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F00%2F73%2F65%2F0656df6b43bacf5.jpg",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481010486&di=1eea4306e070ce336e0c28f82480613a&imgtype=0&src=http%3A%2F%2Fp.geitu.net%2F409%2F5P-jvh.jpg%3Fx-oss-process%3Dimage%2Fresize%2Cw_256%2Climit_1",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481030402&di=a510961dbd68fe447679c54c29e677b2&imgtype=0&src=http%3A%2F%2Favatar.gxnews.com.cn%2Favatar%2F000%2F08%2F33%2F51_avatar_big.jpg",
                            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=781934719,1232780622&fm=15&gp=0.jpg",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602481030258&di=b485992c8565f1123baa9e98889a867e&imgtype=0&src=http%3A%2F%2Fbbsimg.0411cxd.com%2Fforum%2F201105%2F03%2F203852cp0q2mzlq92mps43.png"
                        ).random(),
                        arrayOf("张三", "李四", "王五", "老板boss", "至尊大使", "尊贵皇希特").random(),
                        "0719",
                        Random.nextInt(30),
                        1,
                        arrayOf(
                            "欲穷千里目, 更上一层楼~~~",
                            "666666666666666666666666666666666666666666666666666666666666666666666666666666666666",
                            "sb fgsb fgsb fgsb fgsb fgsb fgsb",
                            "nbbbbbbbbbbbbility",
                            "fgsbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                            "fgnbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                            "操作犀利, 姿势帅气, 震惊世界",
                            "我上单, 带你赢, 小菜鸡, 搞快点",
                            "天王盖地虎, 小鸡炖蘑菇"
                        ).random()
                    )
                )
            } else {
                danmuHelper.hideAllDanmu()
                parent.removeView(danmuContainerView)
            }
        }
    }

    private val danmuContainerView = DanmuContainerView(context)
    private val danmuLayoutParams: ViewGroup.LayoutParams =
        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 360)
    private val danmuHelper = DanMuHelper(context, danmuContainerView)

    private fun doPlayPause() {
        if (videoView.isPlaying) {
            videoView.pausePlay()
            playPauseBtn.setImageResource(R.drawable.ic_pause)
            // 视频停止后, 进度条停止和控制栏自动消失
            contentView.removeCallbacks(fadeOut)
        } else {
            videoView.startPlay()
            playPauseBtn.setImageResource(R.drawable.ic_play)
            show()
        }
    }

    override fun hide() {
        if (isShowing) {
            Util.hideSystemUI(context)
            Util.hideStatusBar(context)
            parent.removeView(contentView)
            isShowing = false

            dismissBrightnessDialog()
            dismissVolumeDialog()
        }
    }

    override fun show() {
        show(DefaultTimeout)
    }

    private fun show(timeout: Long) {
        if (!isShowing) {
            isShowing = true
            parent.addView(contentView, layoutParams)
        }
        Util.showSystemUI(context)
        Util.showStatusBar(context)
        if (timeout != 0L) {
            contentView.removeCallbacks(fadeOut)
            contentView.postDelayed(fadeOut, timeout)
        }
    }

    private var downX = 0f
    private var downY = 0f
    private var changeVolume = false
    private var changeBrightness = false
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
                    changeBrightness = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val parentWidth = contentView.width
                    val parentHeight = contentView.height
                    val deltaX = event.x - downX
                    var deltaY = event.y - downY
                    val absDeltaX = abs(deltaX)
                    val absDeltaY = abs(deltaY)
                    if (!changeVolume && !changeBrightness) {
                        // 竖直滑动, 改变亮度或者音量
                        if (absDeltaX < absDeltaY && absDeltaY >= MOVE_DETECT_THRESHOLD) {
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
                    dismissBrightnessDialog()
                    dismissVolumeDialog()
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


    private val fadeOut = Runnable {
        hide()
    }


    // TODO: 2020/5/23
    // private val layoutChangeListener =
    //         OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
    //             updateFloatingWindowLayout()
    //             if (isShowing) {
    //                 windowManager.updateViewLayout(decor, decorLayoutParams)
    //             }
    //         }

    override val requestDrawOverlayPermissionCallback: () -> Unit
        get() = {
            if (!this::floatWindow.isInitialized) {
                floatWindow = FloatWindow(context,
                        videoView.videoUri,
                        videoView.videoCurrentPosition,
                        videoView.screenOrientation,
                        backToFullScreenCallback = backToFullScreenCallback
                )
            }
            floatWindow.show()
            mediaControllerFloatWindowCallback.invoke()
        }

    override fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    override fun dismissLoading() {
        loadingProgressBar.visibility = View.GONE
    }
}

