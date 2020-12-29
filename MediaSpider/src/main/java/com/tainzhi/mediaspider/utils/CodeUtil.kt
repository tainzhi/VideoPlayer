package com.tainzhi.mediaspider.utils

import java.security.MessageDigest

/**
 * File:     CodeUtil
 * Author:   tainzhi
 * Created:  2020/12/29 16:41
 * Mail:     QFQ61@qq.com
 * Description:
 */


fun String.toMD5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.toHex()
}

fun ByteArray.toHex(): String {
    return joinToString("") { "%02x".format(it) }
}
