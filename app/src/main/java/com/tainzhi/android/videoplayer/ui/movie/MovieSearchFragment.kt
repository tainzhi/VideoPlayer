package com.tainzhi.android.videoplayer.ui.movie

import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tainzhi.android.common.base.ui.fragment.BaseVmBindingFragment
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.MovieSearchResultAdapter
import com.tainzhi.android.videoplayer.databinding.MovieSearchFragmentBinding
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.widget.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * File:     MovieSearchFragment
 * Author:   tainzhi
 * Created:  2020/12/26 18:25
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieSearchFragment : BaseVmBindingFragment<MovieViewModel, MovieSearchFragmentBinding>() {

    private val searchResultAdapter by lazy {
        MovieSearchResultAdapter { movie ->
            val bundle = bundleOf(MovieDetailFragment.MOVIE_ID to movie.id)
            findNavController().navigate(R.id.action_movieFragment_to_movieDetailFragment, bundle)
        }.apply {
            loadMoreModule.run {
                loadMoreView = CustomLoadMoreView()
                setOnLoadMoreListener {
                    loadMore()
                }
            }
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.movie_search_fragment
    }

    override fun initView() {
        setHasOptionsMenu(true)
        with(mBinding) {
            // searchView.run {
            //     this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).run {
            //         setTextColor(android.graphics.Color.WHITE)
            //         setHintTextColor(android.graphics.Color.WHITE)
            //         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            //             // null 使得光标与字体同色
            //             textCursorDrawable = null
            //         }
            //     }
            //     setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //         override fun onQueryTextSubmit(query: String?): Boolean {
            //             if (query != null) {
            //                 startSearch(query)
            //             }
            //             return false
            //         }
            //
            //         override fun onQueryTextChange(newText: String?): Boolean {
            //             return false
            //         }
            //     })
            // }

            searchRecyclerView.run {
                adapter = searchResultAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun initVM(): MovieViewModel = getViewModel()

    override fun startObserve() {
        mViewModel.searchResult.observe(this) { result ->
            when (result) {
                is ResultOf.Success -> {
                    searchResultAdapter.run {
                        if (page == 1) setList(result.data)
                        else addData(result.data)
                        loadMoreModule.run {
                            isEnableLoadMore = true
                            loadMoreComplete()
                        }
                    }
                }
                is ResultOf.SuccessEndData -> {
                    searchResultAdapter.run {
                        addData(result.data)
                        loadMoreModule.loadMoreEnd()
                    }
                }
            }
        }
    }

    private var page = 0
    private var searchKey = ""
    private fun startSearch(key: String) {
        page = 1
        searchKey = key
        // 从第一页开始显示查询结果
        mViewModel.searchMovie(searchKey, page)
    }

    private fun loadMore() {
        page++
        mViewModel.searchMovie(searchKey, page)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_search_expanded, menu)
        val searchMenu = menu.findItem(R.id.search)?.apply {
            isVisible = true
        }
        val searchView = ((searchMenu?.actionView) as SearchView).apply {
            // 展示搜索框, 而不是一个搜索 icon
            isIconified = false
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    startSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // localVideoAdapter.filter.filter(query)
                    return false
                }
            })
            this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).run {
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.WHITE)
                hint = "请输入电影名称"
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    // null 使得光标与字体同色
                    textCursorDrawable = null
                }
            }
            this.findViewById<android.widget.ImageView>(R.id.search_button).setImageResource(R.drawable.ic_search)
            this.findViewById<android.widget.ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            // this.findViewById<ImageView>(R.id.search_mag_icon).setImageResource(R.drawable.ic_search)
            // 去掉下划线
            this.findViewById<View>(R.id.search_plate).setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }
}