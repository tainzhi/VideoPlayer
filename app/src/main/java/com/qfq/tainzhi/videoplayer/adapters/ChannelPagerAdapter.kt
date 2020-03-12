package com.qfq.tainzhi.videoplayer.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class ChannelPagerAdapter constructor(
        fm: FragmentManager?, private val mFragmentList: MutableList<Fragment?>?,
        private val mTitleList: Array<String?>?, private val mIdList: IntArray?) : FragmentPagerAdapter(fm) {
    public override fun getPageTitle(position: Int): CharSequence? {
        return mTitleList.get(position)
    }
    
    public override fun getCount(): Int {
        return mTitleList.size
    }
    
    public override fun getItem(positon: Int): Fragment? {
        return mFragmentList.get(positon)
    }
    
}