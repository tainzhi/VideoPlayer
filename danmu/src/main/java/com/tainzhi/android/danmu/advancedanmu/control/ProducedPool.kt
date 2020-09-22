package com.tainzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.util.Log
import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.dispatch.IDispatcher
import com.tainzhi.android.danmu.dpToPx
import java.util.concurrent.locks.ReentrantLock

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:40
 * @description:
 **/

class ProducedPool(val context: Context) {
    companion object {
        const val TAG = "Danmu.ProducedPool"
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    
    private val reentrantLock = ReentrantLock()
    var dispatcher: IDispatcher? = null
    private var channels: Array<Channel>? = null
    
    @Volatile
    var mixedDanmuPendingQueue = mutableListOf<Danmu>()
    
    @Volatile
    var fastDanmuPendingQueue = mutableListOf<Danmu>()
    
    fun addDanmu(index: Int, danmu: Danmu) {
        Log.d(TAG, "addDanmu")
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
    
    @ExperimentalStdlibApi
    @Synchronized
    fun dispatch(): List<Danmu>? {
        Log.d(TAG, "dispatch")
        if (fastDanmuPendingQueue.isEmpty() && mixedDanmuPendingQueue.isEmpty()) return null
        val danmus =
            if (fastDanmuPendingQueue.size > 0) fastDanmuPendingQueue else mixedDanmuPendingQueue
        val validateDanmuViews = mutableListOf<Danmu>()
        var dispatchCount =
            if (danmus.size > MAX_COUNT_IN_SCREEN) MAX_COUNT_IN_SCREEN else danmus.size
        danmus.forEachIndexed loop@{ index, danmu ->
           if (index >= dispatchCount) return@loop
            dispatcher?.dispatch(danmu, channels!!)
            validateDanmuViews.add(danmu)
        }
        while(dispatchCount > 0) {
            danmus.removeFirst()
            dispatchCount--
        }
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