package com.qfq.tainzhi.videoplayer.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by muqing on 2019/6/16.
 * Email: qfq61@qq.com
 */
object WindowUtil {
    /**
     * dip转为PX
     */
    fun dip2px(context: Context?, dipValue: Float): Int {
        val fontScale: Float = context.getResources().getDisplayMetrics().density
        return (dipValue * fontScale + 0.5f) as Int
    }
    
    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp
     */
    fun px2dip(context: Context?, pxValue: Float): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (pxValue / scale + 0.5f) as Int
    }
    
    fun getScreenSize(context: Context?): IntArray? {
        val wm: WindowManager? = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val displayMetrics: DisplayMetrics? = DisplayMetrics()
        wm.getDefaultDisplay().getMetrics(displayMetrics)
        return intArrayOf(displayMetrics.widthPixels,
                          displayMetrics.heightPixels)
    }
}