package com.tainzhi.mediaspider

import android.content.Context
import okio.buffer
import okio.source
import java.io.File
import java.nio.charset.Charset

/**
 * File:     Resource
 * Author:   tainzhi
 * Created:  2020/12/19 14:29
 * Mail:     QFQ61@qq.com
 * Description:
 */

fun Context.readAssets(path: String): String {
    assets.open(path).use { inputStream ->
        val buffer = inputStream.source().buffer()
        return buffer.readString(Charset.forName("utf-8"))
    }
}

fun File.readString(): String {
    this.inputStream().use { inputStream ->
        val buffer = inputStream.source().buffer()
        return buffer.readString(Charset.forName("utf-8"))
    }
}