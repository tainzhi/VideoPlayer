package com.tanzhi.android.danmu.advancedanmu.control.dispatch

import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 11:30
 * @description:
 **/

interface IDispatcher {
    fun dispatch(danmu: Danmu, channels: Array<Channel>)
}