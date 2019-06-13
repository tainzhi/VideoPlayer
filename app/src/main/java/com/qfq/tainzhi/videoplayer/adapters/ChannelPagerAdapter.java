package com.qfq.tainzhi.videoplayer.adapters;

import android.animation.TimeAnimator;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuLiveFragment;

import java.util.List;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class ChannelPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private String[] mTitleList;
    private int[] mIdList;
    
    public ChannelPagerAdapter(FragmentManager fm, List<Fragment> flist,
                               String[] tList, int[] iList) {
        super(fm);
        mFragmentList = flist;
        mTitleList = tList;
        mIdList = iList;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }
    
    @Override
    public int getCount() { return mTitleList.length; };
    
    @Override
    public Fragment getItem(int positon) {
        return mFragmentList.get(positon);
    }
}
