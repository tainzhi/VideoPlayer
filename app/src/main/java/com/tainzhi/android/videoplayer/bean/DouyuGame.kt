package com.tainzhi.android.videoplayer.bean

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 16:51
 * @description: 某个douyu game
 **/
 
 
data class DouyuGame(
    val cate_id: Int,
    val game_name: String,
    val short_name: String,
    val game_url: String,
    val game_src: String,
    val game_icon: String
)