package com.tainzhi.android.videoplayer.ui.movie

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.adapter.MovieSearchResultAdapter
import com.tainzhi.android.videoplayer.databinding.MovieSearchActivityBinding
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.android.videoplayer.widget.CustomLoadMoreView
import org.koin.androidx.viewmodel.ext.android.getViewModel
import com.tainzhi.android.videoplayer.ui.movie.MovieViewModel as MovieViewModel1

/**
 * File:     MovieSearchActivity
 * Author:   tainzhi
 * Created:  2020/12/26 18:25
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieSearchActivity : BaseVmBindingActivity<MovieViewModel1, MovieSearchActivityBinding>() {

    private val searchResultAdapter by lazy {
        MovieSearchResultAdapter { movie ->
            MovieDetailFragment.start(this, movie.id)
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
        return R.layout.movie_search_activity
    }

    override fun initView() {
        with(mBinding) {
            backIv.setOnClickListener { finish() }
            searchView.run {
                this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).run {
                    setTextColor(android.graphics.Color.WHITE)
                    setHintTextColor(android.graphics.Color.WHITE)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        // null 使得光标与字体同色
                        textCursorDrawable = null
                    }
                }
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            startSearch(query)
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            searchRecyclerView.run {
                adapter = searchResultAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
        }
    }

    override fun initVM(): MovieViewModel1 = getViewModel()

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
}