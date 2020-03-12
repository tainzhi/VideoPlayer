package com.qfq.tainzhi.videoplayer.mvp.presenter

import com.qfq.tainzhi.videoplayer.bean.DouyuChannelBean
import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannels
import com.qfq.tainzhi.videoplayer.mvp.model.DouyuModel
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuChannelPresenter
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuChannelFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import java.util.*

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
class DouyuChannelPresenter constructor(private val mChannelFragment: DouyuChannelFragment?) : IDouyuChannelPresenter {
    private val mDouyuModel: DouyuModel?
    public override fun getChannelList() {
        mDouyuModel.ChannelListGet().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResourceSubscriber<GsonDouyuChannels?>() {
                    public override fun onNext(gsonDouyuChannels: GsonDouyuChannels?) {
                        val channelList: MutableList<DouyuChannelBean?>? = ArrayList()
                        for (dataBean: GsonDouyuChannels.DataBean? in gsonDouyuChannels.getData()) {
                            val channel: DouyuChannelBean? = DouyuChannelBean()
                            channel.setId(dataBean.getCate_id())
                            channel.setName(dataBean.getGame_name())
                            channel.setIcon(dataBean.getGame_icon())
                            channelList.add(channel)
                        }
                        mChannelFragment.showData(channelList)
                    }
                    
                    public override fun onError(t: Throwable?) {}
                    public override fun onComplete() {
                        mChannelFragment.setLoadComplete()
                    }
                })
    }
    
    init {
        mDouyuModel = DouyuModel()
    }
}