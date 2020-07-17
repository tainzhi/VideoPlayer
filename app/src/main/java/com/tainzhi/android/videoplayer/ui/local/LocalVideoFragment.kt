package com.tainzhi.android.videoplayer.ui.local

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.tainzhi.android.common.base.ui.BaseVmBindingFragment
import com.tainzhi.android.ffmpeg.FFmpegInvoker
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.LocalVideoAdapter
import com.tainzhi.android.videoplayer.adapter.LocalVideoViewHolder
import com.tainzhi.android.videoplayer.adapter.RecyclerItemTouchHelper
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.databinding.LocalVideoFragmentBinding
import com.tainzhi.android.videoplayer.ui.MainViewModel
import com.tainzhi.android.videoplayer.ui.PlayActivity
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 本地视频
 */
class LocalVideoFragment : BaseVmBindingFragment<LocalVideoViewModel, LocalVideoFragmentBinding>() {

    companion object {
        private const val DELETE_PERMISSION_REQUEST = 0x1033
    }

    private val localVideoAdapter by lazy(LazyThreadSafetyMode.NONE) {
        LocalVideoAdapter { video ->

            PlayActivity.startPlay(requireActivity(),
                    video.uri,
                    video.title,
                    resolution = video.resolution
            )
        }
    }

    override fun getLayoutResId() = R.layout.local_video_fragment

    override fun initVM() = getViewModel<LocalVideoViewModel>()

    override fun initView() {

        mBinding.localVideoRecyclerView.run {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = localVideoAdapter
            ItemTouchHelper(recyclerItemTouchHelper).attachToRecyclerView(this)
        }

        mBinding.localVideoFab.setOnClickListener {
            FFmpegInvoker.ffmpegVersion()
            showShackBarMessage("to implement: 播放最近一次观看的视频")
        }


        getSharedViewModel<MainViewModel>().run {
            updateToolbarSearchView(true)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        getSharedViewModel<MainViewModel>().run {
            updateToolbarSearchView(false)
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
            // 在Android10+上,获取删除权限
            permissionNeededForDelete.observe(viewLifecycleOwner, Observer { intentSender ->
                intentSender?.let {
                    // On Android 10+, if the app doesn't have permission to modify
                    // or delete an item, it returns an `IntentSender` that we can
                    // use here to prompt the user to grant permission to delete (or modify)
                    // the image.
                    startIntentSenderForResult(
                            intentSender,
                            DELETE_PERMISSION_REQUEST,
                            null,
                            0,
                            0,
                            0,
                            null
                    )
                }
            })
        }

        getSharedViewModel<MainViewModel>().searchString.observe(requireActivity(), Observer { search ->
            localVideoAdapter.filter.filter(search)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST) {
            mViewModel.deletePendingVideo()
        }
    }


    private val recyclerItemTouchHelper = RecyclerItemTouchHelper(
            0,
            ItemTouchHelper.LEFT,
            object : RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
                    if (viewHolder is LocalVideoViewHolder<*>) {
                        val video = localVideoAdapter.data[position]
                        localVideoAdapter.notifyItemRemoved(position)
                        deleteVideo(video, position)
                    }
                }
            }
    )

    /**
     * 删除所在position的video
     *
     * @param video 要删除的视频
     * @param position 该要删除的视频所在adapter中的位置
     */
    private fun deleteVideo(video: LocalVideo, position: Int) {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_dialog_title)
                .setMessage(getString(R.string.delete_dialog_message, video.title))
                .setPositiveButton(R.string.delete_dialog_positive) { _: DialogInterface, _: Int ->
                    mViewModel.deleteVideo(video.uri)
                    showShackBarMessage("已经删除${video.title}")
                }
                .setNegativeButton(R.string.delete_dialog_negative) { dialog: DialogInterface, _: Int ->
                    localVideoAdapter.notifyItemInserted(position)
                    dialog.dismiss()
                }
                .show()
    }

    /**
     * 在宿主activity的BottomNavigationView上显示SnackBar
     */
    private fun showShackBarMessage(message: String) {
        val bottomNavView: BottomNavigationView = activity?.findViewById(R.id.bottom_nav)!!
        Snackbar.make(bottomNavView, message, Snackbar.LENGTH_SHORT)
                // .setAction("Undo") {
                //
                // }
                .apply {
                    anchorView = bottomNavView
                }.show()
    }
}