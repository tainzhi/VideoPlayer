package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.qfq.muqing.myvideoplayer.adapters.HorizontalGridViewAdapter;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalVieoProgressWindow {

    private final static String TAG = "VideoPlayer/HorizontalVideoProgressWindow";
    private Context mContext;
    private Handler mHandler;
    private Uri mVideoUri;
    private int mVideoDuration;
    private int mVideoProgress;

    private HorizontalGridView mHorizontalVideoProgressView;
    private HorizontalGridViewAdapter mAdapter;

    public HorizontalVieoProgressWindow(Context context, Handler handler,
                                         Uri uri, int duration, int progress) {
        mContext = context;
        mHandler = handler;
        mVideoUri = uri;
        mVideoDuration = duration;
        mVideoProgress = progress;

        initView();
    }

    private void initView() {
        mHorizontalVideoProgressView = (HorizontalGridView) View.inflate(mContext, R.layout.horizontal_video_progress_window_layout, null);
        mAdapter = new HorizontalGridViewAdapter(mContext, mVideoUri, mVideoDuration, mVideoProgress);
        mHorizontalVideoProgressView.setAdapter(mAdapter);
        mHorizontalVideoProgressView.setWindowAlignment(HorizontalGridView.WINDOW_ALIGN_BOTH_EDGE);
        mHorizontalVideoProgressView.setWindowAlignmentOffsetPercent(35);
    }

    public void showAt(View parentView) {
        PopupWindow mainWindow = new PopupWindow(mHorizontalVideoProgressView);
        mainWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
    }
}
