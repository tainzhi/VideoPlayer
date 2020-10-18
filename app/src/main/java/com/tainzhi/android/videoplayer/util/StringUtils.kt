package com.tainzhi.android.videoplayer.util

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/10/18 17:54
 * @description:
 **/

/**
 * get multiplication sign from string like "1080x720"
 *
 * 没有找到, 则返回第一个字符
 */
fun String.multiplicationSign(): Char {
    for (i in 0..this.lastIndex) {
        if (!this[i].isDigit()) {
            return this[i]
        }
    }
    return this[0]
}
