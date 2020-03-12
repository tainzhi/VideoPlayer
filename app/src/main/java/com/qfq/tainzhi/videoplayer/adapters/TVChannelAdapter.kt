package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.bean.TVChannelBean

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
class TVChannelAdapter constructor(context: Context?, mList: MutableList<TVChannelBean?>?) : BaseQuickAdapter<TVChannelBean?, BaseViewHolder?>(R.layout.item_tv_channel, mList) {
    private val mContext: Context?
    override fun convert(holder: BaseViewHolder?, channelBean: TVChannelBean?) {
        holder.setText(R.id.tv_tv_channel_name, channelBean.getName())
                .setImageResource(R.id.ic_tv_channel, R.drawable.ic_default_tv_channel)
    }
    
    init {
        mContext = context
    }
}