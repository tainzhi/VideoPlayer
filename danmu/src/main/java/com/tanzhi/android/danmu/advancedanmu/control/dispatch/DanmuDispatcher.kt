package com.tanzhi.android.danmu.advancedanmu.control.dispatch

import android.content.Context
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.tanzhi.android.danmu.advancedanmu.DanmuChannel
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

    override fun dispatch(DanmuModel: DanmuModel, danmuChannels: Array<DanmuChannel>) {
        if (!DanmuModel.attached) {
            val index = Random.nextInt(danmuChannels.size)
            val danmuChannel = danmuChannels[index]
            measure(DanmuModel, danmuChannel)
        }

    }

    private fun measure(DanmuModel: DanmuModel, danmuChannel: DanmuChannel) {
        val text = DanmuModel.text ?: return
        textPaint.textSize = DanmuModel.textSize

        val staticLayout = StaticLayout.Builder.obtain(
                text, 0, text.length, textPaint, StaticLayout.getDesiredWidth(text, textPaint).toInt())
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .build()
        val textWidth = DanmuModel.startPosition.x +
                DanmuModel.marginLeft +
                DanmuModel.avatarWidth +
                DanmuModel.levelBitmapMarginLeft +
                DanmuModel.levelBitmapWidth +
                DanmuModel.textMarginLeft +
                staticLayout.width +
                DanmuModel.textBackgroundPadding.right
        DanmuModel.width = textWidth

        val textHeight = staticLayout.height +
                DanmuModel.textBackgroundPadding.top +
                DanmuModel.textBackgroundPadding.bottom

        if (DanmuModel.avatar != null && DanmuModel.avatarHeight > textHeight) {
            DanmuModel.height = DanmuModel.startPosition.y + DanmuModel.avatarHeight
        } else {
            DanmuModel.height = DanmuModel.startPosition.y + textHeight
        }
        if (DanmuModel.displayType == DanmuModel.RIGHT_TO_LEFT) {
            DanmuModel.startPosition.x = DanmuModel.width
        } else {
            DanmuModel.startPosition.x = -DanmuModel.width
        }

        DanmuModel.isMeasured = true
        DanmuModel.startPosition.y = danmuChannel.topY
        DanmuModel.isAlive = true
    }
}