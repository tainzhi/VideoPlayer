package com.tainzhi.mediaspider.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit.SECONDS

/**
 * File:     OkHttpUtil
 * Author:   tainzhi
 * Created:  2020/12/19 15:39
 * Mail:     QFQ61@qq.com
 * Description:
 */

class OkHttpUtil {

    fun request(urlPath: String, success: (String) -> Unit, error: (String) -> Unit) {
        val request = Request.Builder().url(urlPath).build()
        client.newCall(request).enqueue(
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        error(e.stackTrace.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful && response.body != null) {
                            success(response.body!!.string())
                        } else {
                            error("response failed or body is null")
                        }
                    }
                }
        )
    }

    fun request(urlPath: String): String {
        val request = Request.Builder().url(urlPath).build()
        return client.newCall(request).execute().body!!.string()
    }

    fun post(urlPath: String, headerMap: Map<String, String>?, postBody: Map<String, String>?): String {
        val headers: Headers.Builder = Headers.Builder()
        headerMap?.forEach { t, u ->
            headers.add(t, u)
        }
        var data: ByteArray? = null

        postBody?.let {
            val encodeData = it.keys.joinToString(separator = "&") { key ->
                val enValue = URLEncoder.encode(it[key], "utf-8")
                "$key=$enValue"
            }
            data = encodeData.toByteArray()
        }
        val request = Request.Builder().url(urlPath)
        request.headers(headers.build())
        data?.let { request.post(it.toRequestBody()) }
        return client.newCall(request.build()).execute().body!!.string()
    }

    fun cancelAll() {
        client.dispatcher.cancelAll()
    }

    companion object {
        private val client by lazy {
            OkHttpClient
                    .Builder()
                    .readTimeout(10, SECONDS)
                    .build()
        }
        val instance: OkHttpUtil by lazy { OkHttpUtil() }
    }
}