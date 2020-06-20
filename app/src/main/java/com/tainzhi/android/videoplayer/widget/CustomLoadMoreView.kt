package com.tainzhi.android.videoplayer.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tainzhi.android.videoplayer.R

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/25 下午1:49
 * @description:
 **/


class CustomLoadMoreView : BaseLoadMoreView() {
    override fun getLoadComplete(holder: BaseViewHolder): View =
            holder.getView(R.id.load_more_load_complete_view)

    override fun getLoadEndView(holder: BaseViewHolder): View =
            holder.getView(R.id.load_more_load_end_view)

    override fun getLoadFailView(holder: BaseViewHolder): View =
            holder.getView(R.id.load_more_load_fail_view)

    override fun getLoadingView(holder: BaseViewHolder): View  =
            holder.getView(R.id.load_more_loading_view)

    override fun getRootView(parent: ViewGroup) =
            LayoutInflater.from(parent.context).inflate(R.layout.view_load_more, parent, false)

}
