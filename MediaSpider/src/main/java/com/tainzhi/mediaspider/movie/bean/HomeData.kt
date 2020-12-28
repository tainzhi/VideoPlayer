package com.tainzhi.mediaspider.movie.bean

/**
 * File:     HomeData
 * Author:   tainzhi
 * Created:  2020/12/16 20:56
 * Mail:     QFQ61@qq.com
 * Description:
 */
data class HomeData(val movieList: ArrayList<NewMovie>, val classifyList: ArrayList<Classify>)

//新更新的资源
data class NewMovie(
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
        val id: String,
        //分类名
        val name: String
)