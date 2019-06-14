package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.SingleVideoPlayerActivity;
import com.qfq.tainzhi.videoplayer.adapters.DouyuChannelRoomAdapter;
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean;
import com.qfq.tainzhi.videoplayer.mvp.presenter.DouyuLivePresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuLivePresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuLiveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String CHANNEL_ID = "channel_id";
    private static final String CHANNEL_TITLE = "channel_title";
    
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private String mChannelTitle;
    private int mChannelId;
    private View mView;
    private List<DouyuRoomBean> mChannelRooms;
    private IDouyuLivePresenter mDouyuLivePresenter;
    private DouyuChannelRoomAdapter mAdapter;
    private int mOffset = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mChannelId = bundle.getInt(CHANNEL_ID, 0);
            mChannelTitle = bundle.getString(CHANNEL_TITLE);
        }
    }
    
    public static DouyuLiveFragment newInstance(int id, String title) {
        Bundle bundle = new Bundle();
        bundle.putInt(CHANNEL_ID, id);
        bundle.putString(CHANNEL_TITLE, title);
        DouyuLiveFragment douyuLiveFragment = new DouyuLiveFragment();
        douyuLiveFragment.setArguments(bundle);
        return douyuLiveFragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_douyu_live, container,
                false);
        mRefreshLayout = mView.findViewById(R.id.douyu_live_refresh_layout);
        mRecyclerView = mView.findViewById(R.id.douyu_channel_room_list);
        mChannelRooms = new ArrayList<>();
        mDouyuLivePresenter = new DouyuLivePresenter(this);
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset);
        mAdapter = new DouyuChannelRoomAdapter(getContext(), mChannelRooms);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startPlay(mChannelRooms.get(position).getRoom_id(),
                        mChannelRooms.get(position).getNickname());
        
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                Logger.d("moffset:%s", mOffset);
                onLoadMore();
            }
        }, mRecyclerView);
      
        initView();
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }
    
    private void initView() {
        final GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            // FIXME: 2019/6/14 功能无法实现: 第一行1列, 其余行2列
            public int getSpanSize(int position) {
                return (position == 0) ? 1 : gridLayoutManager.getSpanCount();
            }
        });
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.zhuganzi));
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(true);
    }
    
    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mOffset = 0;
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset);
    }
    
    public void showData(List<DouyuRoomBean> rooms) {
        if (mOffset == 0) {
            mChannelRooms.clear();
            mChannelRooms.addAll(rooms);
        } else {
            mChannelRooms.addAll(rooms);
        }
        mAdapter.notifyDataSetChanged();
    }
    
    public void onLoadMore() {
        mOffset += 20;
        mRefreshLayout.setRefreshing(true);
        mDouyuLivePresenter.getRoomList(mChannelId, mChannelTitle, mOffset);
    }
    
    public void setLoadComplete() {
        mRefreshLayout.setRefreshing(false);
        mAdapter.loadMoreComplete();
    }
    
    private void startPlay(int roomId, String title) {
        String path = "http://tx2play1.douyucdn" +
                              ".cn/live/237974rptJVwMilG_4000p.flv";
        Intent intent = new Intent(getContext(),
                SingleVideoPlayerActivity.class);
        intent.setData(Uri.parse(path));
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
