package com.tainzhi.android.videoplayer.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.databinding.ItemMovieChannelBinding
import com.tainzhi.mediaspider.movie.bean.HomeChannelData

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 17:17
 * @description:
 **/

class MovieChannelAdapter(private val goToPlay: (room: HomeChannelData) -> Unit) :
        BaseQuickAdapter<HomeChannelData, BaseDataBindingHolder<ItemMovieChannelBinding>>(R.layout.item_movie_channel),
        LoadMoreModule {

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemMovieChannelBinding>, item: HomeChannelData) {
        holder.dataBinding?.apply {
            movie = item
            executePendingBindings()
        }
    }

}

// 三列
class MovieChannelAdapterDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val rect = Rect()
        rect.right = (parent.context).resources.getDimension(R.dimen.spacing_micro).toInt()
        rect.left = rect.right
        // 第一行
        if (position < 3) {
            rect.top = (parent.context).resources.getDimension(R.dimen.spacing_normal).toInt()
        }
        outRect.set(rect)
    }
}

