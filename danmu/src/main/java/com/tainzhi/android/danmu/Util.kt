package com.tainzhi.android.danmu

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/12 15:49
 * @description:
 **/

inline fun <reified T> Activity.dpToPx(value: Int): T {
    val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(), this.resources.displayMetrics)

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Context.dpToPx(value: Int): T {
    val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(), this.resources.displayMetrics)

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Activity.spToPx(value: Int): T {
    val result = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value.toFloat(), this.resources.displayMetrics)

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Context.spToPx(value: Int): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        value.toFloat(), this.resources.displayMetrics)
    
    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

// dp转px
fun Int.dp():Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}

// dp转px
fun Float.dp():Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)
}

// sp转px
fun Float.sp():Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,this, Resources.getSystem().displayMetrics)
}

fun Activity.screenWidth(): Int {
    val dm = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

fun Activity.screenHeight(): Int {
    val dm = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(dm)
    return dm.heightPixels
}

