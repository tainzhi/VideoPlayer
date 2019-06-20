package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.adapters.TVChannelAdapter;
import com.qfq.tainzhi.videoplayer.bean.TVChannelBean;
import com.qfq.tainzhi.videoplayer.mvp.presenter.TVPresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ITVPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.DefaultPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class TVFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static TVFragment mInstance;
    private View mView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private List<TVChannelBean> mChannelList;
    private ITVPresenter mTVPresenter;
    private TVChannelAdapter mAdapter;
    
    public static TVFragment newInstance() {
        if (mInstance == null) {
            mInstance = new TVFragment();
        }
        return mInstance;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_tv, container, false);
        mRefreshLayout = mView.findViewById(R.id.tv_refresh_layout);
        mRecyclerView = mView.findViewById(R.id.tv_recyvler_view);
        
        mChannelList = new ArrayList<>();
        mTVPresenter = new TVPresenter(this);
        mAdapter = new TVChannelAdapter(getContext(), mChannelList);
        mAdapter.setOnItemClickListener((adapter, view, position)-> {
            startPlay(mChannelList.get(position).getUrl(),
                    mChannelList.get(position).getName());
        });
    
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.zhuganzi));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        
        mTVPresenter.getChannelList();
        
        return mView;
    }
    
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }
    
    public void showData(List<TVChannelBean> channelList) {
        mChannelList.clear();
        mChannelList.addAll(channelList);
        mAdapter.notifyDataSetChanged();
    }

    public void onLoadComplete() {
        mRefreshLayout.setRefreshing(false);
    }
    
    @Override
    public void onDoubleClick() {
        Logger.d("");
    }
    
    private void startPlay(String url, String name) {
        Intent intent = new Intent(getContext(),
                DefaultPlayActivity.class);
        intent.setData(Uri.parse(url));
        Logger.d(url);
        intent.putExtra("title", name);
        startActivity(intent);
    }
}
