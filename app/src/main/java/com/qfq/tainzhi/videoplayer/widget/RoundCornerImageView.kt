package com.qfq.tainzhi.videoplayer.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by muqing on 2019/6/14.
 * Email: qfq61@qq.com
 */
class RoundCornerImageView : ImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(
            context: Context?, attrs: AttributeSet?,
            defStyle: Int) : super(context, attrs, defStyle) {
    }
    
    override fun onDraw(canvas: Canvas?) {
        val clipPath: Path? = Path()
        val w: Int = getWidth()
        val h: Int = getHeight()
        clipPath.addRoundRect(RectF(0, 0, w, h), 10.0f, 10.0f, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}