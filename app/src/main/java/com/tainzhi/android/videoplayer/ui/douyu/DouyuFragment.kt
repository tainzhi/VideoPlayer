package com.tainzhi.android.videoplayer.ui.douyu

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tainzhi.android.common.base.ui.LazyLoad
import com.tainzhi.android.common.base.ui.fragment.BaseBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.DouyuFragmentBinding

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼首页
 **/

class DouyuFragment : BaseBindingFragment<DouyuFragmentBinding>(), LazyLoad {
    private val defaultChannels = arrayListOf(
            ChannelIdRoom(-1, "推荐"),
            ChannelIdRoom(3, "DOTA2"),
            ChannelIdRoom(1, "LOL"),
            ChannelIdRoom(270, "大逃杀"),
            ChannelIdRoom(181, "王者荣耀"),
            ChannelIdRoom(6, "CS:GO")
    )

    override fun getLayoutResId() = R.layout.douyu_fragment

    override fun initView() {
        with(mBinding) {
            douyuViewPager.run {
                offscreenPageLimit = 2
                adapter = object : FragmentStateAdapter(this@DouyuFragment) {
                    override fun createFragment(position: Int): Fragment {
                        return DouyuGameFragment.newInstance(defaultChannels[position].id.toString())
                    }

                    override fun getItemCount() = defaultChannels.size
                }
            }
            TabLayoutMediator(douyuTabLayout, douyuViewPager) { tab, position ->
                tab.text = defaultChannels[position].name
            }.attach()

            douyuMore.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_douyuFragment_to_douyuCategoryFragment)
            }
        }
    }

}

data class ChannelIdRoom(val id: Int, val name: String)
