package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.qfq.muqing.myvideoplayer.adapters.HorizontalGridViewAdapter;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalVieoProgressWindow {

    private final static String TAG = "VideoPlayer/HorizontalVideoProgressWindow";

    private final static int CONTROLLER_SEEK_TO = 1;

    private Context mContext;
    private Handler mHandler;
    private Uri mVideoUri;
    private int mVideoDuration;
    private int mVideoProgress;
    private int mProgressThumbWidth;
    private int mProgressThumbHeight;

    private View mHorizontalVideoProgressViewContainer;
    private HorizontalGridView mHorizontalVideoProgressView;
    private HorizontalGridViewAdapter mAdapter;

    private PopupWindow mPopupWindow;

    public HorizontalVieoProgressWindow(Context context, Handler handler,
                                         Uri uri, int duration, int progress,
                                        int progressThumbWidth,
                                        int progressThumbHeight) {
        mContext = context;
        mHandler = handler;
        mVideoUri = uri;
        mVideoDuration = duration;
        mVideoProgress = progress;
        mProgressThumbWidth = progressThumbWidth;
        mProgressThumbHeight = progressThumbHeight;

        initView();
    }

    private void initView() {
        Log.v(TAG, "initView");
        mHorizontalVideoProgressViewContainer = View.inflate(mContext, R.layout.horizontal_video_progress_window_layout, null);
        mHorizontalVideoProgressView = (HorizontalGridView) mHorizontalVideoProgressViewContainer.findViewById(R.id.horizontalgridview_videoprogress_id);
        mAdapter = new HorizontalGridViewAdapter(mContext, mVideoUri, mVideoDuration, mVideoProgress, mProgressThumbWidth, mProgressThumbHeight);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mHorizontalVideoProgressView.setAdapter(mAdapter);
        mHorizontalVideoProgressView.setWindowAlignment(HorizontalGridView.WINDOW_ALIGN_BOTH_EDGE);
        mHorizontalVideoProgressView.setWindowAlignmentOffsetPercent(35);
    }

    public void showAt(View parentView, int x, int y) {
        Log.v(TAG, "showAt");
        mPopupWindow = new PopupWindow(mHorizontalVideoProgressViewContainer, mProgressThumbWidth, mProgressThumbHeight);
        mPopupWindow.setAnimationStyle(R.style.animation_single_video_player_progress_popwindow);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupWindow.showAtLocation(parentView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 300);
        mPopupWindow.showAsDropDown(parentView);
        mPopupWindow.update(x, y, -1, -1, true);

    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long videoProgress) {
            Message msg = new Message();
            msg.what = CONTROLLER_SEEK_TO;
            msg.arg1 = (int)videoProgress;
            mHandler.sendMessage(msg);
            mPopupWindow.dismiss();
        }
    };

}
