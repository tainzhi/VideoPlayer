package com.tainzhi.android.videoplayer.ui.local

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
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
import com.tainzhi.android.videoplayer.ui.MainViewModel
import com.tainzhi.android.videoplayer.ui.PlayActivity
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 本地视频
 */
class LocalVideoFragment : BaseVMFragment<LocalVideoViewModel>(useBinding = true) {

    private val localVideoAdapter by lazy(LazyThreadSafetyMode.NONE){
        LocalVideoAdapter() { video ->

            PlayActivity.startPlay(requireActivity(),
                    video.uri,
                    video.title
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

        lateinit var searchView: SearchView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 使用MainActivity的ViewModel
        getSharedViewModel<MainViewModel>().run {
            updateToolbarTitle("LocalVideo")
        }
        // binding.toolbar.run {
        //     inflateMenu(R.menu.search)
        //     menu.findItem(R.id.search).isVisible = true
        //     val searchManager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        //     searchView = (menu.findItem(R.id.search).actionView) as SearchView
        //     searchView.run {
        //         // setSearchableInfo(searchManager.getSearchableInfo(gameName))
        //         maxWidth = Integer.MAX_VALUE
        //         queryHint = "hello"
        //         setOnQueryTextListener(object: SearchView.OnQueryTextListener {
        //             override fun onQueryTextSubmit(query: String?): Boolean {
        //                 localVideoAdapter.filter.filter(query)
        //                 return true
        //             }
        //
        //             override fun onQueryTextChange(newText: String?): Boolean {
        //                 localVideoAdapter.filter.filter(newText)
        //                 return false
        //             }
        //         })
        //         this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).run {
        //             setTextColor(Color.WHITE)
        //             setHintTextColor(Color.WHITE)
        //             setHint("请输入视频名称")
        //             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        //                 // null 使得光标与字体同色
        //                 textCursorDrawable = null
        //             }
        //         }
        //         this.findViewById<ImageView>(R.id.search_button).setImageResource(R.drawable.ic_search)
        //         this.findViewById<ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
        //         // this.findViewById<ImageView>(R.id.search_mag_icon).setImageResource(R.drawable.ic_search)
        //         // 去掉下划线
        //         this.findViewById<View>(R.id.search_plate).setBackgroundColor(Color.TRANSPARENT)
        //     }
        // }
        //
        // // 关闭搜索
        // requireActivity().onBackPressedDispatcher.addCallback {
        //     ((mBinding as LocalVideoFragmentBinding).toolbar as Toolbar)
        //             .collapseActionView()
        // }
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