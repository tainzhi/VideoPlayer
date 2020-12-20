package com.tainzhi.mediaspider.film.bean

/**
 * File:     DetailData
 * Author:   tainzhi
 * Created:  2020/12/16 20:55
 * Mail:     QFQ61@qq.com
 * Description:
 */
data class DetailData(
        //视频id
        val id: String?,
        //分类id
        val tid: String?,
        //名字
        val name: String?,
        //类型
        val type: String?,
        //语言
        val lang: String?,
        //地区
        val area: String?,
        //图片
        val pic: String?,
        //上映年份
        val year: String?,
        //主演
        val actor: String?,
        //导演
        val director: String?,
        //简介
        val des: String?,
        //播放列表
        val videoList: ArrayList<Video>?,
        //所属视频源的key
        val sourceKey: String?
)

data class Video(
        val name: String?,
        val playUrl: String?
)