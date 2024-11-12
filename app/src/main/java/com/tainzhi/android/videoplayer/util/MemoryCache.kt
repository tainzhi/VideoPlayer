package com.tainzhi.android.videoplayer.util

import android.text.TextUtils
import androidx.collection.LruCache

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/3/10 下午3:16
 * @description:
 **/

class MemoryCache {
    private val cache: LruCache<String, Any>

    @Synchronized
    fun put(key: String?, value: Any?) {
        if (TextUtils.isEmpty(key)) return
        if (cache[key!!] != null) {
            cache.remove(key)
        }
        cache.put(key, value!!)
    }

    operator fun get(key: String?): Any? {
        return cache[key!!]
    }

    @Synchronized
    operator fun <T> get(key: String?, clazz: Class<T>?): T? {
        try {
            return cache[key!!] as T?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun remove(key: String?) {
        if (cache[key!!] != null) {
            cache.remove(key)
        }
    }

    operator fun contains(key: String?): Boolean {
        return cache[key!!] != null
    }

    fun clear() {
        cache.evictAll()
    }

    companion object {
        const val KEY_UPDATE_INFO = "KEY_UPDATE_INFO"
        var instance: MemoryCache? = null
            get() {
                if (field == null) {
                    synchronized(MemoryCache::class.java) {
                        if (field == null) {
                            field = MemoryCache()
                        }
                    }
                }
                return field
            }
            private set
    }

    init {
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        val cacheSize = maxMemory / 8
        cache = LruCache(cacheSize)
    }
}
