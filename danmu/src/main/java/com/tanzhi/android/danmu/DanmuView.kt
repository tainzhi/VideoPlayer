package com.tanzhi.android.danmu

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/10 16:37
 * @description:
 **/

class DanmuView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var datas: List<DanmuBean> = arrayListOf()
    private val widthPixels = resources.displayMetrics.widthPixels
    private val heightPixels = resources.displayMetrics.heightPixels
    private var curPos = 0

    private val addHandler = @SuppressLint("HandlerLeak")
    object: Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            addTextView()

            val randomDelay = Random.nextLong(300) + 400
            sendEmptyMessageDelayed(0, randomDelay)
        }
    }

    init {
        addHandler.sendEmptyMessage(0)
    }

    /**
     * 清除所有的弹幕
     */
    fun clear() {
        removeAllViews()
    }

    private fun addTextView() {
        // 循环播放
        if (curPos == datas.size) {
            curPos = 0
        }

        val danmuItemView = DanmuItemView(context, datas[curPos++])
        addView(danmuItemView)

        danmuItemView.run {
            randomVerticalPosition(heightPixels)
            startTranslateAnim(this)
        }
    }

    /**
     * 设置从右往左移动动画, 事件范围2000 - 4000ms
     */
    private fun startTranslateAnim(view: View) {
        val anim = TranslateAnimation(widthPixels.toFloat(), -widthPixels.toFloat(), 0F, 0F)
                .apply {
                    duration = Random.nextLong(2000) + 7000
                    setAnimationListener(object: Animation.AnimationListener {
                        override fun onAnimationRepeat(animation: Animation?) {
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            removeView(view)
                        }

                        override fun onAnimationStart(animation: Animation?) {
                        }
                    })
                }
        view.startAnimation(anim)
    }

}