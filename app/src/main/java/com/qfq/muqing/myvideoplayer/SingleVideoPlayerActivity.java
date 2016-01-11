package com.qfq.muqing.myvideoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class SingleVideoPlayerActivity extends Activity implements SurfaceHolder.Callback {

    private final static String TAG = "VideoPlayer/SingleVideoPlayerActivity";

    private Uri mVideoUri;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPlayView;
    private SurfaceHolder mSurfaceHolder;

    private LinearLayout mControlTitleLayout;
    private TextView mControlTitle;
    private LinearLayout mControllerBarLayout;
    private ImageView mControllerControl;
    private SeekBar mControllerProgress;
    private ImageView mControllerFloatWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mVideoUri = intent.getData();

        setContentView(R.layout.activity_single_video_player);
        mPlayView = (SurfaceView)findViewById(R.id.activity_single_video_player_player_view);
        mControlTitleLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_title_bar);
        mControlTitle = (TextView)findViewById(R.id.activity_single_video_player_player_video_title);
        mControllerBarLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_title_bar);
        mControllerControl = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_control);
        mControllerProgress = (SeekBar)findViewById(R.id.activity_single_video_player_player_controller_progress);
        mControllerFloatWindow = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_floatwindow);

        mControllerControl.setOnClickListener(mContollerControlClickListener);
        mControllerFloatWindow.setOnClickListener(mControllerFloatWindowClickListener);

        mSurfaceHolder = mPlayView.getHolder();
        mSurfaceHolder.addCallback(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        try {
            mMediaPlayer.setDataSource(this, mVideoUri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, mVideoUri.toString() + " IOException!!!");
        }
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
        mMediaPlayer.release();
    }

    private View.OnClickListener mContollerControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "control");
        }
    };

    private View.OnClickListener mControllerFloatWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "float window");
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onPrepared()");
            mMediaPlayer.start();
            Log.v(TAG, "MediaPlayer.start()");
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onCompletion()");
            finish();
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mMediaPlayer.setDisplay(mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
}

