package com.tainzhi.android.danmu.advancedanmu.control.speed

import kotlin.random.Random

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/11 11:45
 * @description:
 **/

class RandomSpeedController: ISpeedController() {
    init {
        minSpeed = 3.5f
        maxSpeed = 9.5f
        speed = ((Random.nextFloat() * (maxSpeed - minSpeed) + minSpeed) / RATE) * width
    }
}
