package com.qfq.tainzhi.videoplayer.mvp.presenter;

import com.qfq.tainzhi.videoplayer.bean.TVChannelBean;
import com.qfq.tainzhi.videoplayer.mvp.model.TVModel;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ITVPresenter;
import com.qfq.tainzhi.videoplayer.ui.fragment.TVFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
public class TVPresenter implements ITVPresenter {
    
    private TVFragment mTVFragment;
    private TVModel model;
    
    public TVPresenter(TVFragment fragment) {
        mTVFragment = fragment;
        model = new TVModel(mTVFragment.getContext());
    }
    
    @Override
    public void getChannelList() {
        model.getChannels().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
        
                    }
    
                    @Override
                    public void onNext(List<String> strings) {
                        List<TVChannelBean> tvChannels = new ArrayList<>();
                        for (int i = 0; i < strings.size(); i += 2) {
                            TVChannelBean channel = new TVChannelBean();
                            //从文件中读取的: 一行是频道地址, 一行是频道名称;
                            //转成数组传过来
                            channel.setUrl(strings.get(i));
                            channel.setName(strings.get(i+1));
                            tvChannels.add(channel);
                        }
                        mTVFragment.showData(tvChannels);
                    }
    
                    @Override
                    public void onError(Throwable e) {
        
                    }
    
                    @Override
                    public void onComplete() {
                        mTVFragment.onLoadComplete();
                    }
                });
    }
}
