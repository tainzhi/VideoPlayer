package com.qfq.muqing.myvideoplayer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by muqing on 11/18/15.
 */
public class Util {
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        Log.v("qfq", "px=" + px + "dp = " + dp + "density=" + metrics.densityDpi);
        return px;
    }

    public static String THUMBNAIL_CACHE_PATH = "list_thumbnail";
    public static String FILENAME_SEPERATOR = "_";
    public static String PROGRESS_THUMB_CACHE_PATH = "pgogress_thumbnail";

    public static String VIDEO_PLAYER_SETTING_PREFERENCE = "settings";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X = "float_window_position_x";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y = "float_window_position_y";

    public static final int DISK_CACHE_SIZE = 640 * 360 * 4 * 33; //30M
    public static final String DISK_CACHE_DIR = "progress_thumbnail";


}
