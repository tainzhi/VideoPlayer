package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.Danmu
import com.tanzhi.android.danmu.advancedanmu.control.dispatch.DanmuDispatcher
import com.tanzhi.android.danmu.advancedanmu.control.dispatch.IDispatcher
import com.tanzhi.android.danmu.dpToPx
import java.util.concurrent.locks.ReentrantLock

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:40
 * @description:
 **/

class ProducedPool(val context: Context) {
    companion object {
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    
    private val reentrantLock = ReentrantLock()
    var dispatcher: IDispatcher = DanmuDispatcher(context)
    private var channels: Array<Channel>? = null
    
    @Volatile
    var mixedDanmuPendingQueue = mutableListOf<Danmu>()
    
    @Volatile
    var fastDanmuPendingQueue = mutableListOf<Danmu>()
    
    fun addDanmu(index: Int, danmu: Danmu) {
        reentrantLock.lock()
        try {
            if (index > -1) {
                mixedDanmuPendingQueue.add(index, danmu)
            } else {
                mixedDanmuPendingQueue.add(danmu)
            }
        } finally {
            reentrantLock.unlock()
        }
    }
    
    @Synchronized
    fun dispatch(): List<Danmu>? {
        if (fastDanmuPendingQueue.isEmpty() && mixedDanmuPendingQueue.isEmpty()) return null
        val danmus =
            if (fastDanmuPendingQueue.size > 0) fastDanmuPendingQueue else mixedDanmuPendingQueue
        val validateDanmuViews = mutableListOf<Danmu>()
        val dispatchCount =
            if (danmus.size > MAX_COUNT_IN_SCREEN) MAX_COUNT_IN_SCREEN else danmus.size
        for (i in 0 until dispatchCount) {
            dispatcher.dispatch(danmus[i], channels!!)
            validateDanmuViews.add(danmus[i])
        }
        danmus.drop(dispatchCount)
        if (validateDanmuViews.size > 0) return validateDanmuViews else return null
    }
    
    fun jumpQueue(danmus: List<Danmu>) {
        reentrantLock.lock()
        try {
            fastDanmuPendingQueue.addAll(danmus)
        } finally {
            reentrantLock.unlock()
        }
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
    
    fun clear() {
        fastDanmuPendingQueue.clear()
        mixedDanmuPendingQueue.clear()
    }
    
    
}