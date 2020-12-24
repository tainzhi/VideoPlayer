package com.tainzhi.mediaspider.film.bean

import android.content.Context
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tainzhi.mediaspider.film.Source
import com.tainzhi.mediaspider.listFromJson
import com.tainzhi.mediaspider.readAssets
import com.tainzhi.mediaspider.readString
import java.io.File

/**
 * File:     MovieManager
 * Author:   tainzhi
 * Created:  2020/12/19 21:14
 * Mail:     QFQ61@qq.com
 * Description:
 */

@JsonClass(generateAdapter = true)
data class ConfigItem(
        val key: String,
        val name: String,
        val new: String,
        val search: String,
        val tags: List<Tag>,
        val type: Int,
        val url: String,
        val view: String
)

@JsonClass(generateAdapter = true)
data class Tag(
        val children: List<Children>,
        val id: Int,
        val title: String
)

@JsonClass(generateAdapter = true)
data class Children(
        val id: Int,
        val title: String
)

/**
 * configFile != null && configSourceFile != null 是为了方便进行本地单元测试
 * 因为本地单元测试没有android context, 无法通过 context.getAssets获取assets下的文件
 *
 * 正常使用, 在app中直接调用 MovieManager.getInstance(context)即可
 */
class MovieManager private constructor(val context: Context, val configFile: File? = null, val configSourceFile: File? = null) {
    companion object {
        @Volatile
        private var _instance: MovieManager? = null
        fun getInstance(context: Context, configFile: File? = null, configSourceFile: File? = null): MovieManager {
            return _instance ?: synchronized(this) {
                MovieManager(context, configFile, configSourceFile).also { _instance = it }
            }
        }
    }

    val configList: List<ConfigItem>? by lazy {
        if (configFile == null) {
            context.readAssets("movie_site_config.json").listFromJson<ConfigItem>()
        } else {
            configFile.readString().listFromJson<ConfigItem>()
        }
    }
    val configListMap: HashMap<String, ConfigItem> by lazy {
        val hashMap = hashMapOf<String, ConfigItem>()
        configList?.forEach {
            hashMap[it.key] = it
        }
        hashMap
    }

    val sourceConfigs: HashMap<String, SourceConfig> by lazy {
        lateinit var sites: List<SiteBean>
        if (configSourceFile == null) {
            sites = context.readAssets("movie_site.json").listFromJson<SiteBean>()
        } else {
            sites = configSourceFile.readString().listFromJson()
        }
        val configMap = hashMapOf<String, SourceConfig>()
        sites.forEach { site ->
            configMap[site.key] = SourceConfig(site.key, site.name) {
                Source(site.key, site.name, site.api, site.download)
            }
        }
        configMap
    }

    /**
     * 根据key获取相应的source
     */
    fun generateSource(key: String?): Source {
        return sourceConfigs[key]?.generateSource() ?: Source("", "", "", "")
    }

    var defaultSourceKey = "okzy"

    /**
     * 获取当前选择的源
     */
    fun curUseSourceConfig(): Source {
        return generateSource(defaultSourceKey)
    }

    fun changeSourceConfig(sourceKey: String) {
        if (sourceConfigs.containsKey(sourceKey)) {
            defaultSourceKey = sourceKey
        }
    }
}

data class SourceConfig(val key: String, val name: String, val generate: () -> Source) {
    fun generateSource(): Source {
        return generate.invoke()
    }
}

@JsonClass(generateAdapter = true)
data class SiteBean(
        @Json(name = "api")
        val api: String,
        @Json(name = "download")
        val download: String,
        @Json(name = "id")
        val id: Int,
        @Json(name = "key")
        val key: String,
        @Json(name = "name")
        val name: String
)