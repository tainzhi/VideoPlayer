package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter;
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean;
import com.qfq.tainzhi.videoplayer.ui.activity.DefaultPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class LocalVideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static LocalVideoFragment mInstance = null;
    private ViewStub mNoVideoHint;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private View mView;
    private List<LocalVideoBean> mLists = new ArrayList<>();
    private LocalVideoAdapter mAdapter;
    
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
        mRefreshLayout =
                (SwipeRefreshLayout) mView.findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.section_lists);
        mNoVideoHint = mView.findViewById(R.id.viewstub_novideo_hint_layout_id);
        
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.zhuganzi));
        mRefreshLayout.setOnRefreshListener(this);
        
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL));
        
        mAdapter = new LocalVideoAdapter(getContext(), mLists);
        mAdapter.setOnItemClickListener(new LocalVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startPlay(mLists.get(position).getId(),
                        mLists.get(position).getTitle(),
                        mLists.get(position).getDuration());
            }
        });
        mAdapter.setOnItemLongClickListener(new LocalVideoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Logger.d("");
                Toast.makeText(getContext(),
                        "Long Click" + mLists.get(position).getPath(),
                        Toast.LENGTH_LONG);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getContext());
                builder.setMessage("删除\"" + mLists.get(position).getTitle());
                builder.setPositiveButton("确定",
                        (dialog, which) -> {
                            mLists.remove(position);
                            Logger.d(mLists.get(position).toString());
                            mAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        });
                builder.setNegativeButton("取消",
                        (dialog, which) -> dialog.dismiss());
                builder.show();
                
            }
        });
        mAdapter.setOnBottomReachedListener(new LocalVideoAdapter.OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                Logger.d("position:%s", position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        
        return mView;
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
    }
    
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        // setAdapter();
        if (mLists.size() == 0) {
            mNoVideoHint.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
        mRefreshLayout.setRefreshing(false);
    }
    
    @Override
    public void onDoubleClick() {
        Logger.d("");
    }
    
    private void startPlay(int id, String title, long duration) {
        Intent intent = new Intent(getContext(),
                DefaultPlayActivity.class);
        Uri videoUri =
                ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
        intent.setData(videoUri);
        intent.putExtra("title", title);
        intent.putExtra("duration", duration);
        startActivity(intent);
    }
}
