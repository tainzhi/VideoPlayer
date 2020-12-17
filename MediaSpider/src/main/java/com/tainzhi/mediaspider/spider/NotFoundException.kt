package com.tainzhi.mediaspider.spider

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/20 17:17
 * @description:
 **/

class NotFoundException(message: String = "Not Found", cause: Throwable?=null) : Exception(message, cause) {
}