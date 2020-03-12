package com.qfq.tainzhi.videoplayer.ui.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.R2
import com.qfq.tainzhi.videoplayer.common.PlayerManager
import com.qfq.tainzhi.videoplayer.common.PlayerManager.PlayerStateListener
import com.qfq.tainzhi.videoplayer.media.IjkVideoView
import com.qfq.tainzhi.videoplayer.mvp.presenter.IjkPlayerPresenter
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IIjkPlayerPresenter
import com.qfq.tainzhi.videoplayer.util.StringUtil
import com.qfq.tainzhi.videoplayer.util.WindowUtil
import java.text.NumberFormat

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
class IjkPlayerActivity : Activity(), PlayerStateListener {
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_view)
    var mVideoView: IjkVideoView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_top_panel_back)
    var mVideoPlayerTopPanelBack: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_top_panel_video_title)
    var mVideoPlayerTopPanelVideoTitle: TextView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_top_panel_system_time)
    var mVideoPlayerTopPanelSystemTime: TextView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_top_panel_battery)
    var mVideoPlayerTopPanelBattery: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.l_video_player_top_panel)
    var mVideoPlayerTopPanel: RelativeLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_bottom_panel_play)
    var mVideoPlayerBottomPanelPlay: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.activity_default_video_player_player_controller_progress)
    var mActivityDefaultVideoPlayerPlayerControllerProgress: SeekBar? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_bottom_panel_float_window)
    var mVideoPlayerBottomPanelFloatWindow: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_bottom_panel_follow)
    var mVideoPlayerBottomPanelFollow: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.l_video_player_bottom_panel)
    var mVideoPlayerBottomPanel: LinearLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.iv_video_volume)
    var mIvVideoVolume: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.tv_video_volume)
    var mTvVideoVolume: TextView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.ll_video_volume)
    var mLlVideoVolume: LinearLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.video_player_loading_animation)
    var mVideoPlayerLoadingAnimation: ImageView? = null
    private var mPlayerManager: PlayerManager? = null
    private var mAudioManager: AudioManager? = null
    private var mIIjkPlayerPresenter: IIjkPlayerPresenter? = null
    private var mDector: GestureDetector? = null
    private var mBatteryReceiver: MyBatteryReceiver? = null
    private var mIsShowPanel = false
    private var mIsPlay = false
    private var mVideoUri: Uri? = null
    private var mVideoTitle: String? = null
    private var mVideoDuration: Long = 0
    private var mVideoProgress: Long = 0
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private var mTopPanelHeight = 0
    private var mBottomPanelHeight = 0
    private var mMaxVolume = 0
    private var mBrightness = -1f
    private var startY = 0f
    private val startX = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ijk_player)
        ButterKnife.bind(this)
        mIIjkPlayerPresenter = IjkPlayerPresenter(this)
        mIIjkPlayerPresenter.getSystemTime()
        mBatteryReceiver = MyBatteryReceiver()
        registerReceiver(mBatteryReceiver,
                         IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val intent = intent
        mVideoUri = intent.data
        val bundle = intent.extras
        mVideoTitle = bundle.getString("title")
        mVideoDuration = bundle.getLong("duration", 0)
        mVideoProgress = bundle.getInt("progress", 0).toLong()
        val screenSize = WindowUtil.getScreenSize(this)
        mScreenWidth = screenSize[0]
        mScreenHeight = screenSize[1]
        mVideoPlayerTopPanel.measure(0, 0)
        mVideoPlayerBottomPanel.measure(0, 0)
        mTopPanelHeight = mVideoPlayerTopPanel.getMeasuredHeight()
        mBottomPanelHeight = mVideoPlayerBottomPanel.getMeasuredHeight()
        mVideoPlayerTopPanel.setTranslationY(0f)
        mVideoPlayerBottomPanel.setTranslationY(0f)
        mVideoPlayerTopPanelVideoTitle.setText(mVideoTitle)
        initPlayer()
        initData()
    }
    
    private fun initPlayer() {
        mPlayerManager = PlayerManager(this)
        mPlayerManager.setFullScreenOnly(true)
        mPlayerManager.setScaleType(PlayerManager.Companion.SCALETYPE_FILLPARENT)
        mPlayerManager.playInFullScreen(true)
        mPlayerManager.setPlayerStateListener(this)
    }
    
    private fun initData() {
        mDector = GestureDetector(this,
                                  object : SimpleOnGestureListener() {
                                      override fun onSingleTapUp(e: MotionEvent?): Boolean {
                                          return super.onSingleTapUp(e)
                                      }
            
                                      override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                                          if (mIsShowPanel) {
                                              hidePanel()
                                          } else {
                                              showPanel()
                                          }
                                          return super.onSingleTapConfirmed(e)
                                      }
                                  })
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDector.onTouchEvent(event)
        //获取格式化对象
        val nt = NumberFormat.getPercentInstance()
        //设置百分数精确度2即保留两位小数
        nt.minimumFractionDigits = 0
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> startY = event.getY()
            MotionEvent.ACTION_MOVE -> {
                val x = event.getX()
                val y = event.getY()
                val temp = (startY - y) / mScreenHeight
                Logger.d("move percent: %.2f", temp)
                if (x < mScreenWidth / 2) {
                    //如果是左半边屏幕 处理音量变化
                    if (Math.abs(temp) > 0.067f) {
                        onVolumeSlide(temp)
                    }
                    return true
                } else {
                    onBrightnessSlide(temp)
                }
            }
            MotionEvent.ACTION_UP -> mIIjkPlayerPresenter.dismissVolumePanel()
        }
        return super.onTouchEvent(event)
    }
    
    override fun onComplete() {}
    override fun onError() {}
    override fun onLoading() {}
    override fun onPlay() {
        mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }
    
    public override fun onStart() {
        super.onStart()
        mIIjkPlayerPresenter.autoHidePanel()
        mIIjkPlayerPresenter.getSystemTime()
        playVideo(mVideoUri.toString())
        // REFACTOR: 2019/6/21 如果是传过来的都是房间号, 还需要获取房间直播源uri
        // mIIjkPlayerPresenter.getPlayerInfo();
    }
    
    public override fun onStop() {
        super.onStop()
        mPlayerManager.onDestroy()
    }
    
    public override fun onDestroy() {
        unregisterReceiver(mBatteryReceiver)
        super.onDestroy()
        // mIIjkPlayerPresenter.unRigister();
    }
    
    fun showPanel() {
        mVideoPlayerTopPanel.setTranslationY(0f)
        mVideoPlayerBottomPanel.setTranslationY(0f)
        mIsShowPanel = true
        mIIjkPlayerPresenter.autoHidePanel()
    }
    
    fun hidePanel() {
        mVideoPlayerTopPanel.setTranslationY(-mTopPanelHeight.toFloat())
        mVideoPlayerBottomPanel.setTranslationY(mBottomPanelHeight.toFloat())
        mIsShowPanel = false
    }
    
    fun playVideo(url: String?) {
        mPlayerManager.play(url)
    }
    
    @OnClick(R.id.video_player_top_panel_back, R.id.video_player_bottom_panel_play, R.id.video_player_bottom_panel_follow, R.id.video_player_bottom_panel_float_window)
    fun onClick(view: View?) {
        when (view.getId()) {
            R.id.video_player_top_panel_back -> finish()
            R.id.video_player_bottom_panel_play -> if (mIsPlay) {
                mIsPlay = false
                mVideoPlayerBottomPanelPlay.setImageResource(R.drawable.ic_video_player_pause)
                // REFACTOR: 2019/6/21 Rxjava2
                //  autoHidePanel需要再处理1个mIsPanel, 判断是否隐藏
                mPlayerManager.pause()
            } else {
                mIsPlay = true
                mVideoPlayerBottomPanelPlay.setImageResource(R.drawable.ic_video_player_play)
                mPlayerManager.start()
            }
            R.id.video_player_bottom_panel_follow -> {
            }
            R.id.video_player_bottom_panel_float_window -> {
            }
        }
    }
    
    fun updateSystemTime() {
        mVideoPlayerTopPanelSystemTime.setText(StringUtil.formatSystemTime())
    }
    
    fun onVolumeSlide(percent: Float) {
        mLlVideoVolume.setVisibility(View.VISIBLE)
        mIvVideoVolume.setImageResource(R.drawable.ic_video_player_volume)
        var currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        // 除以5, 因为音量速度改变太快, 降速
        currentVolume = (percent * mMaxVolume / 5) as Int + currentVolume
        if (currentVolume > mMaxVolume) {
            currentVolume = mMaxVolume
        } else if (currentVolume < 0) {
            currentVolume = 0
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                                      currentVolume, 0)
        val nt = NumberFormat.getPercentInstance()
        //设置百分数精确度2即保留两位小数
        nt.minimumFractionDigits = 0
        val vol = currentVolume * 1.0 / mMaxVolume
        mTvVideoVolume.setText(nt.format(vol))
    }
    
    private fun onBrightnessSlide(percent: Float) {
        Logger.d("%.2f", percent)
        mLlVideoVolume.setVisibility(View.VISIBLE)
        mIvVideoVolume.setImageResource(R.drawable.ic_video_player_brightness)
        mBrightness = window.attributes.screenBrightness
        val lpa = window.attributes
        // 除以8, 因为亮度速度改变太快, 降速
        var sb = mBrightness + percent / 8
        if (sb > 1.0f) {
            sb = 1.0f
        } else if (sb < 0.01f) {
            sb = 0.01f
        }
        lpa.screenBrightness = sb
        window.attributes = lpa
        val nt = NumberFormat.getPercentInstance()
        //设置百分数精确度2即保留两位小数
        nt.minimumFractionDigits = 0
        mTvVideoVolume.setText(nt.format(sb.toDouble()))
    }
    
    fun hideVolumePanel() {
        mLlVideoVolume.setVisibility(View.GONE)
    }
    
    private inner class MyBatteryReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level = intent.getIntExtra("level", 100)
            // REFACTOR: 2019/6/21 待重构 添加更多电池电量信息显示
            if (level > 90) {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0)
            } else if (level > 10) {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0)
            } else {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0)
            }
        }
    }
}