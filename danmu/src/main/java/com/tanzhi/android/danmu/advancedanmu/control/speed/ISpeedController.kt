package com.tanzhi.android.danmu.advancedanmu.control.speed

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 10:19
 * @description:
 **/

open class ISpeedController {
    var width = 0
    var speed = 0f
    var maxSpeed = 0f
    var minSpeed = 0f
    companion object {
        const val RATE = 1000
    }
}