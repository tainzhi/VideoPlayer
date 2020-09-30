package com.tainzhi.android.danmu.advancedanmu

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
    
    var r2lReferenceView: Danmu? = null
    var l2rReferenceView: Danmu? = null
    
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