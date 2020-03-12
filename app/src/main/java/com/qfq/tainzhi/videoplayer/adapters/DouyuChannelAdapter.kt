package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
class DouyuChannelAdapter constructor(
        context: Context?,
        channelList: MutableList<DouyuChannelBean?>?) : BaseQuickAdapter<DouyuChannelBean?, BaseViewHolder?>(R.layout.item_douyu_channel, channelList) {
    private val mContext: Context?
    override fun convert(
            holder: BaseViewHolder?,
            channelBean: DouyuChannelBean?) {
        holder.setText(R.id.douyu_channel_name, channelBean.getName())
        Glide.with(mContext).load(channelBean.getIcon()).fitCenter()
                .into(holder.getView<View?>(R.id.ic_douyu_channel) as CircleImageView?)
    }
    
    init {
        mContext = context
    }
}