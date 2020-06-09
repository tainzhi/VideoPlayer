package com.tanzhi.mediaspider

import org.json.JSONObject
import java.lang.System.currentTimeMillis
import java.security.MessageDigest

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/9 13:32
 * @description: 使用[KRequest] 爬取斗鱼信息
 **/

class DouyuSpider() {

    fun getRoomLive(rid: String): String {
        val timeSeconds = currentTimeMillis() / 1000
        val url = "https://playweb.douyucdn.cn/lapi/live/hlsH5Preview/$rid"
        val auth = (rid + timeSeconds).toMD5()
        val header = mutableMapOf<String, String>(
                "Content-type" to "application/x-www-form-urlencoded",
                "rid" to rid,
                "time" to timeSeconds.toString(),
                "auth" to auth
        )

        val postData = mutableMapOf(
                "rid" to rid,
                "did" to "10000000000000000000000000001501"
        )

        var roomLiveUrl = "-1"
        var response: String? = null
        try {
            response = KRequest().apply {
                mapData = postData
                headers = header
            }.post(url)

            response?.let {it ->
                val jsonObject = JSONObject(it)
                if (jsonObject.getInt("error") == 0) {
                    val dataObject = jsonObject.getJSONObject("data")
                    val rtmpLive =dataObject.getString("rtmp_live")
                    if (rtmpLive.contains("mix=1")) {
                        println("PKing")
                    } else {
                        val regex = Regex(pattern = "[0-9a-zA-Z]*_")
                        val found = regex.find(rtmpLive)
                        if (found != null) {
                            roomLiveUrl = found.value
                        }
                    }
                }

            }
        } catch (e: Exception) {
            println(e.toString())
        }
        return roomLiveUrl
    }

    companion object {
        @Volatile private var instance: DouyuSpider? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: DouyuSpider().also { instance = it}
        }
    }
}

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}
