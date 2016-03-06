package com.qfq.muqing.myvideoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class SingleVideoPlayerActivity extends Activity implements SurfaceHolder.Callback {

    private final static String TAG = "VideoPlayer/SingleVideoPlayerActivity";

    private Context mContext;
    private Uri mVideoUri;
    private String mVideoTitle;
    private long mVideoDuration;
    private int mVideoProgress;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPlayView;
    private SurfaceHolder mSurfaceHolder;

    private View mParentView;
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
    private final static int HIDE_CONTROLLER_BAR = 2;
    private final static int SHOW_CONTROLLER_BAR = 3;

    private final static int HIDE_CONTROLLER_BAR_DELAY = 5000;

    private UpdateSeekBarThread mUpdateSeekBarThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mContext = this;
        Intent intent = getIntent();
        mVideoUri = intent.getData();
        Bundle bundle = intent.getExtras();
        mVideoTitle = bundle.getString("title");
        mVideoDuration = bundle.getLong("duration");
        mVideoProgress = bundle.getInt("progress", 0);


        setContentView(R.layout.activity_single_video_player);
        mParentView = (View)findViewById(R.id.activity_single_video_parrent);
        mPlayView = (SurfaceView)findViewById(R.id.activity_single_video_player_player_view);
        mControllerTitleLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_title_bar);
        mControllerTitle = (TextView)findViewById(R.id.activity_single_video_player_player_video_title);
        mControllerBarLayout = (LinearLayout)findViewById(R.id.activity_single_video_player_player_controller_bar);
        mControllerControl = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_control);
        mControllerProgress = (SeekBar)findViewById(R.id.activity_single_video_player_player_controller_progress);
        mControllerFloatWindow = (ImageView)findViewById(R.id.activity_single_video_player_player_controller_floatwindow);

        //Hide TitleBar and ControllerBar 5s later
        mHandler.sendEmptyMessageDelayed(HIDE_CONTROLLER_BAR, HIDE_CONTROLLER_BAR_DELAY);


        mPlayView.setOnTouchListener(mOnTouchListener);
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
                    break;
                case HIDE_CONTROLLER_BAR:
                    Log.v(TAG, "to hide controller bar");
                    mControllerTitleLayout.setVisibility(View.INVISIBLE);
                    mControllerBarLayout.setVisibility(View.INVISIBLE);
                    break;
                case SHOW_CONTROLLER_BAR:
                    Log.v(TAG, "to show controller bar");
                    mControllerTitleLayout.setVisibility(View.VISIBLE);
                    mControllerBarLayout.setVisibility(View.VISIBLE);
                    break;
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
            Log.v(TAG, "hide current window, to open float window");
            // SingleVideoPlayActivity finish, then back to home screen
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            SingleVideoPlayerActivity.this.startActivity(homeIntent);
            finish();

            FloatWindow.getInstance(getApplicationContext(),
                    mVideoUri, mVideoTitle, mMediaPlayer.getCurrentPosition(), mVideoDuration).showFloatWindow();
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.v(TAG, "onTouch");
            int state = event.getAction();
            switch (state) {
                case MotionEvent.ACTION_UP:
                    mHandler.removeMessages(SHOW_CONTROLLER_BAR);
                    mHandler.sendEmptyMessage(SHOW_CONTROLLER_BAR);

                    mHandler.removeMessages(HIDE_CONTROLLER_BAR);
                    mHandler.sendEmptyMessageDelayed(HIDE_CONTROLLER_BAR, HIDE_CONTROLLER_BAR_DELAY);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onPrepared()");
            mMediaPlayer.seekTo(mVideoProgress);
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

            HorizontalVieoProgressWindow progressWindow = new HorizontalVieoProgressWindow(mContext, mHandler, mVideoUri, (int)(mVideoDuration), (int)(mVideoProgress));
            progressWindow.showAt(mParentView);
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
                try {
                    if (!mIsTouchOnSeekBar && mMediaPlayer.isPlaying()) {
                        int videoProgress = mMediaPlayer.getCurrentPosition();
                        mControllerProgress.setProgress((int) (videoProgress * 1.0 / mVideoDuration * 100));
                    }
                    try {
                        this.sleep(100);
                    } catch (InterruptedException e) {
                        //FIXME: exception
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "MediaPlay state wrong ", e);
                }
            }
        }

        public void stop_thread() {
            isRuuning = false;
        }
    }
}

