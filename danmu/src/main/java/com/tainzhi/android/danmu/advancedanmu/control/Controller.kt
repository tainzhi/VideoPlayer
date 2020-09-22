package com.tainzhi.android.danmu.advancedanmu.control

import android.content.Context
import android.graphics.Canvas
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.dispatch.DanmuDispatcher
import com.tainzhi.android.danmu.advancedanmu.control.speed.RandomSpeedController
import com.tainzhi.android.danmu.advancedanmu.view.IDanmuContainer

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 20:44
 * @description:
 **/

class Controller(context: Context, danmuContainerView: IDanmuContainer) {
    private val danmuPoolManager = PoolManager(context, danmuContainerView)
    private val danmuDispatcher = DanmuDispatcher()
    private val speedController = RandomSpeedController()
    var isChannelCreated = false
    
    init {
        danmuPoolManager.setDispatcher(danmuDispatcher)
    }
    
    fun prepare() {
        danmuPoolManager.start()
    }
    
    fun addDanmu(index: Int, danmu: Danmu) {
        danmuPoolManager.addDanmu(index, danmu)
    }
    
    fun jumpQueue(danmus: List<Danmu>) {
        danmuPoolManager.jumpQueue(danmus)
    }
    
    fun initChannels(canvas: Canvas) {
        if (!isChannelCreated) {
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