package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import com.tanzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tanzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tanzhi.android.danmu.advancedanmu.painter.L2RPainter
import com.tanzhi.android.danmu.advancedanmu.painter.R2LPainter

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:39
 * @description:
 **/

class ConsumedPool(context: Context) {
    
    companion object {
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    val danmuPainterMap = hashMapOf<Int, DanmuPainter>()
    @Volatile
    var mixedDanmuViewQueue = arrayListOf<DanmuModel>()

    var speedController: ISpeedController? = null
    
    private var isDrawing: Boolean = false

    init {
        danmuPainterMap[DanmuModel.LEFT_TO_RIGHT] = L2RPainter()
        danmuPainterMap[DanmuModel.RIGHT_TO_LEFT] = R2LPainter()
        hide(false)
    }


    private fun hide(hide: Boolean) {
        danmuPainterMap.forEach { (_, u) ->
            u.hide = hide
        }
    }

    private fun hideAll(hide: Boolean) {
        danmuPainterMap.forEach { (_, u) ->
            u.hideAll = hide
        }
    }
}