package com.tanzhi.android.danmu.simpledanmu

import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/11 05:52
 * @description:
 **/
 
data class DanmuBean(
        val id: Int,
        val avatorId: Int,
        val userName: String,
        val content: String
): Serializable