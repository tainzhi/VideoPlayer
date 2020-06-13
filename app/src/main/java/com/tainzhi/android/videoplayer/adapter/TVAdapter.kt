package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemTvBinding
import com.tainzhi.android.videoplayer.bean.Tv
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 15:51
 * @description:
 **/

class TVAdapter(private val goToPlay: (tv: Tv) -> Unit ): BaseQuickAdapter<Tv, BaseDataBindingHolder<ItemTvBinding>>(R.layout.item_tv ) {

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }

    override fun convert(holder: BaseDataBindingHolder<ItemTvBinding>, item: Tv) {
        holder.dataBinding?.apply {
            tv = item
            loading.progress = Random().nextFloat()
            executePendingBindings()
        }
    }

}