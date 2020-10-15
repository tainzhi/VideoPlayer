package com.tainzhi.android.danmu.simpledanmu

import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/11 05:52
 * @description:
 **/

data class SimpleDanmuBean(
        val id: Int,
        val avatarId: Int,
        val userName: String,
        val content: String
) : Serializable