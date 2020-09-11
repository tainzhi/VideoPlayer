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

class DanmuPoolManager(context: Context, danmuView: IDanmu) {

    private val danmuProducer = DanmuProducer()
    private val danmuProducerPool = DanmuProducedPool()
    private val danmuConsumer = DanmuConsumer()
    private val danmuConsumedPool = DanmuConsumedPool()

    fun setSpeedController(speedController: ISpeedController) {

    }

    fun addDanmuView(index: Int, danmuView: DanmuModel) {

    }


    fun start() {}

    fun jumpQueue(danmuViews: List<DanmuModel>){

    }

    fun divide(width: Int, height: Int){}

    fun setDispatcher(dispatcher: IDispatcher){}

    fun addPainter(danmuPainter: DanmuPainter, key: Int) {}

    fun drawDanmus(canvas: Canvas) {}

    fun hide(hide: Boolean){}

    fun hideAll(hideAll: Boolean) {}


}