package com.qfq.tainzhi.videoplayer.bean;

import java.util.List;

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
public class DouyuChannelBean {
    private int id;
    private String  name;
    private String src;
    private String url;
    private String icon;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSrc() {
        return src;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Override
    public String toString() {
        return "DouyuChannelBean{" +
                       "id=" + id +
                       ", name='" + name + '\'' +
                       ", src='" + src + '\'' +
                       ", url='" + url + '\'' +
                       ", icon=" + icon +
                       '}';
    }
}
