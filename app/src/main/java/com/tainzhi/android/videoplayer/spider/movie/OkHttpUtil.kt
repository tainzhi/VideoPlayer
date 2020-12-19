package com.tainzhi.android.videoplayer.spider.movie

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
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
                        success(response.body.toString())
                    }
                }
        )
    }

    fun request(urlPath: String): String {
        val request = Request.Builder().url(urlPath).build()
        return client.newCall(request).execute().body.toString()
    }

    companion object {
        private val client by lazy { OkHttpClient.Builder().readTimeout(5, SECONDS).build() }
        val instance: OkHttpUtil by lazy { OkHttpUtil() }
    }
}