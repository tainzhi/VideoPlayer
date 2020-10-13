package com.tainzhi.android.danmu.advancedanmu.control.dispatch

import android.graphics.Paint
import android.text.TextPaint
import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu
import com.tainzhi.android.danmu.advancedanmu.painter.DanmuPainter
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 13:33
 * @description: 确定每个Danmu的绘制位置
 **/

class DanmuDispatcher : IDispatcher {

    private val textPaint = TextPaint().apply {
        flags = Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        strokeWidth = 3.5f
    }

    override fun dispatch(danmu: Danmu, channels: Array<Channel>) {
        if (!danmu.attached) {
            val index = Random.nextInt(channels.size)
            val danmuChannel = channels[index]
            measure(danmu, danmuChannel)
        }
    }

    /**
     * 确定每条Danmu的初始宽和高, 初始坐标
     */
    private fun measure(danmu: Danmu, channel: Channel) {
        val text = danmu.text ?: return
        textPaint.textSize = danmu.textSize

        val textStaticLayout = DanmuPainter.generateTextStaticLayout(textPaint, text)
        danmu.textMeasuredWith = textStaticLayout.width
        danmu.textMeasuredHeight = textStaticLayout.height
        danmu.width = danmu.position.x +
                danmu.marginLeft +
                danmu.avatarWidth +
                danmu.levelBitmapMarginLeft +
                danmu.levelBitmapWidth +
                danmu.textMarginLeft +
                danmu.textMeasuredWith +
                danmu.textBackgroundPadding.right

        val textHeight = danmu.textMeasuredHeight +
                danmu.textBackgroundPadding.top +
                danmu.textBackgroundPadding.bottom

        if (danmu.avatar != null && danmu.avatarHeight > textHeight) {
            danmu.height = danmu.position.y + danmu.avatarHeight
        } else {
            danmu.height = danmu.position.y + textHeight
        }
        if (danmu.displayType == Danmu.RIGHT_TO_LEFT) {
            danmu.position.x = channel.width
        } else {
            danmu.position.x = -channel.width
        }
        
        danmu.isMeasured = true
        danmu.position.y = channel.topY
        danmu.isAlive = true
    }
}