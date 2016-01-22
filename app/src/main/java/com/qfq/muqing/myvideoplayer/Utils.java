package com.qfq.muqing.myvideoplayer;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

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

    public static String THUMBNAIL_CACHE_PATH = "list_thumbnail";
    public static String FILENAME_SEPERATOR = "_";

    public static String VIDEO_PLAYER_SETTING_PREFERENCE = "settings";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X = "float_window_position_x";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y = "float_window_position_y";
}
