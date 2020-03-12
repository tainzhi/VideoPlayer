package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter.MyViewHolder
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean
import com.qfq.tainzhi.videoplayer.util.StringUtil
import java.io.File

/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
class LocalVideoAdapter constructor(private val mContext: Context?, private val mLists: MutableList<LocalVideoBean?>?) : RecyclerView.Adapter<MyViewHolder?>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnBottomReachedListener: OnBottomReachedListener? = null
    private fun generateItems() {
        // DATA is path
        val videoColumns: Array<String?>? = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATE_TAKEN)
        val cursor: Cursor? = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null)
        val totalCount: Int = cursor.getCount()
        Logger.d("local has %d videos", totalCount)
        cursor.moveToFirst()
        for (i in 0 until totalCount) {
            val id: Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
            val path: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
            val size: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
            val size_s: String? = StringUtil.formatMediaSize(size)
            val title: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
            // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
            val duration: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            val resolution: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
            val date_added: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
            val date_modified: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED))
            val date_taken: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN))
            val create_date: String? = getVideoCreatDate(path)
            val thumbnailsPath: String? = getThumbnailPath(id)
            val item: LocalVideoBean? = LocalVideoBean(id, path, size_s, title,
                                                       duration, resolution, date_added,
                                                       date_modified, date_taken, create_date, thumbnailsPath)
            mLists.add(item)
            cursor.moveToNext()
        }
        cursor.close()
    }
    
    fun getThumbnailPath(videoId: Int): String? {
        val thumbColumns: Array<String?>? = arrayOf(MediaStore.Video.Thumbnails.DATA,
                                                    MediaStore.Video.Thumbnails.VIDEO_ID)
        val thumbCursor: Cursor? = mContext.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                                                                       thumbColumns, (MediaStore.Video.Thumbnails.VIDEO_ID +
                "=" + videoId), null, null)
        var path: String? = null
        if (thumbCursor.moveToFirst()) {
            path = thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA))
        }
        return path
    }
    
    fun getVideoCreatDate(path: String?): String? {
        return null
        // Logger.d("%s", path);
        // MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        // retriever.setDataSource(path);
        // String createDate =
        //         retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        // // 解析出的时间格式为, 20180503T044105.000Z, 需转换
        // return StringUtil.formatDate(createDate);
    }
    
    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mOnItemClickListener
    }
    
    fun setOnItemLongClickListener(mOnItemLongClickListener: OnItemLongClickListener?) {
        this.mOnItemLongClickListener = mOnItemLongClickListener
    }
    
    fun setOnBottomReachedListener(mOnBottomReachedListener: OnBottomReachedListener?) {
        this.mOnBottomReachedListener = mOnBottomReachedListener
    }
    
    public override fun onCreateViewHolder(parent: ViewGroup?, ViewType: Int): MyViewHolder? {
        return MyViewHolder(LayoutInflater.from(mContext)
                                    .inflate(R.layout.item_local_video, parent, false))
    }
    
    public override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val video: LocalVideoBean? = mLists.get(position)
        if (video.getThumbnailPath() != null) {
            holder.thumb.setImageURI(Uri.fromFile(File(video.getThumbnailPath())))
        } else {
            holder.thumb.setImageResource(R.drawable.ic_video_default_thumbnail)
        }
        holder.title.setText(video.getTitle())
        holder.size.setText(video.getSize())
        holder.duration.setText(StringUtil.formatMediaTime(video.getDuration()))
        holder.date_added.setText(video.getCreateDate())
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View?) {
                    val position: Int = holder.getLayoutPosition()
                    mOnItemClickListener.onItemClick(holder.itemView, position)
                }
            })
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(object : OnLongClickListener {
                public override fun onLongClick(v: View?): Boolean {
                    val position: Int = holder.getLayoutPosition()
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,
                                                             position)
                    return true
                }
            })
        }
        if (position == mLists.size - 1) {
            mOnBottomReachedListener.onBottomReached(position)
        }
    }
    
    public override fun getItemCount(): Int {
        return mLists.size
    }
    
    open interface OnItemClickListener {
        open fun onItemClick(view: View?, position: Int)
    }
    
    open interface OnItemLongClickListener {
        open fun onItemLongClick(view: View?, position: Int)
    }
    
    open interface OnBottomReachedListener {
        open fun onBottomReached(position: Int)
    }
    
    internal inner class MyViewHolder constructor(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var thumb: ImageView?
        var title: TextView?
        var size: TextView?
        var duration: TextView?
        var date_added: TextView?
        
        init {
            thumb = itemView.findViewById(R.id.item_staggredview_thumbnail)
            title = itemView.findViewById(R.id.item_video_title)
            size = itemView.findViewById(R.id.item_video_size)
            duration = itemView.findViewById(R.id.item_video_duration)
            date_added = itemView.findViewById(R.id.item_video_date_added)
        }
    }
    
    init {
        generateItems()
    }
}