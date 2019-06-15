package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean;
import com.qfq.tainzhi.videoplayer.widget.RoundCornerImageView;

import java.util.List;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuChannelRoomAdapter extends BaseQuickAdapter<DouyuRoomBean,
                                                                     BaseViewHolder> {
    private Context mContext;
    
    public DouyuChannelRoomAdapter(Context context, List<DouyuRoomBean> rooms) {
        super(R.layout.item_douyu_room, rooms);
        mContext = context;
    }
    
    @Override
    protected void convert(BaseViewHolder holder, DouyuRoomBean room) {
        holder.setText(R.id.tv_nickname, room.getNickname())
                .setText(R.id.tv_room_name, room.getRoom_name())
                .setText(R.id.tv_online, room.getOnline() + "位观众");
        Glide.with(mContext).load(room.getRoom_src()).fitCenter()
                .error(R.mipmap.default_dota2)
                .into((RoundCornerImageView)holder.getView(R.id.iv_room));
        
    }
    
}
