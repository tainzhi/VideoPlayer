package com.tanzhi.android.danmu.advancedanmu

import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import com.tanzhi.android.danmu.advancedanmu.view.OnDanmuViewTouchListener

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 15:10
 * @description:
 **/

class DanmuModel: OnDanmuViewTouchListener {

    var paddingLeft = 0
    var paddingRight = 0
    var marginLeft = 0
    var marginRight = 0

    // user avatar
    var avatar: Bitmap? = null
    var avatarWidth = 0
    var avatarHeight = 0
    // 用户图像描边(默认白色描边)
    var avatarStrokes = true

    // 用户等级标签
    var levelBitmap: Bitmap? = null
    var levelBitmapWidth = 0
    var levelBitmapHeight = 0
    var levelBitmapMarginLeft = 0
    var levelBitmapMarginRight = 0

    // 用户等级标签文本
    var levelText: CharSequence? = null
    var levelTextSize = 0f
    var levelTextColor = 0

    // 弹幕文本
    var text: CharSequence? = null
    var textSize = 0f
    var textColor = 0
    var textMarginLeft = 0

    var textBackground: Drawable? = null
    var textBackgroundMarginLeft = 0
    var textBackgroundPadding = Rect(0, 0, 0, 0)

    var startPosition = Point(-1, -1)

    var width = 0
    var height = 0
    var enableTouch = true
    var channelIndex = 0
    var isMoving = true
    var isAlive = true
    var displayType = 0
    var attached = false
    var priority = PRIORITY_NORMAL
    var isMeasured = false
    // var ondanmuViewTouchListener: OndanmuViewTouchListener? = null
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