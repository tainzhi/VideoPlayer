package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean
import com.qfq.tainzhi.videoplayer.widget.RoundCornerImageView

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class DouyuChannelRoomAdapter constructor(context: Context?, rooms: MutableList<DouyuRoomBean?>?) : BaseQuickAdapter<DouyuRoomBean?, BaseViewHolder?>(R.layout.item_douyu_room, rooms) {
    private val mContext: Context?
    override fun convert(holder: BaseViewHolder?, room: DouyuRoomBean?) {
        holder.setText(R.id.tv_nickname, room.getNickname())
                .setText(R.id.tv_room_name, room.getRoom_name())
                .setText(R.id.tv_online, room.getOnline().toString() + "位观众")
        Glide.with(mContext).load(room.getRoom_src()).fitCenter()
                .error(R.mipmap.default_dota2)
                .into(holder.getView<View?>(R.id.iv_room) as RoundCornerImageView?)
    }
    
    init {
        mContext = context
    }
}