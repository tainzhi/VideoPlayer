package com.tainzhi.android.videoplayer.ui.movie

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.orhanobut.logger.Logger
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.MovieChannelAdapter
import com.tainzhi.android.videoplayer.adapter.MovieChannelAdapterDecoration
import com.tainzhi.android.videoplayer.databinding.MovieChannelFragmentBinding
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.widget.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MovieChannelFragment : BaseVmBindingFragment<MovieViewModel, MovieChannelFragmentBinding>() {

    companion object {
        private const val CHANNEL_ID = "channel_id"
        fun newInstance(channelId: String): MovieChannelFragment {
            return MovieChannelFragment().apply {
                arguments = Bundle().apply {
                    putString(CHANNEL_ID, channelId)
                }
            }
        }
    }

    private val channelId by lazy { arguments?.getString(CHANNEL_ID) }
    private val movieChannelAdapter by lazy {
        MovieChannelAdapter { movie ->

        }.apply {
            loadMoreModule.run {
                loadMoreView = CustomLoadMoreView()
                setOnLoadMoreListener { loadMore() }
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.movie_channel_fragment
    }

    override fun initData() {
        if (channelId != null) {
            refresh()
        } else {
            Logger.e("channelId is null")
        }
    }

    override fun initView() {
        with(mBinding) {
            movieChannelRefreshLayout.run {
                setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.color_secondary))
                setOnRefreshListener { refresh() }
            }
            movieChannelRecyclerView.run {
                adapter = movieChannelAdapter
                addItemDecoration(MovieChannelAdapterDecoration())
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
        }
    }

    override fun initVM(): MovieViewModel = getViewModel()

    override fun startObserve() {
        mViewModel.channelListLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    mBinding.movieChannelRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    mBinding.movieChannelRefreshLayout.isRefreshing = false
                    movieChannelAdapter.run {
                        if (mViewModel.isRefreshLoading) setList(result.data)
                        else addData(result.data)
                        loadMoreModule.run {
                            isEnableLoadMore = true
                            loadMoreComplete()
                        }
                    }
                }
                is Result.SuccessEndData -> {
                    mBinding.movieChannelRefreshLayout.isRefreshing = false
                    movieChannelAdapter.run {
                        addData(result.data)
                        loadMoreModule.loadMoreEnd()
                    }
                }
                is Result.Error -> {
                    mBinding.movieChannelRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun loadMore() {
        mViewModel.getChannelData(channelId!!, isRefresh = false)
    }

    private fun refresh() {
        mViewModel.getChannelData(channelId!!, isRefresh = true)
    }
}