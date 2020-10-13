package com.tainzhi.android.danmu.advancedanmu.view

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/9 下午10:08
 * @description:
 **/

/**
 * 用于确定Danmu是否在可见区域
 */
interface OnDanmuTouchListener {

    fun onTouch(x: Float, y: Float): Boolean
}

interface OnDanmuContainerViewListener {

    fun callBack()

    fun release()

    fun hideControlPanel()
}