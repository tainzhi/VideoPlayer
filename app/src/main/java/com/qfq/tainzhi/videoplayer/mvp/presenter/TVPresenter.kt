package com.qfq.tainzhi.videoplayer.mvp.presenter

import com.qfq.tainzhi.videoplayer.bean.TVChannelBean
import com.qfq.tainzhi.videoplayer.mvp.model.TVModel
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ITVPresenter
import com.qfq.tainzhi.videoplayer.ui.fragment.TVFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
class TVPresenter constructor(private val mTVFragment: TVFragment?) : ITVPresenter {
    private val model: TVModel?
    public override fun getChannelList() {
        model.getChannels().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<MutableList<String?>?> {
                    public override fun onSubscribe(d: Disposable?) {}
                    public override fun onNext(strings: MutableList<String?>?) {
                        val tvChannels: MutableList<TVChannelBean?>? = ArrayList()
                        var i: Int = 0
                        while (i < strings.size) {
                            val channel: TVChannelBean? = TVChannelBean()
                            //从文件中读取的: 一行是频道地址, 一行是频道名称;
                            //转成数组传过来
                            channel.setUrl(strings.get(i))
                            channel.setName(strings.get(i + 1))
                            tvChannels.add(channel)
                            i += 2
                        }
                        mTVFragment.showData(tvChannels)
                    }
                    
                    public override fun onError(e: Throwable?) {}
                    public override fun onComplete() {
                        mTVFragment.onLoadComplete()
                    }
                })
    }
    
    init {
        model = TVModel(mTVFragment.getContext())
    }
}