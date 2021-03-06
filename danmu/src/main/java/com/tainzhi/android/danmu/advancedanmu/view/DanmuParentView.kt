package com.tainzhi.android.danmu.advancedanmu.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午9:55
 * @description: 弹幕需要处理点击事件, 那用 [DanmuParentView]作为[DanmuContainerView] 或者 [DanmuContainerSurfaceView]的根布局
 **/

class DanmuParentView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView is IDanmuContainer) {
                if ((childView as IDanmuContainer).hasCanTouchDanmu()) {
                    childView.bringToFront()
                    childView.parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    moveChildToBack(childView)
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun moveChildToBack(child: View) {
        val index = indexOfChild(child)
        if (index > 0) {
            detachViewFromParent(index)
            attachViewToParent(child, 0, child.layoutParams)
        }
    }

}