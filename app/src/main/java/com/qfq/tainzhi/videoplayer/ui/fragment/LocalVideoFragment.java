package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.DoubleVideoPlayerActivity;
import com.qfq.tainzhi.videoplayer.InsetDecoration;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.SingleVideoPlayerActivity;
import com.qfq.tainzhi.videoplayer.adapters.StaggeredAdapter;
import com.qfq.tainzhi.videoplayer.callbacks.OnStaggeredAdapterInformation;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class LocalVideoFragment extends BaseFragment {
    private static final String TAG = "LocalVideoFragment";
    private static LocalVideoFragment mInstance = null;
    
    private View mView;
    private Context mContext;
    private CharSequence mTitle;
    private RecyclerView mList;
    private ViewStub mHintViewStub;
    private StaggeredAdapter mAdapter;
    private int mItemMargin;
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(mContext, "onItemClick: " + position + ", id: " + id, Toast.LENGTH_SHORT).show();
            Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
            String videoTitle = mAdapter.getVideoItemAtPosition(position).videoName;
            long videoDuration = mAdapter.getVideoItemAtPosition(position).videoDuration;
            Intent intent = new Intent(getContext(), SingleVideoPlayerActivity.class);
            intent.setData(videoUri);
            intent.putExtra("title", videoTitle);
            intent.putExtra("duration", videoDuration);
            startActivity(intent);
        }
        
    };
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //Toast.makeText(mContext, "onItemLongClick: " + position + ", id: " + id, Toast.LENGTH_SHORT).show();
            String filePath = mAdapter.getVideoItemAtPosition(position).videoPath;
            Intent startIntent = new Intent(getContext(),
                    DoubleVideoPlayerActivity.class);
            startIntent.putExtra("file", filePath);
            startActivity(startIntent);
            return true;
        }
    };
    private OnStaggeredAdapterInformation mOnStaggeredAdapterInformation = new OnStaggeredAdapterInformation() {
        @Override
        public void onStaggeredAdapterInformation() {
            mList.setVisibility(View.GONE);
            if (mHintViewStub == null) {
                mHintViewStub =
                        (ViewStub) mView.findViewById(R.id.viewstub_novideo_hint_layout_id);
                mHintViewStub.inflate();
            }
        }
    };
    
    public static LocalVideoFragment getInstance() {
        if (mInstance == null) {
            mInstance = new LocalVideoFragment();
        }
        return mInstance;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_local_video, container,
                false);
        initView(mView);
        Logger.d("");
        return mView;
    }
    
    private void initView(View view) {
        mContext = getContext();
        
        mList = (RecyclerView) mView.findViewById(R.id.selection_list);
        mList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_margin);
        mList.addItemDecoration(new InsetDecoration(mContext, mItemMargin));
        
        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);
        
        //set item width, Window.getWidth - marginLeft - marginRight - 2 * 2 * Insets
        mAdapter = new StaggeredAdapter(mContext, (getWindowWidth() - 6 * mItemMargin) / 2, mOnStaggeredAdapterInformation);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAdapter.setOnItemLongClickListener(mOnItemLongClickListener);
        mList.setAdapter(mAdapter);
    }
    
    private int getWindowWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }
    
    @Override
    public void onDoubleClick() {
        Logger.d("");
    }
    
    
}
