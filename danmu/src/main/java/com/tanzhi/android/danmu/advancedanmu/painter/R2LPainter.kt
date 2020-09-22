package com.tanzhi.android.danmu.advancedanmu.painter

import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/15 15:19
 * @description:
 **/

class R2LPainter: DanmuPainter() {
    override fun layout(danmu: Danmu, channel: Channel) {
        if (danmu.position.x - danmu.speed <= -danmu.width) {
            danmu.isAlive = false
            return
        }
        danmu.position.x = danmu.position.x - danmu.speed.toInt()
    }
}