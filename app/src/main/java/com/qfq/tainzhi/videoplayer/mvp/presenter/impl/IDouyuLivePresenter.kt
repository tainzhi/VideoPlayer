package com.qfq.tainzhi.videoplayer.mvp.presenter.impl

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
open interface IDouyuLivePresenter {
    open fun getRoomList(type: Int, title: String?, offset: Int)
}