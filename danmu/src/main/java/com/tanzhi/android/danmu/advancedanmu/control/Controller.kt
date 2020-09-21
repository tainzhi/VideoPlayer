package com.tanzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import com.tanzhi.android.danmu.advancedanmu.control.dispatch.DanmuDispatcher
import com.tanzhi.android.danmu.advancedanmu.control.speed.RandomSpeedController
import com.tanzhi.android.danmu.advancedanmu.view.IDanmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 20:44
 * @description:
 **/

class Controller(context: Context, danmuView: IDanmu) {
    private val danmuPoolManager = PoolManager(context, danmuView)
    private val danmuDispatcher = DanmuDispatcher(context)
    private val speedController = RandomSpeedController()
    var isChannelCreated = false
    
    init {
        danmuPoolManager.setDispatcher(danmuDispatcher)
    }
    
    fun prepare() {
        danmuPoolManager.start()
    }

    fun adddanmuView(index: Int, DanmuModel: DanmuModel) {
        danmuPoolManager.addDanmuView(index, DanmuModel)
    }

    fun jumpQueue(DanmuModels: List<DanmuModel>) {
        danmuPoolManager.jumpQueue(DanmuModels)
    }

    fun initChannels(canvas: Canvas) {
        if (isChannelCreated) {
            speedController.width = canvas.width
            danmuPoolManager.run {
                setSpeedController(speedController)
                divide(canvas.width, canvas.height)
            }
            isChannelCreated = true
        }
    }

    fun draw(canvas: Canvas) {
        danmuPoolManager.drawDanmus(canvas)
    }

}