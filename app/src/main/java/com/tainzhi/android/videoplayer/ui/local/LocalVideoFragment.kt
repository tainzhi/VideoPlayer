package com.tainzhi.android.videoplayer.ui.local

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.LocalVideoFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.LocalVideoAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 本地视频
 */
class LocalVideoFragment : BaseVMFragment<LocalVideoViewModel>(useBinding = true) {

    private val localVideoAdapter by lazy(LazyThreadSafetyMode.NONE){
        LocalVideoAdapter() { video ->

            // PlayActivity.startPlay(requireActivity(),
            //         video.uri,
            //         video.duration
            // )
            showShackBarMessage("${video.title}")
        }
    }

    override fun getLayoutResId() = R.layout.local_video_fragment

    override fun initVM() = getViewModel<LocalVideoViewModel>()

    override fun initView() {
        val binding = mBinding as LocalVideoFragmentBinding
        binding.localVideoRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = localVideoAdapter
        }

        binding.localVideoFab.setOnClickListener {
            showShackBarMessage("to implement: 播放最近一次观看的视频")
        }
    }

    override fun initData() {
        mViewModel.getLocalVideos()
    }

    override fun startObserve() {
        mViewModel.apply {
            localVideoList.observe(viewLifecycleOwner, Observer { it ->
                localVideoAdapter.setList(it)
            })
        }
    }

    /**
     * 在宿主activity的BottomNavigationView上显示SnackBar
     */
    private fun showShackBarMessage(message: String) {
        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav)!!
        Snackbar.make(bottomNavView, message, Snackbar.LENGTH_SHORT).apply {
            anchorView = bottomNavView
        }.show()
    }
}