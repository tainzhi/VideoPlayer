package com.tainzhi.android.danmu.advancedanmu.control.dispatch

import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 11:30
 * @description:
 **/

interface IDispatcher {
    fun dispatch(danmu: Danmu, channels: Array<Channel>)
}