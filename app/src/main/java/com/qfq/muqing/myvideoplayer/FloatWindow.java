package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FloatWindow implements SurfaceHolder.Callback {

    private final static String TAG = "VideoPlayer/FloatWindow";

    private Context mContext;
    private View mFloatWindowView;
    private ImageView mFloatWindowBack;
    private TextView mFloatWindowViewTitle;
    private ImageView mFloatWindowClose;
    private SurfaceView mFloatWindowPlayView;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private int mFloatWindowPositionX;
    private int mFloatWindowPositionY;

    private Uri mVideoUri;
    private String mVideoTitle;
    private int mVideoProgress;
    private long mVideoDuration;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPlayView;
    private SurfaceHolder mSurfaceHolder;

    private static FloatWindow mInstance;

    public FloatWindow(Context context, Uri uri, String title, int progress, long duration) {
        mContext = context;
        mVideoUri = uri;
        mVideoTitle = title;
        mVideoProgress = progress;
        mVideoDuration = duration;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Log.v(TAG, "video uri=" + uri + ", title=" + title + ", progress=" + progress);
    }

    public static FloatWindow getInstance(Context context, Uri uri, String title, int progress, long duration) {
        if (mInstance == null) {
            mInstance = new FloatWindow(context, uri, title, progress, duration);
        }
        return mInstance;
    }

    public void showFloatWindow() {
        Log.v(TAG, "showFloatWindow()");
        initView();
        initMediaPlayer();

    }

    public void closeFloatWindow() {
        Log.v(TAG, "closeFloatWindow()");
        mMediaPlayer.release();
        mWindowManager.removeView(mFloatWindowView);
        mInstance = null;
    }

    private void initView() {
        Log.v(TAG, "initView");
        mFloatWindowView = View.inflate(mContext, R.layout.float_window_layout, null);
        mFloatWindowBack = (ImageView) mFloatWindowView.findViewById(R.id.float_window_title_back);
        mFloatWindowViewTitle = (TextView)mFloatWindowView.findViewById(R.id.float_window_video_title);
        mFloatWindowViewTitle.setText(mVideoTitle);
        mFloatWindowClose = (ImageView) mFloatWindowView.findViewById(R.id.float_window_title_close);
        mFloatWindowPlayView = (SurfaceView)mFloatWindowView.findViewById(R.id.float_window_play_view);
        mFloatWindowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "back to full screen play window");
                // TODO: 2016/1/23 back to full play window
                Intent backIntent = new Intent(mContext, SingleVideoPlayerActivity.class);
                backIntent.setData(mVideoUri);
                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                backIntent.putExtra("title", mVideoTitle);
                backIntent.putExtra("duration", mVideoDuration);
                backIntent.putExtra("progress", mMediaPlayer.getCurrentPosition());
                mContext.startActivity(backIntent);

                closeFloatWindow();
            }
        });
        mFloatWindowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "close float window");
                closeFloatWindow();
            }
        });

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSPARENT;
//        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        // width : heidht = 16:9, this is for video
        // but the whole floatwindow height will take title height into account
        mLayoutParams.width = (int)mContext.getResources().getDimension(R.dimen.float_window_width);
        mLayoutParams.height = (int)mContext.getResources().getDimension(R.dimen.float_window_height);
        getFloatWindowPosition();
        mLayoutParams.x = mFloatWindowPositionX;
        mLayoutParams.y = mFloatWindowPositionY;
        mWindowManager.addView(mFloatWindowView, mLayoutParams);
    }

    private void initMediaPlayer() {
        Log.v(TAG, "initMediaPlayer");
        mSurfaceHolder = mFloatWindowPlayView.getHolder();
        mSurfaceHolder.addCallback(this);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        try {
            mMediaPlayer.setDataSource(mContext, mVideoUri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, mVideoUri.toString() + " IOException!!!");
        }
    }

    private void getFloatWindowPosition() {
        SharedPreferences sp = mContext.getSharedPreferences(Utils.VIDEO_PLAYER_SETTING_PREFERENCE, Context.MODE_PRIVATE);
        mFloatWindowPositionX = sp.getInt(Utils.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X, -1);
        mFloatWindowPositionY = sp.getInt(Utils.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y, -1);


        // First enter float window, set the window in center
        if (mFloatWindowPositionX == -1 || mFloatWindowPositionY == -1) {
            DisplayMetrics dispalyMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(dispalyMetrics);
            int windowWidth = dispalyMetrics.widthPixels;
            int windowHeight = dispalyMetrics.heightPixels;

            Log.v(TAG, "window width=" + windowWidth + ", height="+windowHeight);
            Log.v(TAG, "float width=" + mLayoutParams.width + ", height=" + mLayoutParams.height);

            // the current window orientation is landscape, but we should layout float window in protrait orientation
            // so the windowWidth is height in protrait orientation
            int temp = windowWidth;
            windowWidth = windowHeight;
            windowHeight = temp;

            // layout float window center initially
            mFloatWindowPositionX = (windowWidth - mLayoutParams.width) /2;
            mFloatWindowPositionY = (windowHeight - mLayoutParams.height) / 2;
        }
    }

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onPrepared()");
            mMediaPlayer.seekTo(mVideoProgress);
            mMediaPlayer.start();
            Log.v(TAG, "MediaPlayer.start()");
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "MediaPlayer.onCompletion()");
            closeFloatWindow();
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
