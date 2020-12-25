package com.tainzhi.android.videoplayer.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * File:     Activity
 * Author:   tainzhi
 * Created:  2020/12/7 10:12
 * Mail:     QFQ61@qq.com
 * Description:
 */

/**
 * 当前activity是否显示, 即在最上面, 用户可见
 *
 * getRunningTasks在api>=21已经 Deprecated
 */
fun Context.isForeGround(): Boolean {
    val activityName = this.javaClass.name
    val activityList = (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningTasks(1)
    if (!activityList.isNullOrEmpty()) {
        val componentName = activityList[0].topActivity
        return componentName?.className == activityName
    }
    return false
}

fun Context.openBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run { startActivity(this) }
}
