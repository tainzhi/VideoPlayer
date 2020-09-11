package com.tanzhi.android.danmu.advancedanmu.control.dispatch

import com.tanzhi.android.danmu.advancedanmu.DanmuChannel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 11:30
 * @description:
 **/

interface IDispatcher {
    fun dispatch(DanmuModel: DanmuModel, danmuChannels: Array<DanmuChannel>)
}