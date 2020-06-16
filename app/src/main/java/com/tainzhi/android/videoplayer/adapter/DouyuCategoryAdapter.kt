package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemDouyuCategoryBinding
import com.tainzhi.android.videoplayer.bean.DouyuGame

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/16 09:43
 * @description:
 **/

class DouyuCategoryAdapter(private val call: (roomId: String) -> Unit) : BaseQuickAdapter<DouyuGame, BaseDataBindingHolder<ItemDouyuCategoryBinding>>(R.layout.item_douyu_category){
    init {
        setOnItemClickListener { _, _, position ->
            val room = data[position]
            call.invoke(room.cate_id.toString())
        }
    }
    override fun convert(holder: BaseDataBindingHolder<ItemDouyuCategoryBinding>, item: DouyuGame) {
        holder.dataBinding?.apply {
            game = item
            executePendingBindings()
        }
    }
}