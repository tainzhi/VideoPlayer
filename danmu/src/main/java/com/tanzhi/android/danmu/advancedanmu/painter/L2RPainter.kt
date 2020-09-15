package com.tanzhi.android.danmu.advancedanmu.painter

import com.tanzhi.android.danmu.advancedanmu.Channel
import com.tanzhi.android.danmu.advancedanmu.DanmuModel

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/15 15:19
 * @description:
 **/

class L2RPainter: DanmuPainter() {
    override fun layout(danmuModel: DanmuModel, channel: Channel) {
        if (danmuModel.position.x >= (channel.width + danmuModel.width)) {
            danmuModel.isAlive = false
            return
        }
        danmuModel.position.x = danmuModel.position.x + (danmuModel.speed * ( 1 + 0.5f)).toInt()
    }
}