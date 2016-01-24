package com.qfq.muqing.myvideoplayer.DoubleDecode;

import android.content.Context;
import android.os.Handler;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.view.View;

import com.qfq.muqing.myvideoplayer.R;
import com.qfq.muqing.myvideoplayer.adapters.HorizontalGridViewAdapter;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalVieoProgressWindow {

    private final static String TAG = "VideoPlayer/HorizontalVideoProgressWindow";
    private Context mContext;
    private Handler mHandler;

    private HorizontalGridView mHorizontalVideoProgressView;
    private HorizontalGridViewAdapter mAdapter;

    private HorizontalVieoProgressWindow(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
        initView();
    }

    private void initView() {
        mHorizontalVideoProgressView = (HorizontalGridView) View.inflate(mContext, R.layout.horizontal_video_progress_window_layout, null);
        mAdapter = new HoA
        mHorizontalVideoProgressView.setAdapter(mAdapter);
        mHorizontalVideoProgressView.setO
    }
}
