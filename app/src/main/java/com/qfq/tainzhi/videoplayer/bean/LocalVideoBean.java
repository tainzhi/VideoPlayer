package com.qfq.tainzhi.videoplayer.bean;

/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
public class LocalVideoBean {
    //id, data(path), size, date_added, title, duration,resolution,
    int id;
    String path;
    String size; // long convert to 35M
    String title;
    String duration; // long convert to 100:80:00
    String resolution;
    String date_created;
    String date_modified;
    String date_taken;
    String createDate;
    String thumbnailPath;
    
    public LocalVideoBean(int mId, String mPath, String mSize, String mTitle, String mDuration, String mResolution, String mDate_created, String mDate_modified, String mDate_taken, String mCreateDate, String mThumbnailPath) {
        id = mId;
        path = mPath;
        size = mSize;
        title = mTitle;
        duration = mDuration;
        resolution = mResolution;
        date_created = mDate_created;
        date_modified = mDate_modified;
        date_taken = mDate_taken;
        createDate = mCreateDate;
        thumbnailPath = mThumbnailPath;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int mId) {
        id = mId;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String mPath) {
        path = mPath;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String mSize) {
        size = mSize;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String mTitle) {
        title = mTitle;
    }
    
    public String getDuration() {
        return duration;
    }
    
    public void setDuration(String mDuration) {
        duration = mDuration;
    }
    
    public String getResolution() {
        return resolution;
    }
    
    public void setResolution(String mResolution) {
        resolution = mResolution;
    }
    
    public String getDate_created() {
        return date_created;
    }
    
    public void setDate_created(String mDate_created) {
        date_created = mDate_created;
    }
    
    public String getDate_modified() {
        return date_modified;
    }
    
    public void setDate_modified(String mDate_modified) {
        date_modified = mDate_modified;
    }
    
    public String getDate_taken() {
        return date_taken;
    }
    
    public void setDate_taken(String mDate_taken) {
        date_taken = mDate_taken;
    }
    
    public String getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(String mCreateDate) {
        createDate = mCreateDate;
    }
    
    public String getThumbnailPath() {
        return thumbnailPath;
    }
    
    public void setThumbnailPath(String mThumbnailPath) {
        thumbnailPath = mThumbnailPath;
    }
    
    @Override
    public String toString() {
        return "LocalVideoBean{" +
                       "id=" + id +
                       ", path='" + path + '\'' +
                       ", size='" + size + '\'' +
                       ", title='" + title + '\'' +
                       ", duration='" + duration + '\'' +
                       ", resolution='" + resolution + '\'' +
                       ", date_created='" + date_created + '\'' +
                       ", date_modified='" + date_modified + '\'' +
                       ", date_taken='" + date_taken + '\'' +
                       ", createDate='" + createDate + '\'' +
                       ", thumbnailPath='" + thumbnailPath + '\'' +
                       '}';
    }
}
