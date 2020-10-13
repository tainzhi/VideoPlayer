package com.tainzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.dispatch.IDispatcher
import com.tainzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tainzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tainzhi.android.danmu.advancedanmu.view.IDanmuContainer

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:37
 * @description:
 **/

class PoolManager(val context: Context, val danmuContainerView: IDanmuContainer) {
    
    private val danmuProducerPool = ProducedPool(context)
    private val danmuConsumedPool = ConsumedPool(context)
    private val danmuProducer = Producer(danmuProducerPool, danmuConsumedPool)
    private val danmuConsumer = Consumer(danmuConsumedPool, danmuContainerView)
    
    var isStart = false
    
    fun setSpeedController(speedController: ISpeedController) {
    
    }
    
    fun divide(width: Int, height: Int) {
        danmuProducerPool.divide(width, height)
        danmuConsumedPool.divide(width, height)
    }
    
    fun addDanmu(index: Int, danmu: Danmu) {
        danmuProducer.produce(index, danmu)
    }
    
    
    fun start() {
        if (!isStart) {
            isStart = true
            danmuProducer.start()
            danmuConsumer.start()
        }
    }
    
    fun jumpQueue(danmus: List<Danmu>) {
        danmuProducer.jumpQueue(danmus)
    }
    
    fun setDispatcher(dispatcher: IDispatcher) {
        danmuProducerPool.dispatcher = dispatcher
    }

    fun addPainter(danmuPainter: DanmuPainter, key: Int) {
        danmuConsumedPool.addPainter(danmuPainter, key)
    }

    fun drawDanmus(canvas: Canvas) {
        danmuConsumer.consume(canvas)
    }

    fun hide(hide: Boolean) {}

    fun hideAll(hideAll: Boolean) {
        danmuConsumedPool.hideAll(hideAll)
    }

    fun release() {
        isStart = false
        danmuConsumer.release()
        danmuConsumedPool.release()
        danmuProducer.release()
        danmuProducerPool.release()
    }
}