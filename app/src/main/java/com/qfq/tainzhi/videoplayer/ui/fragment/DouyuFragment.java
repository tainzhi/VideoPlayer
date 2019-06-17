package com.qfq.tainzhi.videoplayer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.adapters.ChannelPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
public class DouyuFragment extends BaseFragment {
    private static DouyuFragment mInstance = null;
    @BindView(R2.id.tab_layout_channel)
    TabLayout mTabLayout;
    @BindView(R2.id.add_channel_iv)
    ImageView mAddChannel;
    @BindView(R2.id.header_layout)
    LinearLayout mLinearLayout;
    @BindView(R2.id.view_pager_douyu_channels)
    ViewPager mViewPager;
    
    private View mView;
    private String[] mTitleList;
    private int[] mIdList;
    private List<Fragment> mFragmentList;
    
    public static DouyuFragment newInstance() {
        if (mInstance == null) {
            mInstance = new DouyuFragment();
        }
        return mInstance;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_douyu, container, false);
        ButterKnife.bind(this, mView);
        initData();
        mViewPager.setAdapter(new ChannelPagerAdapter(getFragmentManager(),
                mFragmentList, mTitleList, mIdList));
        mViewPager.setOffscreenPageLimit(15);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // mLinearLayout.setBackgroundColor(getContext().getColor(R.color.zhuganzi));
        
        return mView;
    }
    
    private void initData() {
        mTitleList  =  new String[]{
                "推荐", "DOTA2", "LOL", "大逃杀", "王者荣耀", "CS:GO"};
    
        mIdList = new int[] {
                -1, 3, 1, 270, 181, 6};
        mFragmentList = new ArrayList<>();
        
        for (int i = 0; i < mTitleList.length; i++) {
            mFragmentList.add(DouyuLiveFragment.newInstance(mIdList[i],
                    mTitleList[i]));
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // mLinearLayout.setBackgroundColor(getContext().getColor(R.color.zhuganzi));
    }
    
    
    @Override
    public void onDoubleClick() {
        Logger.d("");
    }
    
    @OnClick(R2.id.add_channel_iv)
    public void onClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(mInstance);
        ft.add(R.id.container, DouyuChannelFragment.newInstance());
        ft.commit();
    }
    
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logger.d("");
    }
}
