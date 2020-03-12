package com.qfq.tainzhi.videoplayer.util

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.qfq.tainzhi.videoplayer.App

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
class SettingUtil constructor() {
    private val mSetting: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(App.Companion.AppContext)
    fun getIsFirstTime(): Boolean {
        return mSetting.getBoolean("first_time", true)
    }
    
    fun setIsFirstTime(flag: Boolean) {
        mSetting.edit().putBoolean("first_time", flag).apply()
    }
    
    private object SettingUtilInstance {
        private val instance: SettingUtil? = SettingUtil()
    }
    
    companion object {
        val DISK_CACHE_SIZE: Int = 640 * 360 * 4 * 33 //30M
        val DISK_CACHE_DIR: String? = "progress_thumbnail"
        var VIDEO_PLAYER_SETTING_PREFERENCE: String? = "settings"
        var VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X: String? = "float_window_position_x"
        var VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y: String? = "float_window_position_y"
        fun newInstance(): SettingUtil? {
            return SettingUtilInstance.instance
        }
    }
}