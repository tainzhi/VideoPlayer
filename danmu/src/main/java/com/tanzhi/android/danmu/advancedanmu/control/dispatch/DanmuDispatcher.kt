package com.tanzhi.android.danmu.advancedanmu.control.dispatch

import android.content.Context
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel
import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 13:33
 * @description:
 **/

class DanmuDispatcher(context: Context) : IDispatcher {

    private val textPaint = TextPaint().apply {
        flags = Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        strokeWidth = 3.5f
    }

    override fun dispatch(danmuModel: DanmuModel, channels: Array<Channel>) {
        if (!danmuModel.attached) {
            val index = Random.nextInt(channels.size)
            val danmuChannel = channels[index]
            measure(danmuModel, danmuChannel)
        }

    }

    private fun measure(danmuModel: DanmuModel, channel: Channel) {
        val text = danmuModel.text ?: return
        textPaint.textSize = danmuModel.textSize

        val staticLayout = StaticLayout.Builder.obtain(
                text, 0, text.length, textPaint, StaticLayout.getDesiredWidth(text, textPaint).toInt())
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
        val textWidth = danmuModel.position.x +
                danmuModel.marginLeft +
                danmuModel.avatarWidth +
                danmuModel.levelBitmapMarginLeft +
                danmuModel.levelBitmapWidth +
                danmuModel.textMarginLeft +
                staticLayout.width +
                danmuModel.textBackgroundPadding.right
        danmuModel.width = textWidth

        val textHeight = staticLayout.height +
                danmuModel.textBackgroundPadding.top +
                danmuModel.textBackgroundPadding.bottom

        if (danmuModel.avatar != null && danmuModel.avatarHeight > textHeight) {
            danmuModel.height = danmuModel.position.y + danmuModel.avatarHeight
        } else {
            danmuModel.height = danmuModel.position.y + textHeight
        }
        if (danmuModel.displayType == DanmuModel.RIGHT_TO_LEFT) {
            danmuModel.position.x = danmuModel.width
        } else {
            danmuModel.position.x = -danmuModel.width
        }

        danmuModel.isMeasured = true
        danmuModel.position.y = channel.topY
        danmuModel.isAlive = true
    }
}