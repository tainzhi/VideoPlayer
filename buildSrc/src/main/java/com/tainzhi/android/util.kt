package com.tainzhi.android

import java.text.SimpleDateFormat
import java.util.Locale

/**
 * File:     util
 * Author:   tainzhi
 * Created:  2020/12/31 14:07
 * Mail:     QFQ61@qq.com
 * Description:
 */

object Util {
    fun getSystemTime(): String {
        // "YYYY_MM_dd_HH_mm_ss"
        val simpleDateFormat = SimpleDateFormat("MMddHHmmss", Locale.CHINA)
        return simpleDateFormat.format(System.currentTimeMillis())
    }

    fun upperCaseFirst(str: String) :String {
        val ch = str.toCharArray()
        if (ch[0] in 'a'..'z') {
            ch[0] = ch[0] - 32
        }
        return String(ch)
    }
}

object Constant {
    const val autoUploadExtensionName = "autoUpload"
}
