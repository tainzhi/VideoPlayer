package com.tainzhi.mediaspider.spider
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json


/**
 * File:     DouyuGameInfoBean
 * Author:   tainzhi
 * Created:  2020/12/17 21:16
 * Mail:     QFQ61@qq.com
 * Description:
 *
 */
/*

 */
@JsonClass(generateAdapter = true)
data class RoomBean(
    @Json(name = "data")
    val `data`: Data?,
    @Json(name = "error")
    val error: Int,
    @Json(name = "msg")
    val msg: String
)

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "cdnsWithName")
    val cdnsWithName: List<Any>,
    @Json(name = "client_ip")
    val clientIp: String,
    @Json(name = "eticket")
    val eticket: String?,
    @Json(name = "inNA")
    val inNA: Int,
    @Json(name = "is_mixed")
    val isMixed: Boolean,
    @Json(name = "is_pass_player")
    val isPassPlayer: Int,
    @Json(name = "mixedCDN")
    val mixedCDN: String,
    @Json(name = "mixed_live")
    val mixedLive: String,
    @Json(name = "mixed_url")
    val mixedUrl: String,
    @Json(name = "multirates")
    val multirates: List<Any?>?,
    @Json(name = "online")
    val online: Int,
    @Json(name = "p2p")
    val p2p: Int,
    @Json(name = "rate")
    val rate: Int,
    @Json(name = "rateSwitch")
    val rateSwitch: Int,
    @Json(name = "room_id")
    val roomId: Int,
    @Json(name = "rtmp_cdn")
    val rtmpCdn: String,
    @Json(name = "rtmp_live")
    val rtmpLive: String,
    @Json(name = "rtmp_url")
    val rtmpUrl: String,
    @Json(name = "smt")
    val smt: Int,
    @Json(name = "streamStatus")
    val streamStatus: Int
)