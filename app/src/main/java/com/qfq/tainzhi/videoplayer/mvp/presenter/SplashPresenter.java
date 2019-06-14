package com.qfq.tainzhi.videoplayer.mvp.presenter;

import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
public class SplashPresenter implements ISplashPresenter {
    private ISplashActivityView mView;
    
    public SplashPresenter(ISplashActivityView view) {
        this.mView = view;
    }
    
    @Override
    public void setDelay() {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long mLong) throws Exception {
                        mView.enterApp();
                    }
                });
    
    }
    
    @Override
    public void unRegister() {
    }
}
