package com.tanzhi.mediaspider

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.System.currentTimeMillis
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/4 16:01
 * @description: 爬取斗鱼直播间直播源
 **/

class Douyu {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCode() {
        val time = currentTimeMillis()
        val time1000 = time * 1000
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMDD"))
    }
}