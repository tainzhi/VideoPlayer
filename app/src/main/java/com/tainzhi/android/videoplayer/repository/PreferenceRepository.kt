package com.tainzhi.android.videoplayer.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tainzhi.android.videoplayer.util.Theme
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
    var playerType by IntPreference(prefs, PREF_PLAYER, 0)
    var playerRenderType by IntPreference(prefs, PREF_RENDER_TYPE, 0)
    var selectedTheme by IntPreference(prefs, PREF_THEME, Theme.MODE_NIGHT_NO)

    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PREF_THEME -> {
                _theme.value = selectedTheme
            }
        }
    }

    private val _theme = MutableLiveData(selectedTheme)
    val theme: LiveData<Int> = _theme

    companion object {

        const val PREFS_NAME = "VideoPlayer"
        const val PREF_THEME = "pref_theme"
        const val PREF_ADVERTISING = "pref_advertising"
        const val PREF_PLAYER = "pref_player"
        const val PREF_RENDER_TYPE = "pref_render_type"
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

class IntPreference(
        private val preferences: Lazy<SharedPreferences>,
        private val name: String,
        private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value ?: defaultValue) }
    }
}
