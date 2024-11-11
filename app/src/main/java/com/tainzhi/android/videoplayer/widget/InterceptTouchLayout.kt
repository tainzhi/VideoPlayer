package com.tainzhi.android.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.tainzhi.android.videoplayer.R
import kotlin.math.abs

/**
 * File:     InterceptTouchLayout
 * Author:   tainzhi
 * Created:  2020/12/17 20:18
 * Mail:     QFQ61@qq.com
 * Description: 用于解决ViewPager2和RecyclerView的滑动冲突
 *
 * 用法如下
 * // ViewPager2水平滑动, Recyclerview竖直滑动, 必须要在 InterceptTouchLayout中添加数值方向
 * <ViewPager2>
 *     <InterceptTouchLayout
 *      android:orientation=VERTICAL
 *      >
 *          <SwipeRefreshLayout>
 *              <RecycleView>
 *          </SwipeRefreshLayout>
 *     </InterceptTouchLayout>
 * </ViewPager2>
 */
class InterceptTouchLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val HORIZONTAL = LinearLayout.HORIZONTAL
        private const val VERTICAL = LinearLayout.VERTICAL
    }

    private var downX = 0f
    private var downY = 0f
    private var isDragged = false

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var orientation = HORIZONTAL

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.InterceptTouchLayout)
            orientation = a.getInt(R.styleable.InterceptTouchLayout_android_orientation, HORIZONTAL)
            a.recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                isDragged = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragged) {
                    val dx = abs(ev.x - downX)
                    val dy = abs(ev.y - downY)
                    if (orientation == HORIZONTAL) {
                        // 如果子 Recyclerview是水平方向, 才不让 父布局 ViewPager2拦截水平滑动
                        isDragged = dx > touchSlop && dx > dy
                    } else if (orientation == VERTICAL) {
                        // 如果子 Recyclerview是竖直方向, 才不让 父布局 ViewPager2拦截竖直滑动
                        isDragged = dy > touchSlop && dy > dx
                        // 让父布局拦截水平滑动ViewPager2水平滑动
                    }
                }
                parent.requestDisallowInterceptTouchEvent(isDragged)
            }
            MotionEvent.ACTION_UP -> {
                isDragged = false
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}