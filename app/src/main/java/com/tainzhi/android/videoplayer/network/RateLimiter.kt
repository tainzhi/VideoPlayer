package com.tainzhi.android.videoplayer.network

import android.os.SystemClock
import android.util.ArrayMap
import java.util.concurrent.TimeUnit

/**
 * File:     RateLimiter
 * Author:   tainzhi
 * Created:  2020/12/7 20:57
 * Mail:     QFQ61@qq.com
 * Description: Utility class that decides whether we should fetch some data or not.
 */
class RateLimiter<in KEY>(timeout: Int, timeUnit: TimeUnit) {
    private val timeStamps = ArrayMap<KEY, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFetch(key: KEY): Boolean {
        val lastFetched = timeStamps[key]
        val now = now()
        if (lastFetched == null) {
            timeStamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timeStamps[key] = now
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()
}