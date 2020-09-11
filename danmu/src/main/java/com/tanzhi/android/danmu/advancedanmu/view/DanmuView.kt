package com.tanzhi.android.danmu.advancedanmu.view

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tanzhi.android.danmu.advancedanmu.control.DanmuController
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 19:47
 * @description:
 **/

class DanmuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IDanmu {

    private val danmuController = DanmuController(context, this)
    private val onDanmuViewTouchListeners = mutableListOf<OnDanmuViewTouchListener>()
    private val onDanmuParentViewTouchCallbackListener: OnDanmuParentViewTouchCallbackListener? = null

    private var drawFinished = false

    private val lock = Object()

    fun prepare() {
        danmuController.setSpeedController()
        danmuController.prepare()
    }

    override fun add(DanmuModel: DanmuModel) {
        DanmuModel.isMoving = true
        danmuController.addDanmuView(-1, DanmuModel)
    }

    override fun add(index: Int, DanmuModel: DanmuModel) {
        TODO("Not yet implemented")
    }


    override fun jumpQueue(danmuViews: List<DanmuModel>) {
        TODO("Not yet implemented")
    }

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
                    Log.e(TAG, e.message)
                }
            }
            drawFinished = false
        }
    }

    override fun forceSleep() {
    }

    override fun hideAllDanmuView(hideAll: Boolean) {
    }

    override fun hideNormalDanmu(hide: Boolean) {
    }

    override fun hasCanTouchDanmus(): Boolean {
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
        const val TAG = "DanmuView"
    }
}