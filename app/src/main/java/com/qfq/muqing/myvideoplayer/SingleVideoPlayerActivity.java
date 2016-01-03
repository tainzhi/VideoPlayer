package com.qfq.muqing.myvideoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


public class SingleVideoPlayerActivity extends Activity implements SurfaceHolder.Callback {

    private final static String TAG = "VideoPlayer/SingleVideoPlayerActivity";

    private Uri mVideoUri;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPlayView;
    private SurfaceHolder mSurfaceHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mVideoUri = intent.getData();

        setContentView(R.layout.activity_single_video_player);
        mPlayView = (SurfaceView)findViewById(R.id.activity_single_video_player_player_view);
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

