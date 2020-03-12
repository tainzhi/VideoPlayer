package com.qfq.tainzhi.videoplayer.bean

/**
 * Created by muqing on 2019/6/18.
 * Email: qfq61@qq.com
 */
class TVChannelBean constructor() {
    /**
     * url : http://223.110.245.165/ott.js.chinamobile.com/PLTV/3/224/3221226316/index.m3u8
     * name : CCTV1综合
     */
    private var url: String? = null
    private var name: String? = null
    fun getUrl(): String? {
        return url
    }
    
    fun setUrl(url: String?) {
        this.url = url
    }
    
    fun getName(): String? {
        return name
    }
    
    fun setName(name: String?) {
        this.name = name
    }
}