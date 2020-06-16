package com.tanzhi.mediaspider

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.jsoup.Jsoup
import java.lang.System.currentTimeMillis
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/4 16:01
 * @description:
 * 用Volley爬取斗鱼直播间直播源. 因为使用了回调,
 * 非常不方便用于爬虫(比如爬取room id, 需要多个请求和回调, 繁琐)
 * 建议使用 [DouyuSpider]
 *
 * 通过chrome debug工具获取的地址 https://tc-tct.douyucdn2.cn/dyliveflv1/562483rmTwjem0AN_1200p.flv
 * 其中_1200p 对应清晰度
 *
 **/

class DouyuVolleySpider() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCode(requestQueue: RequestQueue) {
        // t2
        val timeMills = currentTimeMillis()
        // t1
        val timeSeconds = timeMills / 1000
        // t3
        val today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        getPreUrl("562483", timeSeconds.toString(), requestQueue)
    }

    /**
     *
     */
    fun getPreUrl(rid: String, time: String, requestQueue: RequestQueue) {
        val url = "https://playweb.douyucdn.cn/lapi/live/hlsH5Preview/$rid"
        val auth = (rid + time).toMD5()
        val header = mutableMapOf(
                "content-type" to "application/x-www-form-urlencoded",
                "rid" to rid,
                "time" to time,
                "auth" to auth
        )

        val postData = mutableMapOf(
                "rid" to rid,
                "did" to "10000000000000000000000000001501"
        )

        /** 成功后的response
        {
            "error": 0,
            "msg": "success",
            "data": {
                "room_id": 85894,
                "rtmp_cdn": "ws",
                "rtmp_url": "https://hlsa.douyucdn.cn/live",
                "rtmp_live": "85894rmovieChow_550/playlist.m3u8?wsSecret=ed5c28f6e977e03e91036ce561f473ec\\u0026wsTime=1591625515\\u0026token=web-douyu-0-85894-8a352071adadaeb0f1705309d80dd80d\\u0026did=10000000000000000000000000001501\\u0026ver=2018061203\\u0026st=0\\u0026preview=1\\u0026pt=3\\u0026origin=all",
                "client_ip": "222.67.156.84",
                ...
            }
        }
        */
        val stringRequest = object : StringRequest(Request.Method.POST,
                url,
                Response.Listener { response ->
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getInt("error") == 0) {
                        val dataObject = jsonObject.getJSONObject("data")
                        val rtmpLive =dataObject.getString("rtmp_live")
                        if (rtmpLive.contains("mix=1")) {
                            println("PKing")
                        } else {
                            val regex = Regex(pattern = "[0-9a-zA-Z]*_")
                            val found = regex.find(rtmpLive)
                            if (found != null) {
                                println(found.value)
                            }
                        }
                    }
                    println("not found")
                },
                Response.ErrorListener { error ->
                    println(error.toString())
                }) {
            override fun getHeaders() = header

            override fun getParams() = postData

        }
        requestQueue.add(stringRequest)
    }

    fun getAllRooms(type: String = "DOTA2") {
        val url = "http://m.douyu.com/list/room?type=$type"
        val userAgent = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Mobile Safari/537.36"
        val document = Jsoup.connect(url).userAgent(userAgent).get()
        val allRooms = document.select("div.NormalRoomItem")
        for (room in allRooms) {
            println(room)
        }
    }
}
