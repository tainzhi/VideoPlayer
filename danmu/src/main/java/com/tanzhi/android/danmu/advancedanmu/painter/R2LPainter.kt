package com.tanzhi.android.danmu.advancedanmu.painter

import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/15 15:19
 * @description:
 **/

class R2LPainter: DanmuPainter() {
    override fun layout(danmuModel: DanmuModel, channel: Channel) {
        if (danmuModel.position.x - danmuModel.speed <=- danmuModel.width) {
            danmuModel.isAlive = false
            return
        }
        danmuModel.position.x = danmuModel.position.x - danmuModel.speed.toInt()
    }
}