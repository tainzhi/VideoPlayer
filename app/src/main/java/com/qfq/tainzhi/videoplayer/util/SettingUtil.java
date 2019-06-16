package com.qfq.tainzhi.videoplayer.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qfq.tainzhi.videoplayer.App;

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
public class SettingUtil {
    
    public static final int DISK_CACHE_SIZE = 640 * 360 * 4 * 33; //30M
    public static final String DISK_CACHE_DIR = "progress_thumbnail";
    public static String VIDEO_PLAYER_SETTING_PREFERENCE = "settings";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X = "float_window_position_x";
    public static String VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y = "float_window_position_y";
    
    private SharedPreferences mSetting =
            PreferenceManager.getDefaultSharedPreferences(App.AppContext);
    
    public static SettingUtil getInstance() {
        return SettingUtilInstance.instance;
    }
    
    public boolean getIsFirstTime() {
        return mSetting.getBoolean("first_time", true);
    }
    
    public void setIsFirstTime(boolean flag) {
        mSetting.edit().putBoolean("first_time", flag).apply();
    }
    
    private static final class SettingUtilInstance {
        private static final SettingUtil instance = new SettingUtil();
    }
}
