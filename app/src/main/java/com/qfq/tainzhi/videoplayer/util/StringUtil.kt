package com.qfq.tainzhi.videoplayer.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by muqing on 2019/6/12.
 * Email: qfq61@qq.com
 */
object StringUtil {
    var hour: Int = 1000 * 60 * 60
    var minute: Int = 1000 * 60
    var second: Int = 1000
    var kbyte: Int = 1024
    var mbyte: Int = 1024 * 1024
    var gbyte: Int = 1024 * 1024 * 1024
    fun formatMediaTime(millsec: Long): String? {
        //"hh:mm:ss"
        //"mm:ss"
        val h: Int = millsec as Int / hour
        val m: Int = millsec as Int % hour / minute
        val sec: Int = millsec as Int % minute / second
        if (h > 0) {
            //"hh:mm:ss" "1:36:2"
            return String.format("%02d:%02d:%02d", h, m, sec)
        } else {
            return String.format("%02d:%02d", m, sec)
        }
    }
    
    fun formatMediaSize(size: Long): String? {
        // 5B
        // 5KB
        // 5MB
        // 5GB
        val k: Int = size as Int % kbyte
        val m: Int = size as Int / mbyte
        val g: Int = size as Int / gbyte
        if (g > 0) {
            return g.toString() + "GB"
        } else if (m > 0) {
            return m.toString() + "MB"
        } else if (k > 0) {
            return k.toString() + "KB"
        } else {
            return size.toString() + "B"
        }
    }
    
    fun formatDate(date: String?): String? {
        if (date == null) {
            return null
        }
        val year: String? = date.substring(0, 4)
        val month: String? = date.substring(4, 6)
        val day: String? = date.substring(6, 8)
        return year + "/" + month + "/" + day
    }
    
    /**
     * 获取当前系统时间 返回格式"HH:mm:ss"
     *
     * @return
     */
    fun formatSystemTime(): String? {
        val format: SimpleDateFormat? = SimpleDateFormat("HH:mm:ss")
        return format.format(Date())
    }
}