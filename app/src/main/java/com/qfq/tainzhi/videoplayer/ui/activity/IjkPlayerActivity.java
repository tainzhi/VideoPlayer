package com.qfq.tainzhi.videoplayer.ui.activity;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.common.PlayerManager;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IIjkPlayerPresenter;
import com.qfq.tainzhi.videoplayer.widget.media.IjkVideoView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
public class IjkPlayerActivity extends AppCompatActivity implements PlayerManager.PlayerStateListener {
    
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
    LinearLayout MLlVideoVolume;
    @BindView(R2.id.video_player_loading_animation)
    ImageView mVideoPlayerLoadingAnimation;
    
    private PlayerManager mPlayerManager;
    private AudioManager mAudioManager;
    private IIjkPlayerPresenter mIIjkPlayerPresenter;
    private GestureDetector mDector;
    private boolean mIsShowPanel;
    private boolean mIsPlay;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_player);
        ButterKnife.bind(this);
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
        NumberFormat nt = NumberFormat.getPercentInstance();
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        float startY = 0, startX;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float temp = (startY - y) / screenHeight;
                if (x < screenWidth / 2) {
                    //如果是左半边屏幕 处理音量变化
                    if (Math.abs(temp) > 0.067f) {
                        onVolumeSlide(temp);
                        startY = y;
                    }
                    return true;
                } else {
                    onBrightnessSlide(temp);
                }
                break;
            case MotionEvent.ACTION_UP:
                mPlayPresenter.dismissVolAlpha();
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
        mIIjkPlayerPresenter.getPlayerInfo(mId);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        mPlayerManager.onDestroy();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mIIjkPlayerPresenter.unRigister();
    }
    
    public void showPanel() {
        // TODO: 2019/6/20
    }
    
    public void hidePanel() {
        // TODO: 2019/6/20
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
                } else {
                
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
    
}
