package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import com.tanzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tanzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tanzhi.android.danmu.advancedanmu.painter.L2RPainter
import com.tanzhi.android.danmu.advancedanmu.painter.R2LPainter
import com.tanzhi.android.danmu.dpToPx

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:39
 * @description:
 **/

class ConsumedPool(val context: Context) {
    
    companion object {
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    val danmuPainterMap = hashMapOf<Int, DanmuPainter>()
    @Volatile
    var mixedDanmuViewQueue = arrayListOf<DanmuModel>()

    var speedController: ISpeedController? = null

    private var channels: Array<Channel>? = null

    private var isDrawing: Boolean = false

    init {
        danmuPainterMap[DanmuModel.LEFT_TO_RIGHT] = L2RPainter()
        danmuPainterMap[DanmuModel.RIGHT_TO_LEFT] = R2LPainter()
        hide(false)
    }

    fun addPainter(danmuPainter: DanmuPainter, key: Int) {
        if (!danmuPainterMap.containsKey(key)) {
            danmuPainterMap.put(key, danmuPainter)
        }
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

    fun isDrawnQueueEmpty(): Boolean =
        if (mixedDanmuViewQueue.isEmpty()) {
            isDrawing = false
            true
        } else {
            false
        }


    fun put(danmuModels: List<DanmuModel>) {
        mixedDanmuViewQueue.addAll(danmuModels)
    }

    fun draw(canvas: Canvas) {
        drawEveryElement(mixedDanmuViewQueue, canvas)
    }

    @Synchronized
    private fun drawEveryElement(danmuModels: List<DanmuModel>, canvas: Canvas) {
        isDrawing = true
        if (danmuModels.isNullOrEmpty()) return
        val maxCount = if (danmuModels.size > MAX_COUNT_IN_SCREEN) MAX_COUNT_IN_SCREEN else danmuModels.size
        for (i in 0 until maxCount) {
            val danmuModel = danmuModels[i]
            if (danmuModel.isAlive) {
                // TODO: 2020/9/18 为什么i-- 
            } else
                i--
        }
        isDrawing = false
    }

    fun divide(width: Int, height: Int) {
        val singleHeight = context.dpToPx<Int>(DEFAULT_SIGNAL_CHANNEL_HEIGHT)
        val count = height / singleHeight
        channels = Array<Channel>(count){ Channel() }
        for (i in 0 until count) {
            channels!![i].run {
                this.width = width
                this.height = singleHeight
                this.topY = i * singleHeight
            }
        }
    }
}