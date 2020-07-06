package com.tanzhi.qmediaplayer.floatwindow

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import kotlin.properties.Delegates

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/6 16:29
 * @description: 7.1以上需要申请权限
 **/
 
class FloatPhone(val context: Context): FloatView() {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutParams = WindowManager.LayoutParams()
    private lateinit var contentView: View

    override var x: Int = 0
        set(value) {
            layoutParams.x = 0
            field = value
        }
    override var y: Int = 0
        set(value) {
            layoutParams.y = 0
            field = value
        }
    override var gravity: Int = Gravity.BOTTOM.or(Gravity.END)
        set(value) {
            layoutParams.gravity = value
            field = value
        }
    override var width = 300
        set(value) {
            layoutParams.width = value
            field = value
        }
    override var height: Int = 400
        set(value) {
            layoutParams.height = value
            field = value
        }

    override fun show() {
        if (Settings.canDrawOverlays(context)) {
            layoutParams.format = PixelFormat.RGBA_8888
            windowManager.addView(contentView, layoutParams)
        } else {
            Log.e("FloatWindow/FloatPhone.show()", "should grand overlay permission")
        }
    }

    override fun dismiss() {
        windowManager.removeView(contentView)
    }

    override fun setView(view: View) {
        var layoutType by Delegates.notNull<Int>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL.or (WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        layoutParams.type = layoutType
        layoutParams.windowAnimations = 0
        contentView = view
    }

    override fun updateLayout() {
        windowManager.updateViewLayout(contentView, layoutParams)
    }

}