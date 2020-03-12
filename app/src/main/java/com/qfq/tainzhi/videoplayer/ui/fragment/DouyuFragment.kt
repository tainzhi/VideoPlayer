package com.qfq.tainzhi.videoplayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.R2
import com.qfq.tainzhi.videoplayer.adapters.ChannelPagerAdapter
import java.util.*

/**
 * Created by muqing on 2019/6/4.
 * Email: qfq61@qq.com
 */
class DouyuFragment : BaseFragment() {
    @kotlin.jvm.JvmField
    @BindView(R2.id.tab_layout_channel)
    var mTabLayout: TabLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.add_channel_iv)
    var mAddChannel: ImageView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.header_layout)
    var mLinearLayout: LinearLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.view_pager_douyu_channels)
    var mViewPager: ViewPager? = null
    private var mView: View? = null
    private var mTitleList: Array<String?>?
    private var mIdList: IntArray?
    private var mFragmentList: MutableList<Fragment?>? = null
    private fun initData() {
        mTitleList = arrayOf(
                "推荐", "DOTA2", "LOL", "大逃杀", "王者荣耀", "CS:GO")
        mIdList = intArrayOf(
                -1, 3, 1, 270, 181, 6)
        mFragmentList = ArrayList()
        for (i in mTitleList.indices) {
            mFragmentList.add(DouyuLiveFragment(mIdList.get(i),
                                                mTitleList.get(i)))
        }
    }
    
    override fun onDoubleClick() {
        Logger.d("")
    }
    
    @OnClick(R2.id.add_channel_iv)
    fun onClick() {
        val ft = fragmentManager.beginTransaction()
        ft.hide(this)
        ft.add(R.id.container,
               DouyuChannelFragment.Companion.newInstance(),
               DouyuChannelFragment.Companion.DOUYU_CHANNEL_TAG)
        ft.commit()
    }
    
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Logger.d("")
    }
    
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_douyu, container, false)
        ButterKnife.bind(this, mView)
        initData()
        mViewPager.setAdapter(ChannelPagerAdapter(fragmentManager,
                                                  mFragmentList, mTitleList, mIdList))
        mViewPager.setOffscreenPageLimit(15)
        mTabLayout.setupWithViewPager(mViewPager)
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE)
        // mLinearLayout.setBackgroundColor(getContext().getColor(R.color.zhuganzi));
        return mView
    }
    
    override fun onResume() {
        super.onResume()
        // mLinearLayout.setBackgroundColor(getContext().getColor(R.color.zhuganzi));
    }
    
    companion object {
        private var mInstance: DouyuFragment? = null
        fun newInstance(): DouyuFragment? {
            if (mInstance == null) {
                mInstance = DouyuFragment()
            }
            return mInstance
        }
    }
}