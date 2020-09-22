package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.Danmu
import com.tanzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tanzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tanzhi.android.danmu.advancedanmu.painter.IDanmuPainter
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
    
    val danmuPainterMap = hashMapOf<Int, IDanmuPainter>()
    
    @Volatile
    var mixedDanmuViewQueue = arrayListOf<Danmu>()
    
    var speedController: ISpeedController? = null
    
    private val danmuChannels = arrayOf<Channel>()
    
    private var channels: Array<Channel>? = null
    
    private var isDrawing: Boolean = false
    
    init {
        danmuPainterMap[Danmu.LEFT_TO_RIGHT] = L2RPainter()
        danmuPainterMap[Danmu.RIGHT_TO_LEFT] = R2LPainter()
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
    
    
    fun put(danmus: List<Danmu>) {
        mixedDanmuViewQueue.addAll(danmus)
    }
    
    fun draw(canvas: Canvas) {
        drawEveryElement(mixedDanmuViewQueue, canvas)
    }
    
    @Synchronized
    private fun drawEveryElement(danmus: List<Danmu>, canvas: Canvas) {
        isDrawing = true
        if (danmus.isNullOrEmpty()) return
        val maxCount = if (danmus.size > MAX_COUNT_IN_SCREEN) MAX_COUNT_IN_SCREEN else danmus.size
        for (i in 0 until maxCount) {
            val danmu = danmus[i]
            if (danmu.isAlive) {
                val painter = getPainter(danmu)
                val channel = danmuChannels[danmu.channelIndex]
                channel.dispatch(danmu)
                if (danmu.attached) {
                    performDraw(danmu, painter, canvas, channel)
                }
            }
        }
        danmus.drop(maxCount)
        isDrawing = false
    }
    
    private fun getPainter(danmu: Danmu): IDanmuPainter =
        danmuPainterMap[danmu.displayType]!!
    
    private fun performDraw(
        danmu: Danmu,
        danmuPainter: IDanmuPainter,
        canvas: Canvas,
        channel: Channel
    ) {
        danmuPainter.execute(canvas, danmu, channel)
    }
    
    fun divide(width: Int, height: Int) {
        val singleHeight = context.dpToPx<Int>(DEFAULT_SIGNAL_CHANNEL_HEIGHT)
        val count = height / singleHeight
        channels = Array<Channel>(count) { Channel() }
        for (i in 0 until count) {
            channels!![i].run {
                this.width = width
                this.height = singleHeight
                this.topY = i * singleHeight
            }
        }
    }
}