package com.tainzhi.android.common.base

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/19 下午9:18
 * @description:
 **/

data class ResponseBody<out T>(val error: Int?, val msg: String?, val data: T)