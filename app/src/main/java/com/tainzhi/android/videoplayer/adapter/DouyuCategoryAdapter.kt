package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.tainzhi.tainzhi.videoplayer.R
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.tainzhi.videoplayer.databinding.ItemDouyuCategoryBinding

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/16 09:43
 * @description:
 **/

class DouyuCategoryAdapter(private val call: (game: DouyuGame) -> Unit) : BaseQuickAdapter<DouyuGame, BaseDataBindingHolder<ItemDouyuCategoryBinding>>(R.layout.item_douyu_category){
    init {
        setOnItemClickListener { _, _, position ->
            call.invoke(data[position])
        }
    }
    override fun convert(holder: BaseDataBindingHolder<ItemDouyuCategoryBinding>, item: DouyuGame) {
        holder.dataBinding?.apply {
            game = item
            executePendingBindings()
        }
    }
}