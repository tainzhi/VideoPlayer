package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by muqing on 11/18/15.
 */
public class Utils {
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        Log.v("qfq", "px=" + px + "dp = " + dp + "density=" + metrics.densityDpi);
        return px;
    }
}
