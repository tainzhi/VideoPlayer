package com.tanzhi.qmediaplayer.floatwindow

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageButton
import com.tanzhi.qmediaplayer.R
import com.tanzhi.qmediaplayer.Util
import com.tanzhi.qmediaplayer.VideoView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/6 12:06
 * @description:
 **/

class FloatWindow(val context: Context) {
    var mWidth = 0.3
    var mHeight = 0.4
    var mX = 0.1
    var mY = 0.2
    var mGravity = Gravity.BOTTOM
    var moveType = MoveType.back
    private lateinit var floatView: FloatView
    private lateinit var valueAnimator: ValueAnimator
    private lateinit var floatLifecycle: FloatLifecycle
    private var isShow = false
    private var floatInit = false

    private val touchListener = object: View.OnTouchListener {
        var lastX = 0f
        var lastY = 0f
        var changeX = 0f
        var changeY = 0f
        var newX = 0f
        var newY = 0f
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                    cancelAnimator()
                }
                MotionEvent.ACTION_MOVE -> {
                    changeX = event.rawX - lastX
                    // TODO: 2020/7/7 为什么不是 event.rayY - lastY 
                    changeY = - event.rawY + lastY
                    newX = floatView.x + changeX
                    newY = floatView.y + changeY
                    floatView.x = newX.toInt()
                    floatView.y = newY.toInt()
                    floatView.updateLayout()
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    when(moveType) {
                        MoveType.slide -> {
                            val startX = floatView.x
                            val endX = if (startX * 2 + v.width > Util.getScreenWidthAndHeight(context).x) Util.getScreenWidthAndHeight(context).x - v.width else 0
                            valueAnimator = ObjectAnimator.ofInt(startX, endX)
                            valueAnimator.addUpdateListener {
                                floatView.x = it.animatedValue as Int
                                floatView.updateLayout()
                            }
                            startAnimator()
                        }
                        MoveType.back -> {
                            val pvhX = PropertyValuesHolder.ofInt("x", floatView.x, Util.getScreenWidthAndHeight(context).x)
                            val pvhY = PropertyValuesHolder.ofInt("y", floatView.y, Util.getScreenWidthAndHeight(context).y)
                            valueAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY)
                            valueAnimator.addUpdateListener {
                                floatView.x  = it.getAnimatedValue("x") as Int
                                floatView.y = it.getAnimatedValue("y") as Int
                                floatView.updateLayout()
                            }
                            startAnimator()
                        }
                    }
                }
            }
            return true
        }
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.float_window, null)
        view.findViewById<VideoView>(R.id.floatWindowVideoView).setOnTouchListener(touchListener)
        view.findViewById<ImageButton>(R.id.floatWindowPlayPauseBtn).setOnClickListener {
            // TODO: 2020/7/7
        }
        view.findViewById<ImageButton>(R.id.floatWindowFullScreenBtn).setOnClickListener {
            dismiss()
        }
        // TODO: 2020/7/7
        // view.findViewById<SeekBar>(R.id.floatWindowProgressBar).setOnSeekBarChangeListener()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            floatView = FloatPhone(context, view)
        } else {
            floatView = FloatToast(context, view)
        }
        floatView.run {
            x = (mX * Util.getScreenWidthAndHeight(context).x).toInt()
            y = (mY * Util.getScreenWidthAndHeight(context).y).toInt()
            width = (mWidth * Util.getScreenWidthAndHeight(context).x).toInt()
            height = (mHeight * Util.getScreenWidthAndHeight(context).y).toInt()
            gravity = mGravity
        }
        floatLifecycle = FloatLifecycle(context.applicationContext, true, null, object: LifecycleListener {
            override fun onShow() {
                show()
            }

            override fun onHide() {
                hide()
            }

            override fun onPostHide() {
                postHide()
            }
        })
    }

    // 显示悬浮窗
    // 如果是第一次展示, 那么显示就是第一次显示
    // 否则就是隐藏之后的显示
    fun show() {
        if (!isShow) {
            if (!floatInit) {
                floatView.show()
                floatInit = true
            } else {
                floatView.visible = true
            }
            isShow = true
        }
    }

    // 删除悬浮窗
    fun dismiss() {
        floatView.dismiss()
        isShow = false
        floatLifecycle.unregister()
    }

    // 隐藏悬浮窗
    private fun hide() {
        if (isShow) {
            floatView.visible = false
            isShow = false
        }
    }

    private fun postHide() {
        floatView.postHide()
        isShow = false
    }

    private fun startAnimator() {
        if (this::valueAnimator.isInitialized) {
            valueAnimator.interpolator = BounceInterpolator()
            valueAnimator.addUpdateListener {
                it.removeAllUpdateListeners()
                it.removeAllListeners()
            }
            valueAnimator.setDuration(500).start()
        }
    }

    private fun cancelAnimator() {
        if (this::valueAnimator.isInitialized && valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
    }

}

object MoveType {
    const val slide = 0
    const val back = 1
}