package com.tanzhi.android.danmu.advancedanmu.control

import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:38
 * @description:
 **/

class Consumer(private val consumedPool: ConsumedPool, danmuView: IDanmu) : Thread() {
    companion object {
        const val SLEEP_TIME = 100L
    }

    var forceSleep = false
    var isStart = true

    @Volatile
    lateinit var danmuViewReference: WeakReference<IDanmu>
    private val lock = ReentrantLock()

    fun consume(canvas: Canvas) {
        consumedPool.draw(canvas)
    }

    fun release() {
        danmuViewReference.clear()
        isStart = false
        interrupt()
    }

    override fun run() {
        super.run()
        while(isStart) {
            if (consumedPool.isDrawnQueueEmpty() or forceSleep) {
                try {
                    Thread.sleep(SLEEP_TIME)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            } else {
                lock.lock()
                try {
                    danmuViewReference.get()?.lockDraw()
                } finally {
                    lock.unlock()
                }
            }
        }
    }
}