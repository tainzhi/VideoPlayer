package com.tanzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 16:51
 * @description:
 **/

open class DanmuPainter: IDanmuPainter() {

    companion object {
        val paint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
            strokeWidth = 3.5f
        }
        val rect = Rect()
    }

    open fun layout(danmuModel: DanmuModel, channel: Channel) { }

    private fun onLayout(danmuModel: DanmuModel, channel: Channel) {
        if (danmuModel.isMoving) {
            layout(danmuModel, channel)
        }
    }

    protected fun draw(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        danmuModel.run {
            text?.let { drawText(canvas, danmuModel, channel) }
            textBackground?.let { drawTextBackground(canvas, danmuModel, channel) }
            avatar?.let { drawAvatar(canvas, danmuModel, channel) }
            avatarStrokes?.let { drawTextAvatarStrokes(canvas, danmuModel, channel) }
            levelBitmap?.let { drawLevel(canvas, danmuModel, channel) }
            levelText?.let { drawLevelText(canvas, danmuModel, channel) }
        }

    }

    protected fun drawAvatar(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val top = danmuModel.position.x + danmuModel.height / 2 -
                danmuModel.avatarHeight / 2
        val x = danmuModel.position.x + danmuModel.marginLeft
        rect.set(x, top,
                (x + danmuModel.avatarWidth),
                top + danmuModel.avatarHeight
        )
        canvas.drawBitmap(danmuModel.avatar!!, null, rect, paint)
    }


    protected fun drawTextAvatarStrokes(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val x = danmuModel.position.x + danmuModel.marginLeft + danmuModel.avatarWidth / 2
        val top = danmuModel.position.y + channel.height / 2

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(x.toFloat(), top.toFloat(), (danmuModel.avatarHeight / 2).toFloat(), paint)
    }

    protected fun drawLevel(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val top = danmuModel.position.y + channel.height / 2 - danmuModel.levelBitmapHeight / 2
        val x = danmuModel.position.x +
                danmuModel.marginLeft +
                danmuModel.avatarWidth +
                danmuModel.levelBitmapMarginLeft
        rect.set(x, top,
                x + danmuModel.levelBitmapWidth,
                top + danmuModel.levelBitmapHeight
        )
        canvas.drawBitmap(danmuModel.levelBitmap!!, null, rect, paint)
    }

    protected fun drawLevelText(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val levelText = danmuModel.levelText ?: return
        paint.run {
            textSize = danmuModel.levelTextSize
            color = danmuModel.levelTextColor
            style = Paint.Style.FILL
        }
        val top = danmuModel.position?.y +
                danmuModel.height / 2 -
                paint.ascent() / 2 -
                paint.descent() / 2
        val x = danmuModel.position.x +
                danmuModel.marginLeft +
                danmuModel.avatarWidth +
                danmuModel.levelBitmapMarginLeft +
                danmuModel.levelBitmapWidth / 2
        canvas.drawText(levelText as String, x.toFloat(), top, paint)
    }

    protected fun drawText(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val text = danmuModel.text ?: return
        paint.run {
            textSize = danmuModel.textSize
            color = danmuModel.textColor
            style = Paint.Style.FILL
        }
        val staticLayout = StaticLayout.Builder.obtain(
                text, 0, text.length, paint, StaticLayout.getDesiredWidth(text, paint).toInt())
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
        val top = danmuModel.position.y +
                channel.height / 2 -
                staticLayout.height / 2
        val x = danmuModel.position.x +
                danmuModel.marginLeft +
                danmuModel.avatarWidth +
                danmuModel.levelBitmapMarginLeft +
                danmuModel.textMarginLeft
        canvas.save()
        canvas.translate(x.toFloat(), top.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }

    protected fun drawTextBackground(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        val text = danmuModel.text ?: return
        val staticLayout = StaticLayout.Builder.obtain(
                text, 0, text.length, paint, StaticLayout.getDesiredWidth(text, paint).toInt())
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
        // FIXME: 2020/9/10 delete this
        // val staticLayout = StaticLayout(
        //         text, paint, ceil(StaticLayout.getDesiredWidth(text, paint).toDouble()).toInt(),
        //         Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
        //
        // )
        val textBackgroundHeight = staticLayout.height + danmuModel.textBackgroundPadding!!.top + danmuModel.textBackgroundPadding!!.bottom
        val top = danmuModel.position.y + (channel.height - textBackgroundHeight) / 2
        val x = danmuModel.position.x +
                danmuModel.marginLeft +
                danmuModel.avatarWidth -
                danmuModel.textBackgroundMarginLeft
        rect.set(x,
                top,
                (x + danmuModel.levelBitmapMarginLeft +
                        danmuModel.levelBitmapWidth +
                        danmuModel.textMarginLeft +
                        danmuModel.textBackgroundMarginLeft +
                        staticLayout.width +
                        danmuModel.textBackgroundPadding!!.right),
                top + textBackgroundHeight)

        danmuModel.textBackground?.bounds = rect
        danmuModel.textBackground?.draw(canvas)
    }

    override fun execute(canvas: Canvas, danmuModel: DanmuModel, channel: Channel) {
        if (danmuModel.speed == 0f) {
            danmuModel.isAlive = false
        }
        onLayout(danmuModel, channel)
        if (hideAll) return
        // 只隐藏用户级别的弹幕
        // 系统级弹幕不隐藏
        if (danmuModel.priority == DanmuModel.PRIORITY_NORMAL && hide) {
            return
        }
        draw(canvas, danmuModel, channel)
    }

    override fun requestLayout() {
    }

}