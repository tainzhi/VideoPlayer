package com.tanzhi.android.danmu.advancedanmu.view

import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:54
 * @description:
 **/

interface IDanmu {
    fun add(DanmuModel: DanmuModel)
    fun add(index: Int, DanmuModel: DanmuModel)
    fun jumpQueue(danmuViews: List<DanmuModel>)
    fun lockDraw()
    fun forceSleep()
    fun hideAlldanmuView(hideAll: Boolean)
    fun hideNormalDanmu(hide: Boolean)
    fun hasCanTouchDanmus(): Boolean

}