package com.tainzhi.mediaspider.movie

import com.tainzhi.mediaspider.movie.bean.Classify
import com.tainzhi.mediaspider.movie.bean.DetailData
import com.tainzhi.mediaspider.movie.bean.DownloadData
import com.tainzhi.mediaspider.movie.bean.Episode
import com.tainzhi.mediaspider.movie.bean.HomeChannelData
import com.tainzhi.mediaspider.movie.bean.HomeData
import com.tainzhi.mediaspider.movie.bean.NewMovie
import com.tainzhi.mediaspider.movie.bean.SearchResultData
import com.tainzhi.mediaspider.utils.OkHttpUtil
import com.tainzhi.mediaspider.utils.Util
import com.tainzhi.mediaspider.utils.isVideoUrl
import org.json.JSONArray
import org.json.JSONObject

/**
 * File:     BaseSource
 * Author:   tainzhi
 * Created:  2020/12/16 20:54
 * Mail:     QFQ61@qq.com
 * Description:
 */
class Source(
        val key: String,
        val name: String,
        val baseUrl: String,
        val downloadBaseUrl: String,
) {

    //请求首页数据
    fun requestHomeData(callback: (t: HomeData?) -> Unit) {
        OkHttpUtil.instance.request(baseUrl,
                success = {
                    callback.invoke(parseHomeData(it))
                },
                error = {
                    it
                }
        )
    }

    //请求频道列表数据
    fun requestHomeChannelData(
            page: Int,
            tid: String,
            callback: (t: ArrayList<HomeChannelData>?) -> Unit
    ) {
        OkHttpUtil.instance.request(
                if (tid == "new") "$baseUrl?ac=videolist&pg=$page" else "$baseUrl?ac=videolist&t=$tid&pg=$page",
                success = {
                    callback.invoke(parseHomeChannelData(it))
                },
                error = {

                }

        )
    }

    //请求搜索数据
    fun requestSearchData(
            searchWord: String,
            page: Int,
            callback: (t: ArrayList<SearchResultData>?) -> Unit
    ) {
        OkHttpUtil.instance.request(
                "$baseUrl?wd=$searchWord&pg=$page",
                success = {
                    callback.invoke(parseSearchResultData(it))
                },
                error = {

                }

        )
    }

    //请求详情数据
    fun requestDetailData(id: String, callback: (t: DetailData?) -> Unit) {
        OkHttpUtil.instance.request(
                "$baseUrl?ac=videolist&ids=$id",
                success = {
                    callback.invoke(parseDetailData(key, it))
                },
                error = {

                }
        )
    }


    //请求下载列表
    fun requestDownloadData(id: String, callback: (t: ArrayList<DownloadData>?) -> Unit) {
        OkHttpUtil.instance.request(
                "$downloadBaseUrl?ac=videolist&ids=$id",
                success = {
                    callback.invoke(parseDownloadData(it))
                },
                error = {

                }
        )
    }


    //以下为解析数据
    private fun parseHomeData(data: String?): HomeData? {
        try {
            if (data == null) return null
            val jsonObject = Util.xmlToJson(data)?.toJson()
            jsonObject?.getJSONObject("rss")?.run {
                val videoList = ArrayList<NewMovie>()
                val video = getJSONObject("list").get("video")
                try {
                    if (video is JSONObject) {
                        videoList.add(
                                NewMovie(
                                        video.getString("last"),
                                        video.getString("id"),
                                        video.getString("tid"),
                                        video.getString("name"),
                                        video.getString("type")
                                )
                        )
                    } else if (video is JSONArray) {
                        for (i in 0 until video.length()) {
                            val json = video.getJSONObject(i)
                            videoList.add(
                                    NewMovie(
                                            json.getString("last"),
                                            json.getString("id"),
                                            json.getString("tid"),
                                            json.getString("name"),
                                            json.getString("type")
                                    )
                            )
                        }
                    }
                } catch (e: Exception) {
                }
                val classifyList = ArrayList<Classify>()
                try {
                    val classList = getJSONObject("class").getJSONArray("ty")
                    for (i in 0 until classList.length()) {
                        val json = classList.getJSONObject(i)
                        classifyList.add(
                                Classify(
                                        json.getString("id"),
                                        json.getString("content")
                                )
                        )
                    }
                } catch (e: Exception) {
                }
                return HomeData(videoList, classifyList)
            }
        } catch (e: Exception) {
        }
        return null
    }

    private fun parseHomeChannelData(data: String?): ArrayList<HomeChannelData>? {
        try {
            if (data == null) return null
            val jsonObject = Util.xmlToJson(data)?.toJson()
            val videoList = ArrayList<HomeChannelData>()
            val videos =
                    jsonObject?.getJSONObject("rss")?.getJSONObject("list")!!.getJSONArray("video")
            for (i in 0 until videos.length()) {
                val json = videos.getJSONObject(i)
                videoList.add(
                        HomeChannelData(
                                json.getString("id"),
                                json.getString("name"),
                                json.getString("pic")
                        )
                )
            }
            return videoList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayListOf()
    }

    private fun parseSearchResultData(data: String?): ArrayList<SearchResultData>? {
        try {
            if (data == null) return null
            val jsonObject = Util.xmlToJson(data)?.toJson()
            val videoList = ArrayList<SearchResultData>()
            val video =
                    jsonObject?.getJSONObject("rss")?.getJSONObject("list")?.get("video")
            if (video is JSONObject) {
                videoList.add(
                        SearchResultData(
                                video.getString("id"),
                                video.getString("name"),
                                video.getString("type")
                        )
                )
            } else if (video is JSONArray) {
                for (i in 0 until video.length()) {
                    val json = video.getJSONObject(i)
                    videoList.add(
                            SearchResultData(
                                    json.getString("id"),
                                    json.getString("name"),
                                    json.getString("type")
                            )
                    )
                }
            }
            return videoList
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayListOf()
    }

    private fun parseDetailData(sourceKey: String, data: String?): DetailData? {
        try {
            if (data == null) return null
            val jsonObject = Util.xmlToJson(data)?.toJson()
            val videoInfo =
                    jsonObject?.getJSONObject("rss")?.getJSONObject("list")!!.getJSONObject("video")
            val dd = videoInfo.getJSONObject("dl").get("dd")
            var episodeList: ArrayList<Episode>? = null
            if (dd is JSONObject) {
                episodeList = dd.getString(("content"))?.split("#")
                        ?.map {
                            val split = it.split("$")
                            if (split.size >= 2) {
                                Episode(split[0], split[1])
                            } else {
                                Episode(split[0], split[0])
                            }
                        }?.toMutableList() as ArrayList<Episode>? ?: arrayListOf()
            } else if (dd is JSONArray) {
                for (i in 0 until dd.length()) {
                    val list = dd.getJSONObject(i)?.getString("content")?.split("#")
                            ?.map {
                                val split = it.split("$")
                                if (split.size >= 2) {
                                    Episode(split[0], split[1])
                                } else {
                                    Episode(split[0], split[0])
                                }
                            }?.toMutableList() as ArrayList<Episode>? ?: arrayListOf()
                    if (list.size > 0) {
                        episodeList = list
                        if (list[0].playUrl.isVideoUrl()) {
                            //优先获取应用内播放的资源
                            break
                        }
                    }
                }
            }
            return DetailData(
                    videoInfo.getString("id"),
                    videoInfo.getString("tid"),
                    videoInfo.getString("name"),
                    videoInfo.getString("type"),
                    videoInfo.getString("lang"),
                    videoInfo.getString("area"),
                    videoInfo.getString("pic"),
                    videoInfo.getString("year"),
                    videoInfo.getString("actor"),
                    videoInfo.getString("director"),
                    videoInfo.getString("des"),
                    episodeList,
                    sourceKey
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun parseDownloadData(data: String?): ArrayList<DownloadData>? {
        try {
            if (data == null) return null
            val jsonObject = Util.xmlToJson(data)?.toJson()
            val video =
                    jsonObject?.getJSONObject("rss")?.getJSONObject("list")!!.getJSONObject("video")

            return video.getJSONObject("dl").getJSONObject("dd").getString("content")?.split("#")
                    ?.map {
                        val split = it.split("$")
                        if (split.size >= 2) {
                            DownloadData(split[0], split[1])
                        } else {
                            DownloadData(split[0], split[0])
                        }
                    }?.toMutableList() as ArrayList<DownloadData>? ?: arrayListOf()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayListOf()
    }
}