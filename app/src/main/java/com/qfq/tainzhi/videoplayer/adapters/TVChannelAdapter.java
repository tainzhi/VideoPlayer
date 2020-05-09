package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.bean.TVChannelBean;

import java.util.List;

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
public class TVChannelAdapter extends BaseQuickAdapter<TVChannelBean, BaseViewHolder> {
    private Context mContext;
    
    public TVChannelAdapter(Context context, List<TVChannelBean> mList) {
        super(R.layout.item_tv_channel, mList);
        mContext = context;
    }
    
    @Override
    protected void convert(BaseViewHolder holder, TVChannelBean channelBean) {
        holder.setText(R.id.tv_tv_channel_name, channelBean.getName())
                .setImageResource(R.id.ic_tv_channel, R.drawable.ic_default_tv_channel);
    }
}
