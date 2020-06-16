package com.tainzhi.android.videoplayer.ui.douyu

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuCategoryFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.DouyuCategoryAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryFragment : BaseVMFragment<DouyuCategoryViewModel>( useBinding = true) {
    private val douyuCategoryAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuCategoryAdapter() { gameId ->
            navigateToCategoryGames(gameId)
        }
    }
    override fun getLayoutResId() = R.layout.douyu_category_fragment

    override fun initVM(): DouyuCategoryViewModel = getViewModel()

    override fun initView() {
        (mBinding as DouyuCategoryFragmentBinding).douyuCategoryRecyclerView.run {
            adapter = douyuCategoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun initData() {
        mViewModel.getDouyuRooms()
    }

    override fun startObserve() {
        mViewModel.games.observe(viewLifecycleOwner, Observer { games ->
            douyuCategoryAdapter.setList(games)
        })
    }

    private fun navigateToCategoryGames(roomId: String) {
        val action = DouyuCategoryFragmentDirections.actionDouyuCategoryFragmentToDouyuGameFragment()
        findNavController().navigate(action)
    }

}