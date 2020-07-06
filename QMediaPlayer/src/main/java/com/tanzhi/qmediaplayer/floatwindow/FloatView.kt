package com.tanzhi.qmediaplayer.floatwindow

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
    open var width: Int = 300
    open var height: Int = 400

    abstract fun init()
    abstract fun dismiss()
}