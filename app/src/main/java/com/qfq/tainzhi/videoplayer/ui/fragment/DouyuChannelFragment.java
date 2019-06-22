package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.adapters.DouyuChannelAdapter;
import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean;
import com.qfq.tainzhi.videoplayer.mvp.presenter.DouyuChannelPresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuChannelPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
public class DouyuChannelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static DouyuChannelFragment mInstance;
    
    @BindView(R2.id.douyu_channel_list)
    RecyclerView mRecyclerView;
    @BindView(R2.id.douyu_channel_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    
    private View mView;
    private List<DouyuChannelBean> mChannels;
    private IDouyuChannelPresenter mDouyuChannelPresenter;
    private DouyuChannelAdapter mAdapter;
    
    public static DouyuChannelFragment newInstance() {
        if (mInstance == null) {
            mInstance = new DouyuChannelFragment();
        }
        return mInstance;
    }
    
    public static DouyuChannelFragment getInstance() {
        return mInstance;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_douyu_channel,
                container, false);
        ButterKnife.bind(this, mView);
        mChannels = new ArrayList<>();
        mDouyuChannelPresenter = new DouyuChannelPresenter(this);
    
        mAdapter = new DouyuChannelAdapter(getContext(),
                mChannels);
        mAdapter.setOnItemClickListener((adapter, view, position)-> {
            // REFACTOR: 2019/6/18 待重构: fragment覆盖在最上面, 暂时想不到好的解决办法
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            ft.add(R.id.container,
                    new DouyuLiveFragment(mChannels.get(position).getId(),
                            mChannels.get(position).getName(), true));
            ft.commit();
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
    
        mDouyuChannelPresenter.getChannelList();
        mRefreshLayout.setRefreshing(true);
        
        return mView;
    }
    
    public void showData(List<DouyuChannelBean> channels) {
        mChannels.clear();
        mChannels.addAll(channels);
        mAdapter.notifyDataSetChanged();
    }
    
    
    public void setLoadComplete() {
        mRefreshLayout.setRefreshing(false);
    }
    
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
    }
}
