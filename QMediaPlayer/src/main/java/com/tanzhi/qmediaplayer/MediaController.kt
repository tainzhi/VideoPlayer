package com.tanzhi.qmediaplayer

import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.media.AudioManager
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/22 17:03
 * @description: 播放控制器, 添加控制按钮, 控制[VideoView]播放
 **/

class MediaController @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var anchor: View
    private lateinit var root: View
    private lateinit var windowManager: WindowManager
    private lateinit var window: Window
    private lateinit var decor: View
    private lateinit var decorLayoutParams: WindowManager.LayoutParams
    private var isShowing = false

    companion object {
        const val DefaultTimeout = 3000
    }

    init {
        initFloatingWindowLayout()
        initFloatingWindow()
    }

    private fun initFloatingWindowLayout() {
        decorLayoutParams = WindowManager.LayoutParams().apply {
            gravity = Gravity.TOP or Gravity.LEFT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = 0
            format = PixelFormat.TRANSLUCENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            flags = flags or
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_SPLIT_TOUCH
            token = null
            windowAnimations = 0
        }
    }

    private fun initFloatingWindow() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        /**
         * 初始化窗口。
         * PhoneWindow为隐藏API，可以从Activity中取。
         * 对下面用到API的代码 进行修改，但不是全部。
         */
        window = (context as Activity).window.apply {
            setWindowManager(windowManager, null, null)
            requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(this@MediaController)
            setBackgroundDrawableResource(android.R.color.transparent)
            setVolumeControlStream(AudioManager.STREAM_MUSIC)
        }
        decor = window.decorView
        decor.setOnTouchListener(this)

        focusable = View.FOCUSABLE
        isFocusableInTouchMode = true
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        requestFocus()

    }
}