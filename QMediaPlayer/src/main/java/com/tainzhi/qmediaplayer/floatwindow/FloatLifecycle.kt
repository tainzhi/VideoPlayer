package com.tainzhi.qmediaplayer.floatwindow

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler

/**
 * Created by yhao on 17-12-1.
 * 用于控制悬浮窗显示周期
 * 使用了三种方法针对返回桌面时隐藏悬浮按钮
 * 1.startCount计数，针对back到桌面可以及时隐藏
 * 2.监听home键，从而及时隐藏
 * 3.resumeCount计时，针对一些只执行onPause不执行onStop的奇葩情况
 */
class FloatLifecycle(private val applicationContext: Context, private val showFlag: Boolean,
                     private val activities: Array<Class<*>>?, lifecycleListener: LifecycleListener) :
        BroadcastReceiver(),
        ActivityLifecycleCallbacks {
    private val mHandler: Handler = Handler()
    private var startCount = 0
    private var resumeCount = 0
    private var appBackground = false
    private val mLifecycleListener: LifecycleListener = lifecycleListener
    private fun needShow(activity: Activity): Boolean {
        if (activities == null) {
            return true
        }
        for (a in activities) {
            if (a.isInstance(activity)) {
                return showFlag
            }
        }
        return !showFlag
    }

    override fun onActivityResumed(activity: Activity) {
        resumeCount++
        if (needShow(activity)) {
            mLifecycleListener.onShow()
        } else {
            mLifecycleListener.onHide()
        }
        if (appBackground) {
            appBackground = false
        }
    }

    override fun onActivityPaused(activity: Activity) {
        resumeCount--
        mHandler.postDelayed({
            if (resumeCount == 0) {
                appBackground = true
                // 不注释掉,那么悬浮窗只在该activity显示,不能在其他activity和桌面显示
                mLifecycleListener.onPostHide()
            }
        }, delay)
    }

    override fun onActivityStarted(activity: Activity) {
        startCount++
    }

    override fun onActivityStopped(activity: Activity) {
        startCount--
        if (startCount == 0) {
            mLifecycleListener.onHide()
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action != null && action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
            if (SYSTEM_DIALOG_REASON_HOME_KEY == reason) {
                mLifecycleListener.onHide()
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        private const val SYSTEM_DIALOG_REASON_KEY = "reason"
        private const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        private const val delay: Long = 300
    }

    fun unregister() {
        (applicationContext as Application).unregisterActivityLifecycleCallbacks(this)
        applicationContext.unregisterReceiver(this)
    }

    init {
        (applicationContext as Application).registerActivityLifecycleCallbacks(this)
        applicationContext.registerReceiver(this, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }
}

interface LifecycleListener {
    fun onShow()
    fun onHide()
    fun onPostHide()
}
