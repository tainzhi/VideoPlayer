package com.tanzhi.mediaspider

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.System.currentTimeMillis
import java.security.MessageDigest
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

    fun getPreUrl(rid: String, time: String, context: Context): String {
        val queue = Volley.newRequestQueue(context)
        val url = "https://playweb.douyucdn.cn/lapi/live/hlsH5Preview/$rid"
        val postData = mapOf(
                "rid" to rid,
                "did" to "10000000000000000000000000001501"
        )
        val auth = (rid + time).toMD5()
        val jsonRequest = object : StringRequest(Request.Method.POST,
                url,
                Response.Listener { response ->  },
                Response.ErrorListener {  }) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf(
                        "content-type" to "application/x-www-form-urlencoded",
                        "rid" to rid,
                        "time" to time,
                        "auth" to auth
                )
            }

            override fun getBody(): ByteArray {
                return postData.toString().toByteArray(Charsets.UTF_8)
            }
        }
        return ""
    }

    fun request(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val jsonRequest = object : StringRequest(Request.Method.POST) {
            override fun getHeaders(): MutableMap<String, String> {
                return super.getHeaders()
            }

            override fun getBody(): ByteArray {
                return super.getBody()
            }
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
