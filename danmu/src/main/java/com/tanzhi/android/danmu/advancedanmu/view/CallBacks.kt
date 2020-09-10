package com.tanzhi.android.danmu.advancedanmu.view

import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午10:08
 * @description:
 **/

interface OnDanmuViewTouchListener {
    fun onTouch(x: Float, y: Float): Boolean

    fun release()
}

interface OnDanmuTouchCallBackListener {
    fun callBack(danmuModel: DanmuModel)
}

interface onDanmuParentViewTouchCallbackListener {
    fun callBack()

    fun release()

    fun hideControlPanel()
}