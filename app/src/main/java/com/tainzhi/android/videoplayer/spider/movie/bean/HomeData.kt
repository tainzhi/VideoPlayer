package com.tainzhi.android.videoplayer.spider.movie.bean

/**
 * File:     DetailBean
 * Author:   tainzhi
 * Created:  2020/12/17 21:21
 * Mail:     QFQ61@qq.com
 * Description:
 */

data class HomeData(val videoList: ArrayList<NewVideo>, val classifyList: ArrayList<Classify>)

//新更新的资源
data class NewVideo(
    //更新时间
    val updateTime: String?,
    //视频id
    val id: String?,
    //分类id
    val tid: String?,
    //名字
    val name: String?,
    //视频类型，国产剧
    val type: String?
)

//分类
data class Classify(
    //分类id
    val id: String?,
    //分类名
    val name: String?
)