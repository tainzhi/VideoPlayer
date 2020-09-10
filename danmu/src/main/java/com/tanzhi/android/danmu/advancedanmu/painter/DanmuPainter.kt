package com.tanzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.DanmuChannel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 16:51
 * @description:
 **/

open class DanmuPainter: IDanmuPainter {

    companion object {
        val paint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
            strokeWidth = 3.5f
        }
        val rectF = RectF()
    }

    var hide: Boolean = false
    var hideAll: Boolean = false

    protected fun layout(danmuModel: DanmuModel, danmuChannel: DanmuChannel) { }

    private fun onLayout(danmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        if (danmuModel.isMoving) {
            layout(danmuModel, danmuChannel)
        }
    }

    protected fun draw(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        danmuModel.run {
            textBackground?.let { drawTextBackground(canvas, danmuModel, danmuChannel)}
            avatar?.let { drawAvatar(canvas, danmuModel, danmuChannel)}
        }

    }

    protected fun drawAvatar(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }
    protected fun drawTextAvatarStrokes(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }
    protected fun drawLevel(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }
    protected fun drawLevelText(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }
    protected fun drawText(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }
    protected fun drawTextBackground(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {

    }

    override fun execute(canvas: Canvas, danmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        if (danmuModel.speed == 0f) {
            danmuModel.isAlive = false
        }
        onLayout(danmuModel, danmuChannel)
        if (hideAll) return
        // 只隐藏用户级别的弹幕
        // 系统级弹幕不隐藏
        if (danmuModel.priority == DanmuModel.PRIORITY_NORMAL && hide) {
            return
        }
        draw(canvas, danmuModel, danmuChannel)
    }

    override fun requestLayout() {
    }

    override fun setAlpha(alpha: Int) {
    }

}