package com.tainzhi.android.danmu.advancedanmu.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.control.Controller

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 19:47
 * @description: 使用View绘制每一条Danmu
 **/

class DanmuContainerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IDanmuContainer {

    private val danmuController = Controller(context, this)

    private var drawFinished = false

    private val lock = Object()


    init {
        danmuController.prepare()

    }

    override fun add(danmu: Danmu) {
        danmu.isMoving = true
        danmuController.addDanmu(-1, danmu)
    }

    override fun add(index: Int, danmu: Danmu) {
    }
    
    
    override fun jumpQueue(danmus: List<Danmu>) {
    }
    
    @SuppressLint("ObsoleteSdkInt")
    override fun lockDraw() {
        // Log.d(TAG, "lockDraw()")
        if (!danmuController.isChannelCreated) return
        synchronized(lock) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation()
            } else {
                postInvalidate()
            }
            if (!drawFinished) {
                try {
                    lock.wait()
                } catch (e: InterruptedException) {
                    Log.e(TAG, e.message ?: "draw danmu encounter error")
                }
            }
            drawFinished = false
        }
    }
    
    override fun forceSleep() {
    }
    
    override fun hideAllDanmu(hideAll: Boolean) {
    }
    
    override fun hideNormalDanmu(hide: Boolean) {
    }
    
    override fun hasCanTouchDanmu(): Boolean {
        return true
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        danmuController.run {
            initChannels(canvas)
            draw(canvas)
        }
        unLockDraw()
    }

    private fun unLockDraw() {
        synchronized(lock) {
            drawFinished = true
            lock.notifyAll()
        }
    }

    companion object {
        const val TAG = "DanmuContainerView"
    }
}