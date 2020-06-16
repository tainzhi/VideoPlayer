package com.tainzhi.android.videoplayer.network

import com.orhanobut.logger.Logger
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 19:39
 * @description:
 **/

class LogInterceptor : Interceptor {
    companion object {
        const val TAG = "LogInterceptor"

        /**
         * 白名单
         * 在白名单中的请求不输出日志
         */
        private val sWhiteList: MutableList<String> = LinkedList()

        init {
            sWhiteList.add("/ceo/b/msg/unreadCount") //未读消息请求接口
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        response = try {
            chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            throw IOException("网络超时!")
        }
        val url = request.url.toUrl().toString()
        return if (existInWhiteList(url)) {
            response
        } else logResponse(response)
    }

    private fun existInWhiteList(url: String?): Boolean {
        for (s in sWhiteList) {
            if (url != null && url.contains(s)) {
                return true
            }
        }
        return false
    }

    private fun logResponse(response: Response): Response {
        try {
            val request = response.request
            val url = request.url.toString()
            val headers = request.headers


            //    Logger.
            //  Logger.d("method : " + request.method());
            if (headers != null && headers.size > 0) {
                //   Logger.d( "headers : " + headers.toString());
            }
            val requestBody = request.body
            if (requestBody != null) {
                val mediaType = requestBody.contentType()
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        Logger.d("%s\n%s%s\n%s",
                                "url : $url",
                                "headers : $headers",
                                "method : " + request.method,
                                "params : " + bodyToString(request))
                    } else {
                        Logger.d("params : " + " maybe [file part] , too large too print , ignored!")
                    }
                }
            } else {
                Logger.d("%s\n%s%s",
                        "url : $url",
                        "method : " + request.method,
                        "headers : $headers")
            }
            val builder = response.newBuilder()
            val clone = builder.build()
            var body = clone.body
            if (body != null) {
                val mediaType = body.contentType()
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        val resp = body.string()
                        Logger.json(resp)
                        body = ResponseBody.create(mediaType, resp)
                        return response.newBuilder().body(body).build()
                    } else {
                        Logger.d(TAG, "data : " + " maybe [file part] , too large too print , ignored!")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun isText(mediaType: MediaType?): Boolean {
        return if (mediaType == null) false else "text" == mediaType.subtype || "json" == mediaType.subtype || "xml" == mediaType.subtype || "html" == mediaType.subtype || "webviewhtml" == mediaType.subtype || "x-www-form-urlencoded" == mediaType.subtype
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "something error when show requestBody."
        }
    }
}
