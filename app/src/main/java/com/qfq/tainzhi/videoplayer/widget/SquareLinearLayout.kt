package com.qfq.tainzhi.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
class SquareLinearLayout : LinearLayout {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec: Int = widthMeasureSpec
        var heightMeasureSpec: Int = heightMeasureSpec
        setMeasuredDimension(View.getDefaultSize(0, widthMeasureSpec),
                             View.getDefaultSize(0, heightMeasureSpec))
        val childWidthSize: Int = getMeasuredWidth()
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY)
        heightMeasureSpec = widthMeasureSpec
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}