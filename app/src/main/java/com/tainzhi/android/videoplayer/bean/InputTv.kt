package com.tainzhi.android.videoplayer.bean

import java.io.Serializable

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/5/12 10:41
 * @description:
 **/
 

data class InputTv(
    val type: String?,
    val tvLists: List<TvLists>
): Serializable

data class TvLists(
    val tvId: String?,
    val tvType: String?,
    val tvName: String,
    val tvImg: String?,
    val tvCircuit: List<String>?,
    val programUrl: String?,
    val introduce: String?
): Serializable

