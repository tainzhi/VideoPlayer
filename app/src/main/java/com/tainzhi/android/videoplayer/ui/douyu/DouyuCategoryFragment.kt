package com.tainzhi.android.videoplayer.ui.douyu

import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.qfq.tainzhi.videoplayer.R
import com.tainzhi.android.common.base.ui.BaseVMFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryFragment : BaseVMFragment<DouyuCategoryViewModel>( useBinding = true) {
    override fun getLayoutResId() = R.layout.douyu_category_fragment

    override fun initVM(): DouyuCategoryViewModel = getViewModel()

    override fun initView() {
    }

    override fun initData() {
    }

    override fun startObserve() {
    }

    private fun navigateToCategoryRooms(roomId: String) {
        val action = DouyuCategoryFragmentDirections.actionDouyuCategoryFragmentToDouyuGameFragment(roomId)
        findNavController().navigate(action)
    }

}