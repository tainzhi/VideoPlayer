package com.tainzhi.android.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/13 下午6:49
 * @description: 自动走马灯滚动的TextView
 *
 * TextView实现滚动, 需要
 * 1.文字长度长于可显示范围：android:singleLine="true"
 * 2.设置可滚到，或显示样式：android:ellipsize="marquee"
 * 3.TextView只有在获取焦点后才会滚动显示隐藏文字，因此需要在包中新建一个类，
 *   继承TextView。重写isFocused方法，这个方法默认行为是，
 *   如果TextView获得焦点，方法返回true，失去焦点则返回false
 *   跑马灯效果估计也是用这个方法判断是否获得焦点，所以把它的返回值始终设置为true。
 *
 *
 *   当然也可以直接使用 TextView, 并 textView.requestFocus(), textView.isSelected = true
 *
 * 实现参考: [https://www.cnblogs.com/McCa/p/4505211.html]
 *
 **/

class AutoScrollHorizontalView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 只有获取到焦点后, 才能自动滚动
    // true 表示获取到焦点, 一直获取到焦点
    override fun isFocused(): Boolean {
        return true
    }
}