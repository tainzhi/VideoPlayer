package com.tainzhi.android.videoplayer.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/20 下午12:14
 * @description:
 **/

class NetWorkUtils {

    companion object {
        @SuppressWarnings("deprecation")
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                networkCapabilities != null && (
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        )
            } else {
                val info = manager.activeNetworkInfo
                !(null == info || !info.isAvailable)
            }
        }

        @SuppressWarnings("deprecation")
        fun isWifiConnected(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                networkCapabilities != null &&
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                val info = manager.activeNetworkInfo
                !(null == info || !info.isAvailable)
            }
        }

        @SuppressWarnings("deprecation")
        fun isMobileData(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                networkCapabilities != null &&
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            } else {
                val info = manager.activeNetworkInfo
                !(null == info || !info.isAvailable)
            }
        }
    }
}
