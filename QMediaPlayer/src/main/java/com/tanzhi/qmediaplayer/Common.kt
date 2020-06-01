package com.tanzhi.qmediaplayer

import android.content.res.Resources
import android.util.TypedValue
import com.orhanobut.logger.Logger

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/20 11:26
 * @description:
 **/

const val DEBUG = true

inline fun logD(info: String = "") {
    if (DEBUG) {
        Logger.d(info)
    }
}

inline fun logI(info: String = "") {
    if (DEBUG) {
        Logger.i(info)
    }
}

inline fun logE(info: String) {
    if (DEBUG) {
        Logger.e(info)
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
