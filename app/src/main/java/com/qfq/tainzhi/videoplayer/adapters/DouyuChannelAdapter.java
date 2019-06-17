package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;
import android.content.res.Configuration;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean;
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
public class DouyuChannelAdapter extends BaseQuickAdapter<DouyuChannelBean,
                                                                 BaseViewHolder> {
    private Context mContext;
    
    public DouyuChannelAdapter(Context context,
                               List<DouyuChannelBean> channelList) {
        super(R.layout.item_douyu_channel, channelList);
        mContext = context;
    }
    
    @Override
    protected void convert(BaseViewHolder holder,
                           DouyuChannelBean channelBean) {
        holder.setText(R.id.douyu_channel_name, channelBean.getName());
        Glide.with(mContext).load(channelBean.getIcon()).fitCenter()
                .into((CircleImageView)holder.getView(R.id.ic_douyu_channel));
    }
}
