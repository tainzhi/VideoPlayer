package com.tainzhi.android.danmu.advancedanmu.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.Controller

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/22 11:03
 * @description: 使用SurfaceView绘制每一条Danmu
 **/

class DanmuContainerSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), IDanmuContainer, SurfaceHolder.Callback {
    
    private var isSurfaceCreated = false
    private val controller = Controller(context, this)
    
    init {
        holder.addCallback(this)
    }
    
    private fun prepare(canvas: Canvas) {
        controller.run {
            prepare()
            initChannels(canvas)
        }
    }

    override fun add(danmu: Danmu) {
        add(-1, danmu)
    }
    
    override fun add(index: Int, danmu: Danmu) {
        controller.addDanmu(index, danmu)
    }
    
    override fun jumpQueue(danmus: List<Danmu>) {
        controller.jumpQueue(danmus)
    }
    
    override fun lockDraw() {
        if (!isSurfaceCreated) return
        val canvas = holder.lockCanvas() ?: return
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        controller.draw(canvas)
        holder.unlockCanvasAndPost(canvas)
        
    }
    
    override fun forceSleep() {
    }
    
    override fun hideAllDanmu(hideAll: Boolean) {
        controller.hideAll(hideAll)
    }
    
    override fun hideNormalDanmu(hide: Boolean) {
    }
    
    override fun hasCanTouchDanmu(): Boolean {
        return false
    }
    
    override fun surfaceCreated(holder: SurfaceHolder?) {
        isSurfaceCreated = true
        val canvas = holder?.lockCanvas() ?: return
        prepare(canvas)
        holder.unlockCanvasAndPost(canvas)
    }
    
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        isSurfaceCreated = false
    }
}