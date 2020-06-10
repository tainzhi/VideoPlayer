package com.tanzhi.mediaspider

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.zip.GZIPInputStream

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/9 13:11
 * @description: 封装的请求
 */

class KRequest {

    var userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36"

    var headers: Map<String, String> ?= null

    var cookie: String ?= null

    private var data: ByteArray? = null

    var mapData: Map<String, String>? = null
        set(value) {
            value?.let {
                field = value

                val encodeData = it.keys.joinToString(separator = "&") { key ->
                    val enValue = URLEncoder.encode(it[key], "utf-8")
                    "$key=$enValue"
                }
                data = encodeData.toByteArray()

            }
        }

    fun get(url: String): String {
        val connect = (URL(url).openConnection() as HttpURLConnection).apply {
            useCaches = false
        }
        cookie?.let { connect.setRequestProperty("cookie", it)}
        userAgent.let { connect.setRequestProperty("User-Agent", it) }
        headers?.forEach { (key, value) ->
            connect.setRequestProperty(key, value)
        }
        return dom(connect)
    }

    fun post(url: String): String {
        val connect = (URL(url).openConnection() as HttpURLConnection).apply {
            useCaches = false
            requestMethod = "POST"
        }
        cookie?.let { connect.setRequestProperty("cookie", it)}
        userAgent.let { connect.setRequestProperty("User-Agent", it) }
        headers?.forEach { (key, value) ->
            connect.setRequestProperty(key, value)
        }
        data?.let {
            connect.doOutput = true
            connect.outputStream.write(it)
        }
        return dom(connect)
    }

    // 获取请求 response
    private fun dom(connect: HttpURLConnection): String {
        // 必须要加压缩, 否则乱码
        val input = BufferedReader(InputStreamReader(GZIPInputStream(connect.inputStream)))
        var result = ""
        while (true) {
            val line: String? = input.readLine() ?: break
            result += line
        }
        return result

    }
}

// 获取Json对象
fun String.json(): JsonObject {
    // val text = dom().body().text()
    return JsonParser.parseString(this).asJsonObject
}

// 获取Json数组
fun String.jsonArray(): JsonArray {
    // val rawJson = this.body().text()
    return JsonParser.parseString(this).asJsonArray
}
