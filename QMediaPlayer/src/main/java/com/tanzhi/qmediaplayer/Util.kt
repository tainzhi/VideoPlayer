package com.tanzhi.qmediaplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import java.util.*

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/20 14:45
 * @description:
 **/

object Util {
    var SYSTEM_UI = 0

    fun stringForTime(timeMs: Long): String {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00"
        }
        val totalSeconds = timeMs / 1000
        val seconds = (totalSeconds % 60).toInt()
        val minutes = (totalSeconds / 60 % 60).toInt()
        val hours = (totalSeconds / 3600).toInt()
        val stringBuilder = StringBuilder()
        val mFormatter = Formatter(stringBuilder, Locale.getDefault())
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    /**
     * This method requires the caller to hold the permission ACCESS_NETWORK_STATE.
     *
     * @param context context
     * @return if wifi is connected,return true
     */
    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Get activity from context object
     *
     * @param context context
     * @return object of Activity or null if it is not Activity
     */
    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context context
     * @return AppCompatActivity if it's not null
     */
    fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    fun setRequestedOrientation(context: Context, orientation: Int) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context)!!.requestedOrientation = orientation
        } else {
            scanForActivity(context)!!.requestedOrientation = orientation
        }
    }

    /**
     * 是否竖屏
     */
    fun isOrientationPort(context: Context): Boolean {
        return getAppCompActivity(context)?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ?: false
    }

    fun getWindow(context: Context?): Window {
        return if (getAppCompActivity(context) != null) {
            getAppCompActivity(context)!!.window
        } else {
            scanForActivity(context)!!.window
        }
    }

    @SuppressLint("RestrictedApi")
    fun showStatusBar(context: Context) {
        getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    //如果是沉浸式的，全屏前就没有状态栏
    @SuppressLint("RestrictedApi")
    fun hideStatusBar(context: Context?) {
        getWindow(context).setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    fun showActionBar(context: Context) {
        if (context is AppCompatActivity) {
            context.supportActionBar?.show()
        }
    }

    fun hideActionBar(context: Context) {
        if (context is AppCompatActivity) {
            context.supportActionBar?.hide()
        }
    }

    @SuppressLint("NewApi")
    fun hideSystemUI(context: Context?) {
        var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        SYSTEM_UI = getWindow(context).decorView.systemUiVisibility
        getWindow(context).decorView.systemUiVisibility = uiOptions
    }

    @SuppressLint("NewApi")
    fun showSystemUI(context: Context?) {
        val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
        getWindow(context).decorView.systemUiVisibility = SYSTEM_UI
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 半透明状态栏
            getWindow(context).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }


    fun getScreenWidthAndHeight(context: Context) : Point{
        val point = Point()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        return point
    }
}
