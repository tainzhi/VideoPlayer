package com.tainzhi.android.videoplayer

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemTvBinding
import com.tainzhi.android.videoplayer.bean.Tv

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 15:51
 * @description:
 **/

class TVAdapter: BaseQuickAdapter<Tv, BaseDataBindingHolder<ItemTvBinding>>(R.layout.item_tv) {

    override fun convert(holder: BaseDataBindingHolder<ItemTvBinding>, item: Tv) {
        holder.dataBinding?.apply {
            tv = item
            executePendingBindings()
        }
    }

}