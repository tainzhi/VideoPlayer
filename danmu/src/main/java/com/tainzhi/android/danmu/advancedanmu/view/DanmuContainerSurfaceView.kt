package com.tainzhi.android.danmu.advancedanmu.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
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
    private val onDanmuTouchListeners = arrayListOf<OnDanmuTouchListener>()

    var onDanmuContainerViewListener: OnDanmuContainerViewListener? = null

    init {
        holder.addCallback(this)
    }

    /**
     * 不断地循环删除不活跃的Danmu点击事件
     */
    private val detachRunnable = object : Runnable {
        override fun run() {
            detachHasCanTouchDanmu()
            handler.postDelayed(this, 100)
        }
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
        if (danmu.enableTouch) {
            onDanmuTouchListeners.add(danmu)
        }
        controller.addDanmu(index, danmu)
    }

    override fun release() {
        onDanmuTouchListeners.clear()
        controller.release()
        handler.removeCallbacks(detachRunnable)
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

    override fun hasCanTouchDanmu() = onDanmuTouchListeners.isNotEmpty()

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
                handler.removeCallbacks(detachRunnable)
                handler.post(detachRunnable)
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
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE -> {
                // do nothing
            }

        }
        return true
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