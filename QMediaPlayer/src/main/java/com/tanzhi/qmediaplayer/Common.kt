package com.tanzhi.qmediaplayer

import android.content.res.Resources
import android.util.Log
import android.util.TypedValue

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/20 11:26
 * @description:
 **/

const val DEBUG = true
const val TAG = "QMediaPlayer"

fun logD(tag:String = TAG, info: String = "") {
    if (DEBUG) {
        Log.d(tag, info)
    }
}

fun logI(tag: String = TAG, info: String = "") {
    if (DEBUG) {
        Log.i(tag, info)
    }
}

fun logE(tag: String = TAG, info: String) {
    if (DEBUG) {
        Log.e(tag, info)
    }
}

// dp转px
fun Int.dp():Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

// dp转px
fun Float.dp():Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this,Resources.getSystem().displayMetrics)
}
