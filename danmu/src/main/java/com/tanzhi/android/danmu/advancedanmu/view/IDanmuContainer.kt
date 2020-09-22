package com.tanzhi.android.danmu.advancedanmu.view

import com.tanzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:54
 * @description:
 **/

interface IDanmuContainer {
    fun add(danmu: Danmu)
    fun add(index: Int, danmu: Danmu)
    fun jumpQueue(danmus: List<Danmu>)
    fun lockDraw()
    fun forceSleep()
    fun hideAllDanmu(hideAll: Boolean)
    fun hideNormalDanmu(hide: Boolean)
    fun hasCanTouchDanmu(): Boolean
    
}