package com.tainzhi.android.videoplayer.ui.douyu

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kennyc.view.MultiStateView
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.DouyuRoomAdapter
import com.tainzhi.android.videoplayer.adapter.DouyuRoomItemDecoration
import com.tainzhi.android.videoplayer.databinding.DouyuGameFragmentBinding
import com.tainzhi.android.videoplayer.ui.MainViewModel
import com.tainzhi.android.videoplayer.ui.play.PlayDouyuActivity
import com.tainzhi.android.videoplayer.widget.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 09:32
 * @description: 斗鱼频道页面, 该页面展示当前游戏分类的所有房间列表
 **/
class DouyuGameFragment : BaseVmBindingFragment<DouyuGameViewModel, DouyuGameFragmentBinding>() {

    // 通过 newInstance(gameId) 创建传参
    private val gameId by lazy(LazyThreadSafetyMode.NONE) { arguments?.getString(GAME_ID)}

    // 通过Navigation创建的 DouyuGameFragment 传参
    private val args: DouyuGameFragmentArgs by navArgs()

    private lateinit var douyuGameId: String

    companion object {
        private const val GAME_ID = "game_id"
        fun newInstance(gameId: String): DouyuGameFragment {
            return DouyuGameFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME_ID, gameId)
                }
            }

        }
    }

    private val douyuRoomAdapter by lazy(LazyThreadSafetyMode.NONE){
        DouyuRoomAdapter { room ->
            PlayDouyuActivity.startPlay(requireActivity(),
                    room.room_id,
                    room.room_name
            )
        }.apply {
            loadMoreModule.run {
                loadMoreView = CustomLoadMoreView()
                setOnLoadMoreListener { loadMore() }
            }
        }
    }

    override fun getLayoutResId() = R.layout.douyu_game_fragment

    override fun initVM(): DouyuGameViewModel = getViewModel()

    override fun initView() {
        mBinding.douyuGameRefreshLayout.run {
            setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.color_secondary))
            setOnRefreshListener { refresh() }
        }

        mBinding.douyuGameRecyclerView.run {
            adapter = douyuRoomAdapter.apply {
                addItemDecoration(DouyuRoomItemDecoration())
            }
            layoutManager = GridLayoutManager(requireActivity(), 2).apply {
                val originSpanSizeLookup = spanSizeLookup
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (position == 0) return 2
                        // loadMoreView 横跨两列
                        else if (douyuRoomAdapter.getItemViewType(position) == BaseQuickAdapter.LOAD_MORE_VIEW) return 2
                        else return 1
                    }
                }
            }
        }

        // gameId 只从DouyuFragment传递, 而从DouyuCategoryFragment不传
        // 从分类创建的DouyuGameFragment需要有center title
        if (gameId == null) {
            getSharedViewModel<MainViewModel>().run {
                updateToolbarCenterTitle(args.gameName)
                updateToolbarCenterTitleVisibility(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 退出时, 不显示center title
        if (gameId == null) {
            getSharedViewModel<MainViewModel>().run {
                updateToolbarCenterTitleVisibility(false)
            }
        }
    }

    override fun initData() {
        douyuGameId = gameId ?: args.gameId
        mViewModel.getGameRooms(gameId ?: args.gameId)
    }

    override fun startObserve() {
        mViewModel.uiState.observe(viewLifecycleOwner, Observer { it ->
            mBinding.douyuGameRefreshLayout.isRefreshing = it.showLoading
            it.showSuccess?.let { list ->
                mBinding.douyuGameMultiStateView.viewState = MultiStateView.ViewState.CONTENT
                douyuRoomAdapter.run {
                    if (it.isRefresh) setList(list)
                    else addData(list)
                    loadMoreModule.run {
                        isEnableLoadMore = true
                        loadMoreComplete()
                    }
                }
            }

            if (it.showEnd) douyuRoomAdapter.loadMoreModule.loadMoreEnd()

            it.showError?.let { message ->
                // TODO: 2020/6/20 网络情况判断, 没有网络时, 怎么处理 
                // activity?.toast(if (message.isBlank()) "Net error" else message)
                // homeMultiStateView.viewState = MultiStateView.ViewState.ERROR
            }
        })
    }

    private fun loadMore() {
        mViewModel.getGameRooms(douyuGameId, isRefresh = false)
    }

    private fun refresh() {
        mViewModel.getGameRooms(douyuGameId, isRefresh = true)
    }

}