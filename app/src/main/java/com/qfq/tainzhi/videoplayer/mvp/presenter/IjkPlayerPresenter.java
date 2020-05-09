package com.qfq.tainzhi.videoplayer.mvp.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IIjkPlayerPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.IjkPlayerActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
public class IjkPlayerPresenter implements IIjkPlayerPresenter {
    private IjkPlayerActivity mIjkPlayerActivity;
    
    public IjkPlayerPresenter(IjkPlayerActivity ijkPlayerActivity) {
        mIjkPlayerActivity = ijkPlayerActivity;
    }
    @Override
    public void getSystemTime() {
        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mIjkPlayerActivity.updateSystemTime();
                    }
    
                    @Override
                    public void onError(Throwable e) {
        
                    }
    
                    @Override
                    public void onComplete() {
        
                    }
                });
    }
    
    @Override
    public void unRigister() {
    
    }
    
    @Override
    public void autoHidePanel() {
        Observable.timer(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mIjkPlayerActivity.hidePanel();
                    }
                });
    }
    
    @Override
    public void getPlayerInfo(int roomId) {
    
    }
    
    @Override
    public void dismissVolumePanel() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mIjkPlayerActivity.hideVolumePanel();
                    }
                });
    }
}
