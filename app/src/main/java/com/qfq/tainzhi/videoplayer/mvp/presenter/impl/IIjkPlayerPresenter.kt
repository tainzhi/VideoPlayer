package com.qfq.tainzhi.videoplayer.mvp.presenter.impl

/**
 * Created by muqing on 2019/6/20.
 * Email: qfq61@qq.com
 */
open interface IIjkPlayerPresenter {
    open fun getSystemTime()
    open fun unRigister()
    open fun autoHidePanel()
    open fun getPlayerInfo(roomId: Int)
    open fun dismissVolumePanel()
}