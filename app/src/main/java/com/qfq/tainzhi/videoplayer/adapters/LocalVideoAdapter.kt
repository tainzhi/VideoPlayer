package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.LocalVideoAdapter.MyViewHolder
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean
import com.qfq.tainzhi.videoplayer.util.StringUtil
import java.io.File

/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
class LocalVideoAdapter(private val mContext: Context, private val mLists: MutableList<LocalVideoBean>) : RecyclerView.Adapter<MyViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnBottomReachedListener: OnBottomReachedListener? = null
    private fun generateItems() {
        // DATA is path
        val videoColumns = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATE_TAKEN)
        val cursor = mContext.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null)
        val totalCount = cursor!!.count
        cursor.moveToFirst()
        for (i in 0 until totalCount) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
            val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
            val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
            val size_s = StringUtil.formatMediaSize(size)
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
            // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
            val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            val resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
            val orientation = "0"
            val date_added = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
            val date_modified = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED))
            val date_taken = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN))
            val create_date = getVideoCreatDate(path)
            val thumbnailsPath = getThumbnailPath(id)
            val item = LocalVideoBean(id, path, size_s, title,
                    duration, resolution, orientation, date_added,
                    date_modified, date_taken, create_date, thumbnailsPath)
            mLists.add(item)
            cursor.moveToNext()
        }
        cursor.close()
    }

    fun getThumbnailPath(videoId: Int): String? {
        val thumbColumns = arrayOf(MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID)
        val thumbCursor = mContext.contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID +
                "=" + videoId, null, null)
        var path: String? = null
        if (thumbCursor!!.moveToFirst()) {
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

    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_local_video, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val video = mLists[position]
        if (video.thumbnailPath != null) {
            holder.thumb.setImageURI(Uri.fromFile(File(video.thumbnailPath)))
        } else {
            holder.thumb.setImageResource(R.drawable.ic_video_default_thumbnail)
        }
        holder.title.text = video.title
        holder.size.text = video.size
        holder.duration.text = StringUtil.formatMediaTime(video.duration)
        holder.date_added.text = video.createDate
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener {
                val position = holder.layoutPosition
                mOnItemClickListener!!.onItemClick(holder.itemView, position)
            }
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener {
                val position = holder.layoutPosition
                mOnItemLongClickListener!!.onItemLongClick(holder.itemView,
                        position)
                true
            }
        }
        if (position == mLists.size - 1) {
            mOnBottomReachedListener!!.onBottomReached(position)
        }
    }

    override fun getItemCount(): Int {
        return mLists.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View?, position: Int)
    }

    interface OnBottomReachedListener {
        fun onBottomReached(position: Int)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumb: ImageView
        var title: TextView
        var size: TextView
        var duration: TextView
        var date_added: TextView

        init {
            thumb = itemView.findViewById(R.id.videoThumbnailImageView)
            title = itemView.findViewById(R.id.item_video_title)
            size = itemView.findViewById(R.id.item_video_size)
            duration = itemView.findViewById(R.id.videoDurationTv)
            date_added = itemView.findViewById(R.id.item_video_date_added)
        }
    }

    init {
        generateItems()
    }
}