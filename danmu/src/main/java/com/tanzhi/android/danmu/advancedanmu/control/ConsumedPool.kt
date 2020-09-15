package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import com.tanzhi.android.danmu.advancedanmu.painter.DanmuPainter

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
    
    private var isDrawing = false`
}