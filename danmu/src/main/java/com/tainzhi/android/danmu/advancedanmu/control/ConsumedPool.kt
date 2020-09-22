package com.tainzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tainzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tainzhi.android.danmu.advancedanmu.painter.IDanmuPainter
import com.tainzhi.android.danmu.advancedanmu.painter.L2RPainter
import com.tainzhi.android.danmu.advancedanmu.painter.R2LPainter
import com.tainzhi.android.danmu.dpToPx

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:39
 * @description:
 **/

class ConsumedPool(val context: Context) {
    
    companion object {
        const val TAG = "Danmu.ConsumedPool"
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    
    val danmuPainterMap = hashMapOf<Int, IDanmuPainter>()
    
    @Volatile
    var mixedDanmuViewQueue = arrayListOf<Danmu>()
    
    var speedController: ISpeedController? = null
    
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
        Log.d(TAG, "put(), danmus.size=${danmus.size}")
        mixedDanmuViewQueue.addAll(danmus)
    }
    
    fun draw(canvas: Canvas) {
        drawEveryElement(mixedDanmuViewQueue, canvas)
    }
    
    @Synchronized
    private fun drawEveryElement(danmus: ArrayList<Danmu>, canvas: Canvas) {
        Log.d(TAG, "drawEveryElement, danmu.size=${danmus.size}")
        isDrawing = true
        if (danmus.isNullOrEmpty()) return
        var maxCount = if (danmus.size > MAX_COUNT_IN_SCREEN) MAX_COUNT_IN_SCREEN else danmus.size
        danmus.forEachIndexed loop@{ index, danmu ->
            if (index >= maxCount) return@loop
            if (danmu.isAlive) {
                val painter = getPainter(danmu)
                val channel = channels?.get(danmu.channelIndex) ?: return
                channel.dispatch(danmu)
                if (danmu.attached) {
                    performDraw(danmu, painter, canvas, channel)
                }
            } else {
                danmu.isDeprecated = true
            }
        }
        // 已经失效的Danmu, 移除ConsumedPool
        danmus.forEach {
            if (it.isDeprecated) danmus.remove(it)
        }
        Log.d(TAG, "drawEveryElement, after, danmu.size=${danmus.size}")
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