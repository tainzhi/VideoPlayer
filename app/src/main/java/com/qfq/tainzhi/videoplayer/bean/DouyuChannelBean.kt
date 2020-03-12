package com.qfq.tainzhi.videoplayer.bean

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
class DouyuChannelBean constructor() {
    private var id: Int = 0
    private var name: String? = null
    private var src: String? = null
    private var url: String? = null
    private var icon: String? = null
    fun getId(): Int {
        return id
    }
    
    fun setId(id: Int) {
        this.id = id
    }
    
    fun getName(): String? {
        return name
    }
    
    fun setName(name: String?) {
        this.name = name
    }
    
    fun getSrc(): String? {
        return src
    }
    
    fun setSrc(src: String?) {
        this.src = src
    }
    
    fun getUrl(): String? {
        return url
    }
    
    fun setUrl(url: String?) {
        this.url = url
    }
    
    fun getIcon(): String? {
        return icon
    }
    
    fun setIcon(icon: String?) {
        this.icon = icon
    }
    
    public override fun toString(): String {
        return ("DouyuChannelBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", src='" + src + '\'' +
                ", url='" + url + '\'' +
                ", icon=" + icon +
                '}')
    }
}