package com.tainzhi.android.videoplayer.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import java.io.PipedReader
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/9 15:43
 * @description:
 **/
 
class PreferenceRepository (val context: Context) {
    private val prefs: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(
                PREFS_NAME, MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    var advertising by BooleanPreference(prefs, PREF_ADVERTISING, false)
    var playerType by StringPreference(prefs, PREF_PLAYER, "")
    var playerRenderType by StringPreference(prefs, PREF_RENDER_TYPE, "")
    var selectedTheme by StringPreference(prefs, PREF_DARK_MODE_ENABLED, "")

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            // TODO: 2020/7/9 添加MutalabeLiveData提醒
        }
    }

    companion object {
        const val PREFS_NAME = "VideoPlayer"
        const val PREF_ADVERTISING = "pref_advertising"
        const val PREF_PLAYER = "pref_player"
        const val PREF_RENDER_TYPE = "pref_render_type"
        const val PREF_DARK_MODE_ENABLED = "pref_dark_mode"
    }
}

class BooleanPreference(
        private val preferences: Lazy<SharedPreferences>,
        private val name: String,
        private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}

class StringPreference(
        private val preferences: Lazy<SharedPreferences>,
        private val name: String,
        private val defaultValue: String
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.value.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}
