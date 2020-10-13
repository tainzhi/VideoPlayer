package com.tainzhi.android.danmu.advancedanmu.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
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

    private val onDanmuTouchListeners = arrayListOf<OnDanmuTouchListener>()

    var onDanmuContainerViewListener: OnDanmuContainerViewListener? = null

    private var drawFinished = false

    private val lock = Object()

    init {
        danmuController.prepare()
    }

    override fun add(danmu: Danmu) {
        add(-1, danmu)
    }

    override fun add(index: Int, danmu: Danmu) {
        if (danmu.enableTouch) {
            onDanmuTouchListeners.add(danmu)
        }
        danmuController.addDanmu(index, danmu)
    }
    
    
    override fun jumpQueue(danmus: List<Danmu>) {
    }
    
    @SuppressLint("ObsoleteSdkInt")
    override fun lockDraw() {
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
        danmuController.hideAll(hideAll)
    }

    override fun hideNormalDanmu(hide: Boolean) {
    }

    override fun hasCanTouchDanmu(): Boolean {
        return onDanmuTouchListeners.isNotEmpty()
    }

    override fun release() {
        onDanmuTouchListeners.clear()
        danmuController.release()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        detachHasCanTouchDanmu()
        danmuController.run {
            initChannels(canvas)
            draw(canvas)
        }
        unLockDraw()
    }

    /**
     * 去除那些不活跃的Danmu的监听事件
     */
    private fun detachHasCanTouchDanmu() {
        onDanmuTouchListeners.removeAll {
            !(it as Danmu).isAlive
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (hasCanTouchDanmu()) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_UP -> {
                onDanmuTouchListeners.forEach {
                    if (it.onTouch(event.x, event.y)) {
                        val danmu = it as Danmu
                        danmu.touchCallback?.invoke(danmu)
                        // 该事件被单个Danmu消耗
                        return true
                    }
                }
                if (!hasCanTouchDanmu()) {
                    onDanmuContainerViewListener?.callBack()
                }
            }
            MotionEvent.ACTION_POINTER_DOWN, ACTION_MOVE -> {
                // do nothing
            }

        }
        return true
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