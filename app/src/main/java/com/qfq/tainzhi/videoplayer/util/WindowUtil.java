package com.qfq.tainzhi.videoplayer.util;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.MonthDisplayHelper;
import android.view.WindowManager;

/**
 * Created by muqing on 2019/6/16.
 * Email: qfq61@qq.com
 */
public class WindowUtil {
    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }
    
    /**
     * 根据手机的分辨率从 px 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    public static int[] getScreenSize(Context context) {
        WindowManager wm =
                (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return new int[] {displayMetrics.widthPixels,
                displayMetrics.heightPixels};
    }
}
