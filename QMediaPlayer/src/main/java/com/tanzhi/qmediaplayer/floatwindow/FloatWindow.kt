package com.tanzhi.qmediaplayer.floatwindow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.tanzhi.qmediaplayer.R

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/6 12:06
 * @description:
 **/

class FloatWindow(val context: Context) {
    var width = 0.3
    var height = 0.4
    var x = 0.1
    var y = 0.2
    private lateinit var content: View

    init {
        content = LayoutInflater.from(context).inflate(R.layout.float_window, null)

    }
}