package com.qfq.tainzhi.videoplayer.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.qfq.tainzhi.videoplayer.App;

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
public class SettingUtil {
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
