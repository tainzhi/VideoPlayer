package com.tainzhi.android.videoplayer.bean

import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 16:43
 * @description: 某个斗鱼game下的房间
 **/

data class DouyuRoom(
    val room_id: String,
    val room_src: String,
    val room_name: String,
    val owner_uid: String,
    val online: String,
    val hn: Int,
    val nickname: String,
    val url: String
): Serializable