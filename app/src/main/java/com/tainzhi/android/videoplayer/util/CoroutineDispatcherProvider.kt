package com.tainzhi.android.videoplayer.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/24 下午1:25
 * @description:
 **/

data class CoroutineDispatcherProvider(
        val main: CoroutineDispatcher = Dispatchers.Main,
        val default: CoroutineDispatcher = Dispatchers.Default,
        val io: CoroutineDispatcher = Dispatchers.IO
) {
    constructor() : this(Main, Default, IO)

    val mainImmediate = Dispatchers.Main.immediate
}