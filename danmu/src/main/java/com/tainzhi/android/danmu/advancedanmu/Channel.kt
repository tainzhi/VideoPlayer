package com.tainzhi.android.danmu.advancedanmu

import com.tainzhi.android.danmu.dp

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/9/10 15:09
 * @description:
 **/

class Channel {

    var speed = 3f
    var width = 0
    var height = 0
    var topY = 0
    var space = 60

    private var r2lReferenceView: Danmu? = null
    private var l2rReferenceView: Danmu? = null

    companion object {

        const val MAX_COUNT_IN_SCREEN = 30
        private const val DEFAULT_SIGNAL_CHANNEL_HEIGHT = 30

        /**
         * 弹幕区域同时能显示的最大弹幕频道数
         *
         * @param width 弹幕区域宽度, 一般为屏幕宽度
         * @param height 弹幕区域高度, height / 单个弹幕高度 = 区域内弹幕个数
         */
        fun createChannels(width: Int, height: Int): Array<Channel> {
            val singleHeight = (DEFAULT_SIGNAL_CHANNEL_HEIGHT).dp()
            val count = height / singleHeight
            val channels = Array<Channel>(count) { Channel() }
            for (i in 0 until count) {
                channels!![i].run {
                    this.width = width
                    this.height = singleHeight
                    this.topY = i * singleHeight
                }
            }
            return channels
        }
    }

    fun dispatch(danMuView: Danmu) {
        if (danMuView.attached) {
            return
        }
        danMuView.speed = speed
        if (danMuView.displayType == Danmu.RIGHT_TO_LEFT) {
            var deltaX = 0
            if (r2lReferenceView != null) {
                deltaX = (width - r2lReferenceView!!.position.x - r2lReferenceView!!.width)
            }
            if (r2lReferenceView == null || r2lReferenceView?.isAlive != true || deltaX > space) {
                danMuView.attached = true
                r2lReferenceView = danMuView
            }
        } else if (danMuView.displayType == Danmu.LEFT_TO_RIGHT) {
            var mDeltaX = 0
            if (l2rReferenceView != null) {
                mDeltaX = l2rReferenceView!!.position.x
            }
            if (l2rReferenceView == null || l2rReferenceView?.isAlive != true || mDeltaX > space) {
                danMuView.attached = true
                l2rReferenceView = danMuView
            }
        }
    }
}