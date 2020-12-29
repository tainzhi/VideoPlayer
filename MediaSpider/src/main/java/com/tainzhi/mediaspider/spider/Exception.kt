package com.tainzhi.mediaspider.spider

import android.content.res.Resources.NotFoundException

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/20 17:17
 * @description:
 **/

open class NotFoundException(message: String = "Not Found", cause: Throwable? = null) : Exception(message, cause) {
}

class RoomNotExist(message: String = "房间不存在") : NotFoundException(message) {}

class RoomNotLiving(message: String = "房间未开播") : NotFoundException(message) {}
