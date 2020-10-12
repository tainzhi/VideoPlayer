package com.tainzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.util.Log
import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Channel.Companion.MAX_COUNT_IN_SCREEN
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.dispatch.IDispatcher
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
        while (dispatchCount > 0) {
            danmus.removeFirst()
            dispatchCount--
        }
        if (validateDanmuViews.size > 0) {
            Log.d(TAG, "dispatch ${validateDanmuViews.size} Danmu")
        }
        return if (validateDanmuViews.size > 0) validateDanmuViews else null
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
        channels = Channel.createChannels(width, height)
    }
    
    fun clear() {
        fastDanmuPendingQueue.clear()
        mixedDanmuPendingQueue.clear()
    }
    
    
}