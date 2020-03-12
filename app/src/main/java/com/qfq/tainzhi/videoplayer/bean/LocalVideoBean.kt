package com.qfq.tainzhi.videoplayer.bean

/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
class LocalVideoBean constructor(
        //id, data(path), size, date_added, title, duration,resolution,
        var id: Int, var path: String?, // long convert to 35M
        var size: String?, var title: String?,
        //多少秒
        var duration: Long, var resolution: String?,
        var date_created: String?, var date_modified: String?, var date_taken: String?, var createDate: String?, var thumbnailPath: String?) {
    fun getId(): Int {
        return id
    }
    
    fun setId(mId: Int) {
        id = mId
    }
    
    fun getPath(): String? {
        return path
    }
    
    fun setPath(mPath: String?) {
        path = mPath
    }
    
    fun getSize(): String? {
        return size
    }
    
    fun setSize(mSize: String?) {
        size = mSize
    }
    
    fun getTitle(): String? {
        return title
    }
    
    fun setTitle(mTitle: String?) {
        title = mTitle
    }
    
    fun getDuration(): Long {
        return duration
    }
    
    fun setDuration(mDuration: Long) {
        duration = mDuration
    }
    
    fun getResolution(): String? {
        return resolution
    }
    
    fun setResolution(mResolution: String?) {
        resolution = mResolution
    }
    
    fun getDate_created(): String? {
        return date_created
    }
    
    fun setDate_created(mDate_created: String?) {
        date_created = mDate_created
    }
    
    fun getDate_modified(): String? {
        return date_modified
    }
    
    fun setDate_modified(mDate_modified: String?) {
        date_modified = mDate_modified
    }
    
    fun getDate_taken(): String? {
        return date_taken
    }
    
    fun setDate_taken(mDate_taken: String?) {
        date_taken = mDate_taken
    }
    
    fun getCreateDate(): String? {
        return createDate
    }
    
    fun setCreateDate(mCreateDate: String?) {
        createDate = mCreateDate
    }
    
    fun getThumbnailPath(): String? {
        return thumbnailPath
    }
    
    fun setThumbnailPath(mThumbnailPath: String?) {
        thumbnailPath = mThumbnailPath
    }
    
    public override fun toString(): String {
        return ("LocalVideoBean{" +
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
                '}')
    }
    
}