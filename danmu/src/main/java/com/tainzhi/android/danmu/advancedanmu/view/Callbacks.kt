package com.tainzhi.android.danmu.advancedanmu.view

import com.tainzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午10:08
 * @description:
 **/

interface OnDanmuwTouchListener {

    fun onTouch(x: Float, y: Float): Boolean

    fun release()
}

interface OnDanmuTouchCallBackListener {
    fun callBack(danmu: Danmu)
}

interface OnDanmuParentViewTouchCallbackListener {
    fun callBack()

    fun release()

    fun hideControlPanel()
}