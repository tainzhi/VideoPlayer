package com.qfq.tainzhi.videoplayer.mvp.presenter;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean;
import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannelRooms;
import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannels;
import com.qfq.tainzhi.videoplayer.mvp.model.DouyuModel;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuChannelPresenter;
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuChannelFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
public class DouyuChannelPresenter implements IDouyuChannelPresenter {
    private DouyuChannelFragment mChannelFragment;
    private DouyuModel mDouyuModel;
    
    public DouyuChannelPresenter(DouyuChannelFragment fragment) {
        mChannelFragment = fragment;
        mDouyuModel = new DouyuModel();
    }
    
    @Override
    public void getChannelList() {
        mDouyuModel.ChannelListGet().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<GsonDouyuChannels>() {
                    @Override
                    public void onNext(GsonDouyuChannels gsonDouyuChannels) {
                        List<DouyuChannelBean> channelList = new ArrayList<>();
                        for (GsonDouyuChannels.DataBean dataBean:
                                gsonDouyuChannels.getData()) {
                            DouyuChannelBean channel = new DouyuChannelBean();
                            channel.setId(dataBean.getCate_id());
                            channel.setName(dataBean.getGame_name());
                            channel.setIcon(dataBean.getGame_icon());
                            channelList.add(channel);
                        }
                        mChannelFragment.showData(channelList);
                    }
    
                    @Override
                    public void onError(Throwable t) {
        
                    }
    
                    @Override
                    public void onComplete() {
                        mChannelFragment.setLoadComplete();
        
                    }
                });
    }
}
