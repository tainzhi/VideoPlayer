package com.tainzhi.android.danmu.advancedanmu.painter

import com.tainzhi.android.danmu.advancedanmu.Channel
import com.tainzhi.android.danmu.advancedanmu.Danmu

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/15 15:19
 * @description:
 **/

class L2RPainter: DanmuPainter() {
    override fun layout(danmu: Danmu, channel: Channel) {
        if (danmu.position.x >= (channel.width + danmu.width)) {
            danmu.isAlive = false
            return
        }
        danmu.position.x = danmu.position.x + (danmu.speed * (1 + 0.5f)).toInt()
    }
}