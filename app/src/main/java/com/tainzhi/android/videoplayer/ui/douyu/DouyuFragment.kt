package com.tainzhi.android.videoplayer.ui.douyu

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuFragmentBinding
import com.tainzhi.android.common.base.ui.BaseFragment

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼首页
 **/

class DouyuFragment : BaseFragment(useBinding = true) {
    private val defaultChannels = arrayListOf(
            ChannelIdRoom(-1, "推荐"),
            ChannelIdRoom(3, "DOTA2"),
            ChannelIdRoom(1, "LOL"),
            ChannelIdRoom(270, "大逃杀"),
            ChannelIdRoom(181, "王者荣耀"),
            ChannelIdRoom(6, "CS:GO")
    )

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

            }
        }
    }

    override fun getLayoutResId() = R.layout.douyu_fragment

    override fun initView() {
        val binding = mBinding as DouyuFragmentBinding
        binding.douyuViewPager.run {
            offscreenPageLimit = 4
            adapter = object : FragmentStateAdapter(this@DouyuFragment) {
                override fun createFragment(position: Int): Fragment {
                    return DouyuGameFragment(defaultChannels[position].id, defaultChannels[position].name)
                }

                override fun getItemCount() = defaultChannels.size
            }
        }
        TabLayoutMediator(binding.douyuTabLayout, binding.douyuViewPager) { tab, position ->
            tab.text = defaultChannels[position].name
        }.attach()

    }

    override fun initData() {

    }

    override fun onResume() {
        super.onResume()
        // viewPager.registerOnPageChangeCallback(onPageChangeCallback)
        // TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        //     tab.text = defaultChannels[position].name
        // }.attach()
    }

    override fun onPause() {
        super.onPause()
        // viewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        // TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        //     tab.text = defaultChannels[position].name
        // }.detach()
    }
}

data class ChannelIdRoom(val id: Int, val name: String)
