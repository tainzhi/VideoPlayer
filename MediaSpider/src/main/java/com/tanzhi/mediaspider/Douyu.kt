package com.tanzhi.mediaspider

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

class Douyu(val context: Context) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCode() {
        // t2
        val timeMills = currentTimeMillis()
        // t1
        val timeSeconds = timeMills / 1000
        // t3
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        getPreUrl("777", timeSeconds.toString(), context)
    }

    fun getPreUrl(rid: String, time: String, context: Context): String {
        val queue = Volley.newRequestQueue(context)
        val url = "https://playweb.douyucdn.cn/lapi/live/hlsH5Preview/$rid"
        val postData = mapOf(
                "rid" to rid,
                "did" to "10000000000000000000000000001501"
        )
        val auth = (rid + time).toMD5()
        val jsonRequest = object : StringRequest(Method.POST,
                url,
                Response.Listener { response ->
                    println(response.toString())
                },
                Response.ErrorListener { error ->
                    error
                    println(error.toString())
                }) {
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
        queue.add(jsonRequest)
        return ""
    }

    // fun request(context: Context) {
    //     val queue = Volley.newRequestQueue(context)
    //     val jsonRequest = object : StringRequest(Request.Method.POST) {
    //         override fun getHeaders(): MutableMap<String, String> {
    //             return super.getHeaders()
    //         }
    //
    //         override fun getBody(): ByteArray {
    //             return super.getBody()
    //         }
    //     }
    // }
}

fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}
