package com.tainzhi.mediaspider

import fr.arnaudguyon.xmltojsonlib.XmlToJson
import java.util.Locale

/**
 * File:     Util
 * Author:   tainzhi
 * Created:  2020/12/19 16:10
 * Mail:     QFQ61@qq.com
 * Description:
 */
object Util {

    fun xmlToJson(xmlString: String): XmlToJson? {
        return XmlToJson.Builder(xmlString).build()
    }
}

/**
 * 可以在应用内播放的地址
 */
fun String?.isVideoUrl(): Boolean {
    return (this ?: "").toLowerCase(Locale.ROOT).run {
        endsWith(".m3u8")
                || endsWith("||.mp4")
                || endsWith(".flv")
                || endsWith(".avi")
                || endsWith(".rm")
                || endsWith(".rmvb")
                || endsWith(".wmv")
    }
}
