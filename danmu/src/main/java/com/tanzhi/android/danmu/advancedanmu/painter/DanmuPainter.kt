package com.tanzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.DanmuChannel
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

    protected fun layout(DanmuModel: DanmuModel, danmuChannel: DanmuChannel) { }

    private fun onLayout(DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        if (DanmuModel.isMoving) {
            layout(DanmuModel, danmuChannel)
        }
    }

    protected fun draw(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        DanmuModel.run {
            text?.let { drawText(canvas, DanmuModel, danmuChannel) }
            textBackground?.let { drawTextBackground(canvas, DanmuModel, danmuChannel) }
            avatar?.let { drawAvatar(canvas, DanmuModel, danmuChannel) }
            avatarStrokes?.let { drawTextAvatarStrokes(canvas, DanmuModel, danmuChannel) }
            levelBitmapSize?.let { drawLevel(canvas, DanmuModel, danmuChannel) }
            levelText?.let { drawLevelText(canvas, DanmuModel, danmuChannel) }
        }

    }

    protected fun drawAvatar(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val top = DanmuModel.startPosition.x + DanmuModel.size!!.height / 2 -
                DanmuModel.avatarSize!!.height / 2
        val x = DanmuModel.startPosition.x + DanmuModel.marginLeft
        rect.set(x, top,
                (x + DanmuModel.avatarSize!!.width),
                top + DanmuModel.avatarSize!!.height
        )
        canvas.drawBitmap(DanmuModel.avatar!!, null, rect, paint)
    }


    protected fun drawTextAvatarStrokes(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val x = DanmuModel.startPosition.x + DanmuModel.marginLeft + DanmuModel.avatarSize!!.width / 2
        val top = DanmuModel.startPosition.y + danmuChannel.height / 2

        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(x.toFloat(), top.toFloat(), (DanmuModel.avatarSize!!.height / 2).toFloat(), paint)
    }

    protected fun drawLevel(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val top = DanmuModel.startPosition.y + danmuChannel.height / 2 - DanmuModel.levelBitmapSize!!.height / 2
        val x = DanmuModel.startPosition.x +
                DanmuModel.marginLeft +
                DanmuModel.avatarSize!!.width +
                DanmuModel.levelBitmapMarginLeft
        rect.set(x, top,
                x + DanmuModel.levelBitmapSize!!.width,
                top + DanmuModel.levelBitmapSize!!.height
        )
        canvas.drawBitmap(DanmuModel.levelBitmap!!, null, rect, paint)
    }

    protected fun drawLevelText(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val levelText = DanmuModel.levelText ?: return
        paint.run {
            textSize = DanmuModel.levelTextSize
            color = DanmuModel.levelTextColor
            style = Paint.Style.FILL
        }
        val top = DanmuModel.startPosition?.y +
                DanmuModel.size!!.height / 2 -
                paint.ascent() / 2 -
                paint.descent() / 2
        val x = DanmuModel.startPosition.x +
                DanmuModel.marginLeft +
                DanmuModel.avatarSize!!.width +
                DanmuModel.levelBitmapMarginLeft +
                DanmuModel.levelBitmapSize!!.width / 2
        canvas.drawText(levelText as String, x.toFloat(), top, paint)
    }

    protected fun drawText(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val text = DanmuModel.text ?: return
        paint.run {
            textSize = DanmuModel.textSize
            color = DanmuModel.textColor
            style = Paint.Style.FILL
        }
        val staticLayout = StaticLayout.Builder.obtain(
                text, 0, text.length, paint, StaticLayout.getDesiredWidth(text, paint).toInt())
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
        val top = DanmuModel.startPosition.y +
                danmuChannel.height / 2 -
                staticLayout.height / 2
        val x = DanmuModel.startPosition.x +
                DanmuModel.marginLeft +
                DanmuModel.avatarSize!!.width +
                DanmuModel.levelBitmapMarginLeft +
                DanmuModel.textMarginLeft
        canvas.save()
        canvas.translate(x.toFloat(), top.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }

    protected fun drawTextBackground(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val text = DanmuModel.text ?: return
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
        val textBackgroundHeight = staticLayout.height + DanmuModel.textBackgroundPadding!!.top + DanmuModel.textBackgroundPadding!!.bottom
        val top = DanmuModel.startPosition.y + (danmuChannel.height - textBackgroundHeight) / 2
        val x = DanmuModel.startPosition.x +
                DanmuModel.marginLeft +
                DanmuModel.avatarSize!!.width -
                DanmuModel.textBackgroundMarginLeft
        rect.set(x,
                top,
                (x + DanmuModel.levelBitmapMarginLeft +
                        DanmuModel.levelBitmapSize!!.width +
                        DanmuModel.textMarginLeft +
                        DanmuModel.textBackgroundMarginLeft +
                        staticLayout.width +
                        DanmuModel.textBackgroundPadding!!.right),
                top + textBackgroundHeight)

        DanmuModel.textBackground?.bounds = rect
        DanmuModel.textBackground?.draw(canvas)
    }

    override fun execute(canvas: Canvas, DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        if (DanmuModel.speed == 0f) {
            DanmuModel.isAlive = false
        }
        onLayout(DanmuModel, danmuChannel)
        if (hideAll) return
        // 只隐藏用户级别的弹幕
        // 系统级弹幕不隐藏
        if (DanmuModel.priority == DanmuModel.PRIORITY_NORMAL && hide) {
            return
        }
        draw(canvas, DanmuModel, danmuChannel)
    }

    override fun requestLayout() {
    }

}