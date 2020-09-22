package com.tainzhi.android.danmu

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.tainzhi.android.danmu.simpledanmu.DanmuBean

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

object Util {
    fun loadData() : ArrayList<DanmuBean> {
        val datas = ArrayList<DanmuBean>()
        datas.add(DanmuBean(0, R.drawable.internet_star, "小强", "女神我爱你！"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "Jack ma", "妈妈问我为什么跪着听歌"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_lori, "习大大", "歌词特别美"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_robot, "宝强", "全世界都安静了"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "宝宝", "听到放不下耳机，听到耳朵痛都不放下，哈哈哈"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "我是大神", "中国好声音，I want you"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "茜茜", "一天脑子里都在唱这首歌"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "Bangbangbang", "有故事，好听"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "stalse", "我就是为了你开的会员"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "hehe", "太好听了.."))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_robot, "小强", "女神我爱你！"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "Jack ma", "妈妈问我为什么跪着听歌"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "习大大", "歌词特别美"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "宝强", "全世界都安静了"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "宝宝", "听到放不下耳机，听到耳朵痛都不放下，哈哈哈"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "我是大神", "中国好声音，I want you"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_robot, "茜茜", "一天脑子里都在唱这首歌"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "Bangbangbang", "有故事，好听"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "stalse", "我就是为了你开的会员"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "hehe", "太好听了.."))
        datas.add(DanmuBean(0, R.drawable.internet_star, "小强", "女神我爱你！"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_robot, "Jack ma", "妈妈问我为什么跪着听歌"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "习大大", "歌词特别美"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "宝强", "全世界都安静了"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "宝宝", "听到放不下耳机，听到耳朵痛都不放下，哈哈哈"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "我是大神", "中国好声音，I want you"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_uncle, "茜茜", "一天脑子里都在唱这首歌"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "Bangbangbang", "有故事，好听"))
        datas.add(DanmuBean(0, R.drawable.make_music_voice_changer_funny, "stalse", "我就是为了你开的会员"))
        datas.add(DanmuBean(0, R.drawable.internet_star, "hehe", "太好听了.."))

        return datas
    }
}