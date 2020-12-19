package com.tainzhi.android.videoplayer.spider.movie

import android.content.Context
import okio.buffer
import okio.source
import java.nio.charset.Charset

/**
 * File:     Resource
 * Author:   tainzhi
 * Created:  2020/12/19 14:29
 * Mail:     QFQ61@qq.com
 * Description:
 */

fun Context.readAssets(path: String): String {
    val assetBasePath = "../app/src/main/assets/"
    assets.open(assetBasePath + path).use { inputStream ->
        val buffer = inputStream.source().buffer()
        return buffer.readString(Charset.forName("utf-8"))
    }
}

fun Context.readRawFile(id: Int): String {
    resources.openRawResource(id).use { inputStream ->
        val buffer = inputStream.source().buffer()
        return buffer.readString(Charset.forName("utf-8"))
    }
}
