package com.qfq.tainzhi.videoplayer.bean;

/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
public class LocalVideoBean {
    String id;
    String title;
    String path;
    String duration;
    String size;
    String process;
    
    
    public LocalVideoBean(String mId, String mTitle, String mPath, String mDuration, String mSize, String mProcess) {
        id = mId;
        title = mTitle;
        path = mPath;
        duration = mDuration;
        size = mSize;
        process = mProcess;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String mId) {
        id = mId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String mTitle) {
        title = mTitle;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String mPath) {
        path = mPath;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String mDuration) {
        duration = mDuration;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String mSize) {
        size = mSize;
    }
    
    public String getProcess() {
        return process;
    }
    
    public void setProcess(String mProcess) {
        process = mProcess;
    }
}
