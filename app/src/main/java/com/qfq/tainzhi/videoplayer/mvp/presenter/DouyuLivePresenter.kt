package com.qfq.tainzhi.videoplayer.mvp.presenter

import com.qfq.tainzhi.videoplayer.bean.DouyuRoomBean
import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannelRooms
import com.qfq.tainzhi.videoplayer.mvp.model.DouyuModel
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.IDouyuLivePresenter
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuLiveFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import java.util.*

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class DouyuLivePresenter constructor(private val mFragment: DouyuLiveFragment?) : IDouyuLivePresenter {
    private val mDouyuModel: DouyuModel?
    public override fun getRoomList(type: Int, title: String?, offset: Int) {
        mDouyuModel.RoomListGet(type, title, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResourceSubscriber<GsonDouyuChannelRooms?>() {
                    public override fun onNext(mGsonChannelRooms: GsonDouyuChannelRooms?) {
                        try {
                            val douyuRooms: MutableList<DouyuRoomBean?>? = ArrayList()
                            for (dataBean: GsonDouyuChannelRooms.DataBean? in mGsonChannelRooms.getData()) {
                                val room: DouyuRoomBean? = DouyuRoomBean()
                                room.setRoom_id(dataBean.getRoom_id())
                                room.setRoom_src(dataBean.getRoom_src())
                                room.setRoom_thumb(dataBean.getRoom_src())
                                room.setOnline(dataBean.getOnline())
                                room.setRoom_name(dataBean.getRoom_name())
                                room.setNickname(dataBean.getNickname())
                                douyuRooms.add(room)
                            }
                            mFragment.showData(douyuRooms)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    
                    public override fun onError(t: Throwable?) {}
                    public override fun onComplete() {
                        mFragment.setLoadComplete()
                    }
                })
    }
    
    init {
        mDouyuModel = DouyuModel()
    }
}