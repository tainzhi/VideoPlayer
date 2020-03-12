package com.qfq.tainzhi.videoplayer.mvp.presenter

import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
class SplashPresenter constructor(private val mView: ISplashActivityView?) : ISplashPresenter {
    public override fun setDelay() {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribe(object : Consumer<Long?> {
                    @Throws(Exception::class)
                    public override fun accept(mLong: Long?) {
                        mView.enterApp()
                    }
                })
    }
    
    public override fun unRegister() {}
    
}