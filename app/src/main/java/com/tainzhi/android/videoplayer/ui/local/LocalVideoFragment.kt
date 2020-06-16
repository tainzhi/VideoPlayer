package com.tainzhi.android.videoplayer.ui.local

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.LocalVideoFragmentBinding
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.videoplayer.adapter.LocalVideoAdapter
import com.tainzhi.android.videoplayer.adapter.LocalVideoViewHolder
import com.tainzhi.android.videoplayer.adapter.RecyclerItemTouchHelper
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
        val binding = mBinding as LocalVideoFragmentBinding
        binding.localVideoRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = localVideoAdapter
            ItemTouchHelper(recyclerItemTouchHelper).attachToRecyclerView(this)
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

    private val recyclerItemTouchHelper = RecyclerItemTouchHelper(
            0,
            ItemTouchHelper.LEFT,
            object : RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
                    if (viewHolder is LocalVideoViewHolder<*>) {
                        // mViewModel.deleteBrowseHistory(viewHolder.video)


                        // todo 弹出框, 是否确认删除
                        // val snackbar: Snackbar = Snackbar.make(mBinding.root, R.string.remove_item_msg,
                        //         Snackbar.LENGTH_LONG)
                        // snackbar.setAction(R.string.undo) { _ ->
                        //     mViewModel.insertBrowseHistory(viewHolder.browseHistory!!)
                        // }
                        // snackbar.setActionTextColor(Color.YELLOW)
                        // snackbar.show()
                    }
                }
            }
    )
}