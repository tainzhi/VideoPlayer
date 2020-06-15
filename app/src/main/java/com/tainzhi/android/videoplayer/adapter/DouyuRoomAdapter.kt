package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemDouyuRoomBinding
import com.tainzhi.android.videoplayer.bean.DouyuRoom

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 17:17
 * @description:
 **/

class DouyuRoomAdapter(private val goToPlay: (room : DouyuRoom) -> Unit) :BaseQuickAdapter<DouyuRoom, BaseDataBindingHolder<ItemDouyuRoomBinding>>(R.layout.item_douyu_room) {

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }
    override fun convert(holder: BaseDataBindingHolder<ItemDouyuRoomBinding>, item: DouyuRoom) {
        holder.dataBinding?.apply {
            room = item
            executePendingBindings()
        }
    }

}
