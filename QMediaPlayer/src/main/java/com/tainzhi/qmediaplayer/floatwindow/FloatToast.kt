package com.tainzhi.qmediaplayer.floatwindow

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import java.lang.reflect.Method

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/6 16:10
 * @description: 自定义toast方式, 无需申请权限
 **/

class FloatToast(val context: Context, val view: View) : FloatView(){

    private val toast: Toast = Toast(context)
    private lateinit var tn: Any
    private lateinit var show: Method
    private lateinit var hide: Method

    init {
        toast.view = view
        initTN()
    }

    override var gravity: Int = 0
        set(value) {
            toast.setGravity(value, x, y)
            field = value
        }

    override fun show() {
        try {
            show.invoke(tn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            hide.invoke(tn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override var visible: Boolean = false
        set(value) {
            view.visibility = if (value) View.VISIBLE else View.INVISIBLE
            field = value
        }

    private fun initTN() {
        try {
            val tnField = toast.javaClass.getDeclaredField("mTN")
            tnField.isAccessible = true
            tn = tnField.get(toast)
            show = tn.javaClass.getMethod("show")
            hide = tn.javaClass.getMethod("hide")

            val tnParamsField = tn.javaClass.getDeclaredField("mParams")
            tnParamsField.isAccessible = true
            val params = tnParamsField.get(tn) as WindowManager.LayoutParams
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            params.width = width
            params.height = height
            params.windowAnimations = 0
            val tnNextViewField = tn.javaClass.getDeclaredField("mNextView")
            tnNextViewField.isAccessible = true
            tnNextViewField.set(tn, toast.view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateLayout() {
        TODO("Not yet implemented")
    }

    override fun postHide() {
        view.post { visible = false }
    }
}