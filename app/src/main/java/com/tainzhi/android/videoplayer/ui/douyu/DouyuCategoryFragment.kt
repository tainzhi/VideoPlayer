package com.tainzhi.android.videoplayer.ui.douyu

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.DouyuCategoryFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.adapter.DouyuCategoryAdapter
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼分类页面
 **/
class DouyuCategoryFragment : BaseVmBindingFragment<DouyuCategoryViewModel, DouyuCategoryFragmentBinding>() {
    private val douyuCategoryAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuCategoryAdapter() { game ->
            navigateToCategoryGames(game)
        }
    }
    override fun getLayoutResId() = R.layout.douyu_category_fragment

    override fun initVM(): DouyuCategoryViewModel = getViewModel()

    override fun initView() {
        mBinding.douyuCategoryRecyclerView.run {
            adapter = douyuCategoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun initData() {
        mViewModel.getDouyuRooms()
    }

    override fun startObserve() {
        mViewModel.games.observe(viewLifecycleOwner, Observer { games ->
            douyuCategoryAdapter.setList(games)
        })
    }

    private fun navigateToCategoryGames(game: DouyuGame) {
        val action = DouyuCategoryFragmentDirections.actionDouyuCategoryFragmentToDouyuGameFragment(
                game.cate_id.toString(),
                game.game_name
        )
        findNavController().navigate(action)
    }


}