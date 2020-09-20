package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import com.tanzhi.android.danmu.advancedanmu.control.dispatch.IDispatcher
import com.tanzhi.android.danmu.advancedanmu.control.speed.ISpeedController
import com.tanzhi.android.danmu.advancedanmu.painter.DanmuPainter
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:37
 * @description:
 **/

class DanmuPoolManager(val context: Context, val danmuView: IDanmu) {

    private val danmuProducerPool = ProducedPool(context)
    private val danmuConsumedPool = ConsumedPool(context)
    private val danmuProducer = Producer(danmuProducerPool, danmuConsumedPool)
    private val danmuConsumer = Consumer(danmuConsumedPool, danmuView)

    var isStart = false

    fun setSpeedController(speedController: ISpeedController) {

    }

    fun divide(width: Int, height: Int) {
        danmuProducerPool.divide(width, height)
        danmuConsumedPool.divide(width, height)
    }

    fun addDanmuView(index: Int, danmuView: DanmuModel) {
        danmuProducer.produce(index, danmuView)
    }


    fun start() {
        if (!isStart) {
            isStart = false
            danmuProducer.start()
            danmuConsumer.start()
        }
    }

    fun jumpQueue(danmuViews: List<DanmuModel>){
        danmuProducer.jumpQueue(danmuViews)
    }

    fun setDispatcher(dispatcher: IDispatcher){
        danmuProducerPool.dispatcher = dispatcher
    }

    fun addPainter(danmuPainter: DanmuPainter, key: Int) {
        danmuConsumedPool.addPainter(danmuPainter, key)
    }

    fun drawDanmus(canvas: Canvas) {
        danmuConsumer.consume(canvas)
    }

    fun hide(hide: Boolean){}

    fun hideAll(hideAll: Boolean) {}

    fun release() {
        isStart = false
    }

}