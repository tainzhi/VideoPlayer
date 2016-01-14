package com.qfq.muqing.myvideoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private String mVideoTitle;
    private long mVideoDuration;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPlayView;
    private SurfaceHolder mSurfaceHolder;

    private LinearLayout mControllerTitleLayout;
    private TextView mControllerTitle;
    private LinearLayout mControllerBarLayout;
    private ImageView mControllerControl;
    private SeekBar mControllerProgress;
    private ImageView mControllerFloatWindow;

    private boolean mVideoPlayOrPause = false; // play state is true, stop state is false;
    private boolean mIsTouchOnSeekBar = false;

    private final static int CONTROLLER_CONTROL = 0;
    private final static int CONTROLLER_SEEK_TO = 1;

    private UpdateSeekBarThread mUpdateSeekBarThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mVideoUri = intent.getData();
        Bundle bundle = intent.getExtras();
        mVideoTitle = bundle.getString("title");
        mVideoDuration = bundle.getLong("duration");


        setContentView(R.layout.activity_single_video_player);
        mPlayView = (SurfaceView)findViewById(R.id.activity_single_video_player_player_view);
        mControllerTitleLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_title_bar);
        mControllerTitle = (TextView)findViewById(R.id.activity_single_video_player_player_video_title);
        mControllerBarLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_title_bar);
        mControllerControl = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_control);
        mControllerProgress = (SeekBar)findViewById(R.id.activity_single_video_player_player_controller_progress);
        mControllerFloatWindow = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_floatwindow);

        mControllerTitle.setText(mVideoTitle);
        mControllerControl.setOnClickListener(mContollerControlClickListener);
        mControllerFloatWindow.setOnClickListener(mControllerFloatWindowClickListener);
        mControllerProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

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
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    final private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case CONTROLLER_CONTROL:
                    // pause
                    if (mVideoPlayOrPause) {
                        mVideoPlayOrPause = false;
                        mMediaPlayer.pause();
                        mControllerControl.setImageResource(R.drawable.activity_single_video_player_control_stop);
                    } else {
                        // play
                        mVideoPlayOrPause = true;
                        mMediaPlayer.start();
                        mControllerControl.setImageResource(R.drawable.activity_single_video_player_control_play);
                    }
                    break;
                case CONTROLLER_SEEK_TO:
                    int videoProgress = msg.arg1;
                    mMediaPlayer.seekTo(videoProgress);
                default:
                    break;
            }
        }
    };

    private View.OnClickListener mContollerControlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick() ControllerClick");
            Message msg = mHandler.obtainMessage();
            msg.what = CONTROLLER_CONTROL;
            mHandler.sendMessage(msg);
        }
    };

    private View.OnClickListener mControllerFloatWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "float window");
            //TODO: implement float window
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onPrepared()");
            mMediaPlayer.start();
            mVideoPlayOrPause = true;
            mUpdateSeekBarThread = new UpdateSeekBarThread("UpdateSeekBarThread");
            mUpdateSeekBarThread.start();
            Log.v(TAG, "MediaPlayer.start()");
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onCompletion()");
            mUpdateSeekBarThread.stop_thread();
            finish();
        }
    };

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b) {
                int videoProgress = (int) (i * 1.0 / 100 * mVideoDuration);
                Message msg = new Message();
                msg.what = CONTROLLER_SEEK_TO;
                msg.arg1 = videoProgress;
                mHandler.sendMessage(msg);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mIsTouchOnSeekBar = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mIsTouchOnSeekBar = false;
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

    private class UpdateSeekBarThread extends Thread {

        volatile boolean isRuuning = true;
        protected UpdateSeekBarThread(String threadName) {
            this.setName(threadName);
            isRuuning = true;
        }

        @Override
        public void run() {
            super.run();
            while (mMediaPlayer != null && isRuuning) {
                if (!mIsTouchOnSeekBar && mMediaPlayer.isPlaying()) {
                    int videoProgress = mMediaPlayer.getCurrentPosition();
                    mControllerProgress.setProgress((int) (videoProgress * 1.0 / mVideoDuration * 100));
                }
                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    //FIXME: exception
                }
            }
        }

        public void stop_thread() {
            isRuuning = false;
        }
    }
}

