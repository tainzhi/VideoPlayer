package com.qfq.tainzhi.videoplayer.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
// REFACTOR: 2019/6/10 待重构 添加behavior
class BottomNavigationBehavior constructor(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View?>(context, attrs) {
    private var outAnimator: ObjectAnimator? = null
    private var inAnimator: ObjectAnimator? = null
    
    // 垂直滑动
    public override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: View?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
    
    public override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout?, child: View?, target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        if (dy > 0) { // 上滑隐藏
            if (outAnimator == null) {
                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0f, child.getHeight().toFloat())
                outAnimator.setDuration(200)
            }
            if (!outAnimator.isRunning() && child.getTranslationY() <= 0) {
                outAnimator.start()
            }
        } else if (dy < 0) { // 下滑显示
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.getHeight().toFloat(), 0f)
                inAnimator.setDuration(200)
            }
            if (!inAnimator.isRunning() && child.getTranslationY() >= child.getHeight()) {
                inAnimator.start()
            }
        }
    }
}