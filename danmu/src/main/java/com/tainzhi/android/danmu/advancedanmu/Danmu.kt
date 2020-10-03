package com.tainzhi.android.danmu.advancedanmu

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.tainzhi.android.danmu.advancedanmu.view.OnDanmuTouchListener
import com.tainzhi.android.danmu.dp
import com.tainzhi.android.danmu.sp

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 15:10
 * @description:
 **/

class Danmu : OnDanmuTouchListener {

    var paddingLeft = 0
    var paddingRight = 0
    var marginLeft = 30.dp()
    var marginRight = 0

    // user avatar
    var avatar: Bitmap? = null
    var avatarWidth = 30.dp()
    var avatarHeight = 30.dp()

    // 用户图像描边(默认白色描边)
    var avatarStrokes = true

    // 用户等级标签
    var levelBitmap: Bitmap? = null
    var levelBitmapWidth = 33.dp()
    var levelBitmapHeight = 16.dp()
    var levelBitmapMarginLeft = 5.dp()
    var levelBitmapMarginRight = 0

    // 用户等级标签文本
    var levelText: CharSequence? = null
    var levelTextSize = 14f.sp()
    var levelTextColor = 0

    // 弹幕文本
    var text: CharSequence? = null
    var textSize = 14f.sp()
    var textColor = 0
    var textMarginLeft = 5.dp()

    var textBackground: Drawable? = null
    var textBackgroundMarginLeft = 0
    var textBackgroundPadding = Rect(15.dp(), 3.dp(), 15.dp(), 3.dp())

    var position = Point(-1, -1)

    var width = 0
    var height = 0
    var enableTouch = true
    var channelIndex = 0
    var isMoving = true
    var isAlive = true
    var isDeprecated = false
    var displayType = 0
    var attached = false
    var priority = PRIORITY_NORMAL
    var isMeasured = false

    var speed = 0f

    override fun onTouch(x: Float, y: Float): Boolean {
        return false
    }

    override fun release() {
    }

    companion object {

        const val RIGHT_TO_LEFT = 1
        const val LEFT_TO_RIGHT = 2


        const val PRIORITY_SYSTEM = 100
        const val PRIORITY_NORMAL = 50
    }
}