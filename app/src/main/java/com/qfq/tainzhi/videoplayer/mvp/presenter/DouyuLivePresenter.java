package com.qfq.tainzhi.videoplayer.mvp.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean;
import com.qfq.tainzhi.videoplayer.bean.GsonChannelRooms;
import com.qfq.tainzhi.videoplayer.mvp.model.DouyuModel;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuLivePresenter;
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuLiveFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuLivePresenter implements IDouyuLivePresenter {
    private DouyuLiveFragment mFragment;
    private DouyuModel mDouyuModel;
    
    public DouyuLivePresenter(DouyuLiveFragment fragment) {
        mFragment = fragment;
        this.mDouyuModel = new DouyuModel();
    }
    
    @Override
    public void getRoomList(int type, String title, int offset) {
        List<DouyuRoomBean> douyuRooms = new ArrayList<>();
        mDouyuModel.RoomListGet(type, title, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResourceSubscriber<GsonChannelRooms>() {
                    @Override
                    public void onNext(GsonChannelRooms mGsonChannelRooms) {
                        List<DouyuRoomBean> roomInfos = new ArrayList<>();
                        try {
                            Gson gson = new Gson();
                            for (GsonChannelRooms.DataBean dataBean :
                                    mGsonChannelRooms.getData()) {
                                DouyuRoomBean room = new DouyuRoomBean();
                                room.setRoom_id(dataBean.getRoom_id());
                                room.setRoom_src(dataBean.getRoom_src());
                                room.setRoom_thumb(dataBean.getRoom_src());
                                room.setOnline(dataBean.getOnline());
                                room.setRoom_name(dataBean.getRoom_name());
                                room.setNickname(dataBean.getNickname());
                                douyuRooms.add(room);
                            }
                            mFragment.showData(douyuRooms);
                            mFragment.setLoadComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
    
                    @Override
                    public void onError(Throwable t) {
        
                    }
    
                    @Override
                    public void onComplete() {
        
                    }
                });
    }
}
