package com.tainzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 14:57
 * @description:
 **/

abstract class IDanmuPainter {
    abstract fun execute(canvas: Canvas, danmu: Danmu, channel: Channel)

    abstract fun requestLayout()

    var hide: Boolean = false
    var hideAll: Boolean = false

    var alpha = 0f
}