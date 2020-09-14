package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import java.util.concurrent.locks.ReentrantLock

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:40
 * @description:
 **/

class DanmuProducedPool(context: Context) {
    companion object {
        const val MAX_COUNT_IN_SCREEN = 30
        const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 40
    }
    
    private val reentrantLock = ReentrantLock()
    private val applicationContext = context.applicationContext
    
    @Volatile
    var mixedDanmuPendingQueue = mutableListOf<DanmuModel>()
    @Volatile
    var fastDanmuPendingQueue = mutableListOf<DanmuModel>()
    
    fun addDanmu(index: Int, danmu: DanmuModel) {
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
    
    fun jumpQueue(danmus: List<DanmuModel>) {
        reentrantLock.lock()
        try {
            fastDanmuPendingQueue.addAll(danmus)
        } finally {
            reentrantLock.unlock()
        }
    }
    
    fun clear() {
        fastDanmuPendingQueue.clear()
        mixedDanmuPendingQueue.clear()
    }
    
    
}