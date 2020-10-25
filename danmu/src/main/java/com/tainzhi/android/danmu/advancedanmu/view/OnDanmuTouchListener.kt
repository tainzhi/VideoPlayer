package com.tainzhi.android.danmu.advancedanmu.view

/**
 * 用于确定Danmu是否在可见区域
 */
interface OnDanmuTouchListener {
    fun onTouch(x: Float, y: Float): Boolean
}
