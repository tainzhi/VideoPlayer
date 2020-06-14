package com.tainzhi.android.videoplayer.ui.local

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.LocalVideoFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.LocalVideoAdapter
import com.tainzhi.android.videoplayer.ui.PlayActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 本地视频
 */
class LocalVideoFragment : BaseVMFragment<LocalVideoViewModel>(useBinding = true) {

    private val localVideoAdapter by lazy(LazyThreadSafetyMode.NONE){
        LocalVideoAdapter() { video ->

            PlayActivity.startPlay(requireActivity(),
                    video.uri,
                    video.duration
            )
        }
    }

    override fun getLayoutResId() = R.layout.local_video_fragment

    override fun initVM() = getViewModel<LocalVideoViewModel>()

    override fun initView() {
        (mBinding as LocalVideoFragmentBinding).localVideoRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = localVideoAdapter
        }
    }

    override fun initData() {
        mViewModel.getLocalVideos()
    }

    override fun startObserve() {
        mViewModel.apply {
            localVideoList.observe(viewLifecycleOwner, Observer {it ->
                localVideoAdapter.setList(it)
            })
        }
    }
}