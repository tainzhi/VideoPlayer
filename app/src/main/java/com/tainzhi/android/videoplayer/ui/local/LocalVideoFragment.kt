package com.tainzhi.android.videoplayer.ui.local

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.kennyc.view.MultiStateView.ViewState.CONTENT
import com.kennyc.view.MultiStateView.ViewState.EMPTY
import com.kennyc.view.MultiStateView.ViewState.ERROR
import com.tainzhi.android.videoplayer.base.ui.LazyLoad
import com.tainzhi.android.videoplayer.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.LocalVideoAdapter
import com.tainzhi.android.videoplayer.adapter.LocalVideoViewHolder
import com.tainzhi.android.videoplayer.adapter.RecyclerItemTouchHelper
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.databinding.LocalVideoFragmentBinding
import com.tainzhi.android.videoplayer.ui.PlayActivity
import com.tainzhi.android.videoplayer.widget.dialog.showCheckPermissionDialog
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 本地视频
 */
class LocalVideoFragment : BaseVmBindingFragment<LocalVideoViewModel, LocalVideoFragmentBinding>(),
    LazyLoad {

    companion object {
        private const val DELETE_PERMISSION_REQUEST = 0x1033
        const val REQUEST_REQUIRED_PERMISSIONS = 42
    }

    private val requestPermissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    private var showRationale = false

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

        setHasOptionsMenu(true)

        with(mBinding) {
            localVideoRecyclerView.run {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = localVideoAdapter
                ItemTouchHelper(recyclerItemTouchHelper).attachToRecyclerView(this)
            }

            localVideoFab.setOnClickListener {
                // TODO: 2020/12/20 实现功能: 最近一次播放的视频 开始播放
                showShackBarMessage("to implement: 播放最近一次观看的视频")
            }
            mBinding.localVideoMultiStateView.run {
                getView(ERROR)?.setOnClickListener {
                    requestMissingRequiredPermissions()
                }
                getView(EMPTY)?.setOnClickListener {
                    initData()
                }
            }
        }

    }

    override fun initData() {
        if (haveStoragePermission()) {
            mBinding.localVideoMultiStateView.viewState = EMPTY
            mViewModel.getLocalVideos()
        } else {
            mBinding.localVideoMultiStateView.viewState = ERROR
            requestMissingRequiredPermissions()
        }
    }

    override fun startObserve() {
        mViewModel.apply {
            localVideoList.observe(viewLifecycleOwner, {
                mBinding.localVideoMultiStateView.viewState = CONTENT
                localVideoAdapter.setList(it)
            }
            )
            // 在Android10+上,获取删除权限
            permissionNeededForDelete.observe(viewLifecycleOwner, { intentSender ->
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.local_search, menu)
        val searchMenu = menu.findItem(R.id.search)?.apply {
            isVisible = true
        }
        ((searchMenu?.actionView) as SearchView).apply {
            // setSearchableInfo(searchManager.getSearchableInfo(gameName))
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    localVideoAdapter.filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    localVideoAdapter.filter.filter(query)
                    return false
                }
            })
            TODO("implement search view")
            // this.findViewById<SearchView.SearchAutoComplete>(android.R.id.search_src_text).run {
            //     setTextColor(android.graphics.Color.WHITE)
            //     setHintTextColor(android.graphics.Color.WHITE)
            //     hint = "请输入视频名称"
            //     if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            //         // null 使得光标与字体同色
            //         textCursorDrawable = null
            //     }
            // }
            // this.findViewById<android.widget.ImageView>(R.id.search_button).setImageResource(R.drawable.ic_search)
            // this.findViewById<android.widget.ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            // // this.findViewById<ImageView>(R.id.search_mag_icon).setImageResource(R.drawable.ic_search)
            // // 去掉下划线
            // this.findViewById<View>(android.R.id.search_plate).setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }

    private fun haveStoragePermission() =
            ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED

    private fun requestMissingRequiredPermissions() {
        if (!haveStoragePermission()) {
            val missing = HashSet<String>()
            showRationale = false
            for (permission in requestPermissions) {
                if (ContextCompat.checkSelfPermission(requireContext(), permission)
                        != PERMISSION_GRANTED) {
                    missing.add(permission)
                    showRationale = showRationale or (shouldShowRequestPermissionRationale(permission))
                }
            }
            if (missing.isNotEmpty()) {
                requestPermissions(missing.toTypedArray(), REQUEST_REQUIRED_PERMISSIONS)
            }
        } else {
            initData()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_REQUIRED_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                initData()
            } else {
                var showRationale = false
                for (i in grantResults.indices) {
                    if (grantResults[i] != PERMISSION_GRANTED) {
                        showRationale = showRationale || shouldShowRequestPermissionRationale(permissions[i])

                    }
                }
                if (showRationale) {
                    showNoAccess()
                } else {
                    // If we were not supposed to show the rationale before requestPermissions(...) and
                    // we still shouldn't show the rationale it means the user previously selected
                    // "don't ask again" in the permission request dialog. In this case we bring up the
                    // system permission settings for this package.
                    requireActivity().showCheckPermissionDialog(childFragmentManager)
                }
            }
        }
    }

    private fun showNoAccess() {

    }
}