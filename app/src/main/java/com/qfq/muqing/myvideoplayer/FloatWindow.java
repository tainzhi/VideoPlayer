package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FloatWindow {

    private final static String TAG = "VideoPlayer/FloatWindow";

    private Context mContext;
    private View mFloatWindowView;
    private ImageView mFloatWindowBack;
    private TextView mFloatWindowViewTitle;
    private ImageView mFloatWindowClose;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

    private int mFloatWindowPositionX;
    private int mFloatWindowPositionY;


    public FloatWindow(Context context) {
        mContext = context;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        initView();
        
    }

    private void initView() {
        mFloatWindowView = View.inflate(mContext, R.layout.float_window_layout, null);
        mFloatWindowBack = (ImageView) mFloatWindowView.findViewById(R.id.float_window_title_back);
        mFloatWindowViewTitle = (TextView)mFloatWindowView.findViewById(R.id.float_window_video_title);
        mFloatWindowClose = (ImageView) mFloatWindowView.findViewById(R.id.float_window_title_close);

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.format = PixelFormat.TRANSPARENT;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
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

            mFloatWindowPositionX = (windowWidth - mLayoutParams.width) /2;
            mFloatWindowPositionY = (windowHeight - mLayoutParams.height) / 2;
        }
    }


}
