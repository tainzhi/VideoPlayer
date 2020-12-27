package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.ItemMovieSearchResultBinding
import com.tainzhi.mediaspider.film.bean.SearchResultData

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 17:17
 * @description:
 **/

class MovieSearchResultAdapter(private val goToPlay: (movie: SearchResultData) -> Unit) :
        BaseQuickAdapter<SearchResultData, BaseDataBindingHolder<ItemMovieSearchResultBinding>>(R.layout.item_movie_search_result),
        LoadMoreModule {

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemMovieSearchResultBinding>, item: SearchResultData) {
        holder.dataBinding?.apply {
            movie = item
            executePendingBindings()
        }
    }


}

