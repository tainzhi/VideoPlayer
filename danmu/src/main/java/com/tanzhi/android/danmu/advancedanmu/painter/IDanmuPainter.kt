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

abstract class IDanmuPainter {
    abstract fun execute(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel)

    abstract fun requestLayout()

    var hide: Boolean = false
    var hideAll: Boolean = false

    var alpha = 0f
}