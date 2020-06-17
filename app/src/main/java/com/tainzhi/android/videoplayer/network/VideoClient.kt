package com.tainzhi.android.videoplayer.network

import com.tainzhi.android.common.base.BaseRetrofitClient
import okhttp3.OkHttpClient

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 16:35
 * @description:
 **/

object VideoClient: BaseRetrofitClient() {

    val service by lazy { getService(VideoService::class.java, VideoService.DOUYU_BASE_URL)}

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        // FIXME: 2020/6/16 去除自定义 LogInterceptor
        // builder.addInterceptor(LogInterceptor())
    }
}