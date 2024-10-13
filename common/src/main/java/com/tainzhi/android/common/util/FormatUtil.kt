package com.tainzhi.android.common.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by muqing on 2019/6/12.
 * Email: qfq61@qq.com
 *
 * Description: 格式转换
 */
object FormatUtil {
    private const val hour = 1000 * 60 * 60
    private const val minute = 1000 * 60
    private const val second = 1000
    private const val kbyte = 1024
    private const val mbyte = 1024 * 1024
    private const val gbyte = 1024 * 1024 * 1024

    /**
     * 把毫秒转换成60进制时间, 比如hh:mm:ss, mm:ss
     *
     * @param millisecond 毫秒
     */
    fun Long.formatMediaDuration(): String {
        val h = this.toInt() / hour
        val m = this.toInt() % hour / minute
        val sec = this.toInt() % minute / second
        return if (h > 0) {
            //"hh:mm:ss" "1:36:2"
            String.format("%02d:%02d:%02d", h, m, sec)
        } else if (m > 0){
            String.format("%02d:%02d", m, sec)
        } else {
            String.format("00:%02d", sec)
        }
    }

    /**
     * 把字节转换成人可读的 5B, 5KB, 5MB, 5GB
     *
     * @param size 字节大小
     * @return 可读字节大小, 单位为B, KB, MB, GB
     */
    fun Long.formatMediaSize(): String {
        // 5B
        // 5.1K
        // 5.1M
        // 5.1G
        val k = this.toInt() % kbyte
        val m = this.toInt() / mbyte
        val g = this.toInt() / gbyte
        return if (g > 0) {
            String.format("%.1fG", this.toFloat() / gbyte)
        } else if (m > 0) {
            String.format("%.1fM", this.toFloat() / mbyte)
        } else if (k > 0) {
            "${k}K"
        } else {
            this.toString() + "B"
        }
    }

    /**
     * 获取mm/dd/yyyy格式时间
     */
    fun Long.formatMediaDate(): String {
        return SimpleDateFormat("MM/dd/yyyy").format(Date(this))
    }

    /**
     * 获取当前系统时间 返回格式"HH:mm:ss"
     *
     * @return
     */
    fun formatSystemTime(): String {
        val format = SimpleDateFormat("HH:mm:ss")
        return format.format(Date())
    }
}