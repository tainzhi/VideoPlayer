package com.tainzhi.android.videoplayer.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ItemLocalVideoBinding
import com.tainzhi.android.videoplayer.bean.LocalVideo
import com.tainzhi.android.videoplayer.bean.Tv

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/12 15:00
 * @description:
 **/

class LocalVideoAdapter(private val goToPlay: (video: LocalVideo) -> Unit ) : BaseQuickAdapter<LocalVideo, BaseDataBindingHolder<ItemLocalVideoBinding>>(R.layout.item_local_video) {

    init {
        setOnItemClickListener { _, _, position ->
            val clickedTv = data[position]
            goToPlay.invoke(clickedTv)
        }
    }
    override fun convert(holder: BaseDataBindingHolder<ItemLocalVideoBinding>, item: LocalVideo) {
        holder.dataBinding?.apply {
            video = item
            executePendingBindings()
        }
    }

}
