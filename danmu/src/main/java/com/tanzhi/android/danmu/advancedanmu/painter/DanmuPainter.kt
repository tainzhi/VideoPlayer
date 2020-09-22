package com.tanzhi.android.danmu.advancedanmu.painter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.Danmu

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
    
    open fun layout(danmu: Danmu, channel: Channel) {}
    
    private fun onLayout(danmu: Danmu, channel: Channel) {
        if (danmu.isMoving) {
            layout(danmu, channel)
        }
    }
    
    protected fun draw(canvas: Canvas, danmu: Danmu, channel: Channel) {
        danmu.run {
            text?.let { drawText(canvas, danmu, channel) }
            textBackground?.let { drawTextBackground(canvas, danmu, channel) }
            avatar?.let { drawAvatar(canvas, danmu, channel) }
            avatarStrokes?.let { drawTextAvatarStrokes(canvas, danmu, channel) }
            levelBitmap?.let { drawLevel(canvas, danmu, channel) }
            levelText?.let { drawLevelText(canvas, danmu, channel) }
        }
        
    }
    
    protected fun drawAvatar(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val top = danmu.position.x + danmu.height / 2 -
                danmu.avatarHeight / 2
        val x = danmu.position.x + danmu.marginLeft
        rect.set(
            x, top,
            (x + danmu.avatarWidth),
            top + danmu.avatarHeight
        )
        canvas.drawBitmap(danmu.avatar!!, null, rect, paint)
    }
    
    
    protected fun drawTextAvatarStrokes(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val x = danmu.position.x + danmu.marginLeft + danmu.avatarWidth / 2
        val top = danmu.position.y + channel.height / 2
        
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(x.toFloat(), top.toFloat(), (danmu.avatarHeight / 2).toFloat(), paint)
    }
    
    protected fun drawLevel(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val top = danmu.position.y + channel.height / 2 - danmu.levelBitmapHeight / 2
        val x = danmu.position.x +
                danmu.marginLeft +
                danmu.avatarWidth +
                danmu.levelBitmapMarginLeft
        rect.set(
            x, top,
            x + danmu.levelBitmapWidth,
            top + danmu.levelBitmapHeight
        )
        canvas.drawBitmap(danmu.levelBitmap!!, null, rect, paint)
    }
    
    protected fun drawLevelText(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val levelText = danmu.levelText ?: return
        paint.run {
            textSize = danmu.levelTextSize
            color = danmu.levelTextColor
            style = Paint.Style.FILL
        }
        val top = danmu.position?.y +
                danmu.height / 2 -
                paint.ascent() / 2 -
                paint.descent() / 2
        val x = danmu.position.x +
                danmu.marginLeft +
                danmu.avatarWidth +
                danmu.levelBitmapMarginLeft +
                danmu.levelBitmapWidth / 2
        canvas.drawText(levelText as String, x.toFloat(), top, paint)
    }
    
    protected fun drawText(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val text = danmu.text ?: return
        paint.run {
            textSize = danmu.textSize
            color = danmu.textColor
            style = Paint.Style.FILL
        }
        val staticLayout = StaticLayout.Builder.obtain(
            text, 0, text.length, paint, StaticLayout.getDesiredWidth(text, paint).toInt()
        )
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .build()
        val top = danmu.position.y +
                channel.height / 2 -
                staticLayout.height / 2
        val x = danmu.position.x +
                danmu.marginLeft +
                danmu.avatarWidth +
                danmu.levelBitmapMarginLeft +
                danmu.textMarginLeft
        canvas.save()
        canvas.translate(x.toFloat(), top.toFloat())
        staticLayout.draw(canvas)
        canvas.restore()
    }
    
    protected fun drawTextBackground(canvas: Canvas, danmu: Danmu, channel: Channel) {
        val text = danmu.text ?: return
        val staticLayout = StaticLayout.Builder.obtain(
            text, 0, text.length, paint, StaticLayout.getDesiredWidth(text, paint).toInt()
        )
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
        val textBackgroundHeight =
            staticLayout.height + danmu.textBackgroundPadding.top + danmu.textBackgroundPadding.bottom
        val top = danmu.position.y + (channel.height - textBackgroundHeight) / 2
        val x = danmu.position.x +
                danmu.marginLeft +
                danmu.avatarWidth -
                danmu.textBackgroundMarginLeft
        rect.set(
            x,
            top,
            (x + danmu.levelBitmapMarginLeft +
                    danmu.levelBitmapWidth +
                    danmu.textMarginLeft +
                    danmu.textBackgroundMarginLeft +
                    staticLayout.width +
                    danmu.textBackgroundPadding.right),
            top + textBackgroundHeight
        )
        
        danmu.textBackground?.bounds = rect
        danmu.textBackground?.draw(canvas)
    }
    
    override fun execute(canvas: Canvas, danmu: Danmu, channel: Channel) {
        if (danmu.speed == 0f) {
            danmu.isAlive = false
        }
        onLayout(danmu, channel)
        if (hideAll) return
        // 只隐藏用户级别的弹幕
        // 系统级弹幕不隐藏
        if (danmu.priority == Danmu.PRIORITY_NORMAL && hide) {
            return
        }
        draw(canvas, danmu, channel)
    }

    override fun requestLayout() {
    }

}