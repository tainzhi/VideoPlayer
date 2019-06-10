package com.qfq.tainzhi.videoplayer.mvp.presenter;

import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
public class SplashPresenter implements ISplashPresenter {
    private ISplashActivityView mView;
    private Subscription mSubscription;
    
    public SplashPresenter(ISplashActivityView view) {
        this.mView = view;
    }
    
    @Override
    public void setDelay() {
        mSubscription = Observable.timer(300, TimeUnit. MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long mLong) {
                        mView.enterApp();
                    }
                });
    }
    
    @Override
    public void unRegister() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
