package com.tanzhi.qmediaplayer.floatwindow

import android.view.View

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/6 10:01
 * @description:
 **/
 
abstract class FloatView  {
    open var x: Int = 0
    open var y: Int = 0
    open var gravity: Int = 0
    open var width = 0
    open var height = 0

    abstract fun show()
    abstract fun dismiss()
    abstract fun updateLayout()
    abstract fun setView(view: View)
}