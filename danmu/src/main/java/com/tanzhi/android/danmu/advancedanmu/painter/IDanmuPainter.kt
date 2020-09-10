package com.tanzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.DanmuChannel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 14:57
 * @description:
 **/

interface IDanmuPainter {
    fun execute(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel)

    fun requestLayout()

    fun setAlpha(alpha: Int)

    fun hideNormal(hide: Boolean)

    fun hideAll(hideAll: Boolean)
}