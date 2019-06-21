package com.qfq.tainzhi.videoplayer.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.common.PlayerManager;
import com.qfq.tainzhi.videoplayer.mvp.presenter.IjkPlayerPresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IIjkPlayerPresenter;
import com.qfq.tainzhi.videoplayer.util.StringUtil;
import com.qfq.tainzhi.videoplayer.util.WindowUtil;
import com.qfq.tainzhi.videoplayer.widget.media.IjkVideoView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
public class IjkPlayerActivity extends Activity implements PlayerManager.PlayerStateListener {
    
    @BindView(R2.id.video_view)
    IjkVideoView mVideoView;
    @BindView(R2.id.video_player_top_panel_back)
    ImageView mVideoPlayerTopPanelBack;
    @BindView(R2.id.video_player_top_panel_video_title)
    TextView mVideoPlayerTopPanelVideoTitle;
    @BindView(R2.id.video_player_top_panel_system_time)
    TextView mVideoPlayerTopPanelSystemTime;
    @BindView(R2.id.video_player_top_panel_battery)
    ImageView mVideoPlayerTopPanelBattery;
    @BindView(R2.id.l_video_player_top_panel)
    RelativeLayout mVideoPlayerTopPanel;
    @BindView(R2.id.video_player_bottom_panel_play)
    ImageView mVideoPlayerBottomPanelPlay;
    @BindView(R2.id.activity_default_video_player_player_controller_progress)
    SeekBar mActivityDefaultVideoPlayerPlayerControllerProgress;
    @BindView(R2.id.video_player_bottom_panel_float_window)
    ImageView mVideoPlayerBottomPanelFloatWindow;
    @BindView(R2.id.video_player_bottom_panel_follow)
    ImageView mVideoPlayerBottomPanelFollow;
    @BindView(R2.id.l_video_player_bottom_panel)
    LinearLayout mVideoPlayerBottomPanel;
    @BindView(R2.id.iv_video_volume)
    ImageView mIvVideoVolume;
    @BindView(R2.id.tv_video_volume)
    TextView mTvVideoVolume;
    @BindView(R2.id.ll_video_volume)
    LinearLayout mLlVideoVolume;
    @BindView(R2.id.video_player_loading_animation)
    ImageView mVideoPlayerLoadingAnimation;
    
    private PlayerManager mPlayerManager;
    private AudioManager mAudioManager;
    private IIjkPlayerPresenter mIIjkPlayerPresenter;
    private GestureDetector mDector;
    private MyBatteryReceiver mBatteryReceiver;
    private boolean mIsShowPanel;
    private boolean mIsPlay;
    private Uri mVideoUri;
    private String mVideoTitle;
    private long mVideoDuration;
    private long mVideoProgress;
    private int mScreenWidth, mScreenHeight;
    private int mTopPanelHeight;
    private int mBottomPanelHeight;
    private int mMaxVolume;
    private float mBrightness = -1f;
    private float startY = 0, startX;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);
        ButterKnife.bind(this);
        
        mIIjkPlayerPresenter = new IjkPlayerPresenter(this);
        mIIjkPlayerPresenter.getSystemTime();
        
        mBatteryReceiver = new MyBatteryReceiver();
        registerReceiver(mBatteryReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        
        Intent intent = getIntent();
        mVideoUri = intent.getData();
        Bundle bundle = intent.getExtras();
        mVideoTitle = bundle.getString("title");
        mVideoDuration = bundle.getLong("duration", 0);
        mVideoProgress = bundle.getInt("progress", 0);
        
        int[] screenSize = WindowUtil.getScreenSize(this);
        mScreenWidth = screenSize[0];
        mScreenHeight = screenSize[1];
        mVideoPlayerTopPanel.measure(0, 0);
        mVideoPlayerBottomPanel.measure(0, 0);
        mTopPanelHeight = mVideoPlayerTopPanel.getMeasuredHeight();
        mBottomPanelHeight = mVideoPlayerBottomPanel.getMeasuredHeight();
        mVideoPlayerTopPanel.setTranslationY(0);
        mVideoPlayerBottomPanel.setTranslationY(0);
        
        mVideoPlayerTopPanelVideoTitle.setText(mVideoTitle);
        
        initPlayer();
        initData();
    }
    
    private void initPlayer() {
        mPlayerManager = new PlayerManager(this);
        mPlayerManager.setFullScreenOnly(true);
        mPlayerManager.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        mPlayerManager.playInFullScreen(true);
        mPlayerManager.setPlayerStateListener(this);
    }
    
    private void initData() {
        mDector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return super.onSingleTapUp(e);
                    }
                    
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mIsShowPanel) {
                            hidePanel();
                        } else {
                            showPanel();
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDector.onTouchEvent(event);
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float temp = (startY - y) / mScreenHeight;
                Logger.d("move percent: %.2f", temp);
                if (x < mScreenWidth / 2) {
                    //如果是左半边屏幕 处理音量变化
                    if (Math.abs(temp) > 0.067f) {
                        onVolumeSlide(temp);
                    }
                    return true;
                } else {
                    onBrightnessSlide(temp);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIIjkPlayerPresenter.dismissVolumePanel();
                break;
            
        }
        return super.onTouchEvent(event);
    }
    
    @Override
    public void onComplete() {
    }
    
    @Override
    public void onError() {
    }
    
    @Override
    public void onLoading() {
    }
    
    @Override
    public void onPlay() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mIIjkPlayerPresenter.autoHidePanel();
        mIIjkPlayerPresenter.getSystemTime();
        playVideo(mVideoUri.toString());
        // REFACTOR: 2019/6/21 如果是传过来的都是房间号, 还需要获取房间直播源uri
        // mIIjkPlayerPresenter.getPlayerInfo();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        mPlayerManager.onDestroy();
    }
    
    @Override
    public void onDestroy() {
        unregisterReceiver(mBatteryReceiver);
        super.onDestroy();
        // mIIjkPlayerPresenter.unRigister();
    }
    
    public void showPanel() {
        mVideoPlayerTopPanel.setTranslationY(0);
        mVideoPlayerBottomPanel.setTranslationY(0);
        mIsShowPanel = true;
        mIIjkPlayerPresenter.autoHidePanel();
    }
    
    public void hidePanel() {
        mVideoPlayerTopPanel.setTranslationY(-mTopPanelHeight);
        mVideoPlayerBottomPanel.setTranslationY(mBottomPanelHeight);
        mIsShowPanel = false;
    }
    
    public void playVideo(String url) {
        mPlayerManager.play(url);
    }
    
    @OnClick({R.id.video_player_top_panel_back,
            R.id.video_player_bottom_panel_play,
            R.id.video_player_bottom_panel_follow,
            R.id.video_player_bottom_panel_float_window})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_player_top_panel_back:
                finish();
                break;
            case R.id.video_player_bottom_panel_play:
                if (mIsPlay) {
                    mIsPlay = false;
                    mVideoPlayerBottomPanelPlay.setImageResource(R.drawable.ic_video_player_pause);
                    // REFACTOR: 2019/6/21 Rxjava2
                    //  autoHidePanel需要再处理1个mIsPanel, 判断是否隐藏
                    mPlayerManager.pause();
                } else {
                    mIsPlay = true;
                    mVideoPlayerBottomPanelPlay.setImageResource(R.drawable.ic_video_player_play);
                    mPlayerManager.start();
                }
                break;
            case R.id.video_player_bottom_panel_follow:
                // TODO: 2019/6/20
                break;
            case R.id.video_player_bottom_panel_float_window:
                // TODO: 2019/6/20
                break;
        }
    }
    
    public void updateSystemTime() {
        mVideoPlayerTopPanelSystemTime.setText(StringUtil.formatSystemTime());
    }
    
    public void onVolumeSlide(float percent) {
        mLlVideoVolume.setVisibility(View.VISIBLE);
        mIvVideoVolume.setImageResource(R.drawable.ic_video_player_volume);
        int currentVolume =
                mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // 除以5, 因为音量速度改变太快, 降速
        currentVolume = (int) (percent * mMaxVolume / 5)  + currentVolume;
        if (currentVolume > mMaxVolume) {
            currentVolume = mMaxVolume;
        } else if (currentVolume < 0) {
            currentVolume = 0;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                currentVolume, 0);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        double vol = currentVolume * 1.0 / mMaxVolume;
        mTvVideoVolume.setText(nt.format(vol));
    }
    
    private void onBrightnessSlide(float percent) {
        Logger.d("%.2f", percent);
        mLlVideoVolume.setVisibility(View.VISIBLE);
        mIvVideoVolume.setImageResource(R.drawable.ic_video_player_brightness);
        mBrightness = getWindow().getAttributes().screenBrightness;
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        // 除以8, 因为亮度速度改变太快, 降速
        float sb = mBrightness + percent / 8;
        if (sb > 1.0f) {
            sb = 1.0f;
        } else if (sb < 0.01f) {
            sb = 0.01f;
        }
        lpa.screenBrightness = sb;
        getWindow().setAttributes(lpa);
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        mTvVideoVolume.setText(nt.format(sb));
    }
    
    public void hideVolumePanel() {
        mLlVideoVolume.setVisibility(View.GONE);
    }
    
    
    private class MyBatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 100);
            // REFACTOR: 2019/6/21 待重构 添加更多电池电量信息显示
            if (level > 90) {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0);
            } else if (level > 10) {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0);
            } else {
                mVideoPlayerTopPanelBattery.setImageResource(R.drawable.ic_video_player_battery_0);
            }
        }
    }
}
