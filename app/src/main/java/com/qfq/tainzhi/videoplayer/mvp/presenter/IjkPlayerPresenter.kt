package com.qfq.tainzhi.videoplayer.mvp.presenter

import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IIjkPlayerPresenter
import com.qfq.tainzhi.videoplayer.ui.activity.IjkPlayerActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
class IjkPlayerPresenter constructor(private val mIjkPlayerActivity: IjkPlayerActivity?) : IIjkPlayerPresenter {
    public override fun getSystemTime() {
        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Long?>() {
                    public override fun onNext(aLong: Long?) {
                        mIjkPlayerActivity.updateSystemTime()
                    }
                    
                    public override fun onError(e: Throwable?) {}
                    public override fun onComplete() {}
                })
    }
    
    public override fun unRigister() {}
    public override fun autoHidePanel() {
        Observable.timer(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Long?> {
                    @Throws(Exception::class)
                    public override fun accept(aLong: Long?) {
                        mIjkPlayerActivity.hidePanel()
                    }
                })
    }
    
    public override fun getPlayerInfo(roomId: Int) {}
    public override fun dismissVolumePanel() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<Long?> {
                    @Throws(Exception::class)
                    public override fun accept(aLong: Long?) {
                        mIjkPlayerActivity.hideVolumePanel()
                    }
                })
    }
    
}