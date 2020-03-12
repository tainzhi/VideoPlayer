package com.qfq.tainzhi.videoplayer.adapters

import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.StaggeredAdapter.VerticalItemHolder
import com.qfq.tainzhi.videoplayer.callbacks.OnStaggeredAdapterInformation
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Administrator on 2015/11/15.
 */
class StaggeredAdapter constructor(private val mContext: Context?, private val mThumbnailParentWidth: Int, private val mOnStaggeredAdapterInformation: OnStaggeredAdapterInformation?) : RecyclerView.Adapter<VerticalItemHolder?>() {
    private val mItems: ArrayList<VideoItem?>?
    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null
    private var mOnItemLongClickListener: AdapterView.OnItemLongClickListener? = null
    private val mDefaultThumbnailBitmap: Bitmap?
    fun removeItem(position: Int) {
        if (position >= mItems.size) return
        mItems.removeAt(position)
        notifyItemRemoved(position)
    }
    
    fun getVideoItemAtPosition(position: Int): VideoItem? {
        return mItems.get(position)
    }
    
    public override fun onCreateViewHolder(container: ViewGroup?, viewType: Int): VerticalItemHolder? {
        val layoutInflater: LayoutInflater? = LayoutInflater.from(container.getContext())
        val root: View? = layoutInflater.inflate(R.layout.item_local_video, container, false)
        return VerticalItemHolder(root, this)
    }
    
    public override fun getItemCount(): Int {
        return mItems.size
    }
    
    public override fun onBindViewHolder(itemHolder: VerticalItemHolder?, position: Int) {
        val thumbnailView: View? = itemHolder.videoThumbnail
        if (position % 4 == 0) {
            thumbnailView.setMinimumHeight(300)
        } else {
            thumbnailView.setMinimumHeight(100)
        }
        val item: VideoItem? = mItems.get(position)
        itemHolder.setVideoTitle(item.videoName)
        itemHolder.setVideoSize(item.videoSize.toString() + "")
        itemHolder.setVideoDuration(item.videoDuration)
        itemHolder.setVideoProgress(item.videoProgress)
        loadThumbnailBitmap(item.videoId,
                            item.videoDuration,
                            item.videoProgress,
                            item.videoPath,
                            itemHolder.getVideoThumbnail())
    }
    
    class VerticalItemHolder constructor(
            v: View?,
            adapter: StaggeredAdapter?) : RecyclerView.ViewHolder(v), View.OnClickListener, OnLongClickListener {
        private val videoTitle: TextView?
        private val videoSize: TextView?
        private val videoDuration: TextView?
        private val videoProgress: TextView?
        private val videoThumbnail: ImageView?
        private val mAdapter: StaggeredAdapter?
        public override fun onClick(v: View?) {
            mAdapter.onItemHolderClick(this)
        }
        
        public override fun onLongClick(v: View?): Boolean {
            mAdapter.onItemHolderLongClick(this)
            return true
        }
        
        fun setVideoThumbnail(videoThumbnail: Drawable?) {
            this.videoThumbnail.setImageDrawable(videoThumbnail)
        }
        
        fun getVideoThumbnail(): ImageView? {
            return videoThumbnail
        }
        
        fun setVideoThumbnail(thumbnailBitmap: Bitmap?) {
            videoThumbnail.setImageBitmap(thumbnailBitmap)
        }
        
        fun setVideoTitle(videoTitle: String?) {
            this.videoTitle.setText(videoTitle)
        }
        
        fun setVideoSize(videoSize: String?) {
            this.videoSize.setText(videoSize)
        }
        
        fun setVideoDuration(videoDuration: Long) {
            this.videoDuration.setText(java.lang.Long.toString(videoDuration))
        }
        
        fun setVideoProgress(videoProgress: Long) {
            this.videoProgress.setText(java.lang.Long.toString(videoProgress))
        }
        
        init {
            v.setOnClickListener(this)
            v.setOnLongClickListener(this)
            mAdapter = adapter
            videoThumbnail = v.findViewById<View?>(R.id.item_staggredview_thumbnail) as ImageView?
            videoTitle = v.findViewById<View?>(R.id.item_video_title) as TextView?
            videoSize = v.findViewById<View?>(R.id.item_video_size) as TextView?
            videoDuration = v.findViewById<View?>(R.id.item_video_duration) as TextView?
            videoProgress = v.findViewById<View?>(R.id.item_video_date_added) as TextView?
        }
    }
    
    class VideoItem constructor(var videoId: Int, var videoPath: String?, var videoName: String?, var videoSize: Int, var videoDuration: Long) {
        var videoProgress: Long = 0
        
    }
    
    private fun onItemHolderClick(itemHolder: VerticalItemHolder?) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                                             itemHolder.getAdapterPosition(), mItems.get(itemHolder.getAdapterPosition()).videoId.toLong())
        }
    }
    
    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener?) {
        mOnItemClickListener = listener
    }
    
    private fun onItemHolderLongClick(itemHolder: VerticalItemHolder?) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(null, itemHolder.itemView,
                                                     itemHolder.getAdapterPosition(), mItems.get(itemHolder.getAdapterPosition()).videoId.toLong())
        }
    }
    
    fun setOnItemLongClickListener(listener: AdapterView.OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }
    
    private fun generateItems() {
        // DATA is path
        val videoColumns: Array<String?>? = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION)
        val cursor: Cursor? = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null)
        val totalCount: Int = cursor.getCount()
        // no video, then show the no video hint
        if (totalCount <= 0) {
            mOnStaggeredAdapterInformation.onStaggeredAdapterInformation()
        }
        Log.i("totalCount.........", "count")
        cursor.moveToFirst()
        for (i in 0 until totalCount) {
            val videoId: Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
            val videoPath: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            val videoDuration: Long = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            val videoTitle: String? = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
            val videoSize: Int = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
            val item: VideoItem? = VideoItem(videoId, videoPath, videoTitle, videoSize, videoDuration)
            mItems.add(item)
            cursor.moveToNext()
        }
        cursor.close()
    }
    
    private fun scaleBitmap(originalBitmap: Bitmap?, toWidth: Int, toHeight: Int): Bitmap? {
        val scaleWidth: Float = (toWidth as Float) / originalBitmap.getWidth()
        val scaleHeight: Float = (toHeight as Float) / originalBitmap.getHeight()
        
        //        float scale = 0;
        //        if (scaleWidth < scaleHeight) {
        //            scale = scaleWidth;
        //        } else {
        //            scale = scaleHeight;
        //        }
        val matrix: Matrix? = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                                   originalBitmap.getHeight(), matrix, true)
    }
    
    private fun loadThumbnailBitmap(videoId: Int, videoDuration: Long, videoProgress: Long, videoPath: String?, thumbnailView: ImageView?) {
        if (cancelPotionalWork(videoId, thumbnailView)) {
            val task: ThumbnailBitmapWorkTask? = ThumbnailBitmapWorkTask(videoId, videoDuration, videoProgress, videoPath, thumbnailView)
            val asyncDrawable: AsyncDrawable? = AsyncDrawable(mContext.getResources(),
                                                              mDefaultThumbnailBitmap, task)
            thumbnailView.setImageDrawable(asyncDrawable)
            task.execute(videoId)
        }
    }
    
    internal inner class ThumbnailBitmapWorkTask constructor(videoId: Int, videoDuration: Long, videoProgress: Long, videoPath: String?, imageView: ImageView?) : AsyncTask<Int?, Void?, Bitmap?>() {
        private val imageViewWeakReference: WeakReference<ImageView?>?
        private val videoId: Int
        private val videoDuration: Long
        private val videoProgress: Long
        private val videoPath: String?
        override fun doInBackground(vararg params: Int?): Bitmap? {
            var bitmap: Bitmap? = null
            val thumbnailParent: File? = File(mContext.getExternalCacheDir(), "/list_thumbnail")
            if (!thumbnailParent.exists()) {
                thumbnailParent.mkdir()
            }
            val videoThumbnailPathName: String? = mContext.getExternalCacheDir().toString() + "/list_thumbnail/" + videoId + "_" + videoProgress
            val file: File? = File(videoThumbnailPathName)
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(videoThumbnailPathName)
            } else {
                bitmap = createVideoThumbnail(videoId, videoProgress)
                saveVideoThumbnail(bitmap, videoThumbnailPathName)
            }
            return bitmap
        }
        
        override fun onPostExecute(bitmap: Bitmap?) {
            if (imageViewWeakReference != null && bitmap != null) {
                val imageView: ImageView? = imageViewWeakReference.get()
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap)
                }
            }
        }
        
        /**
         *
         * @param videoId
         * @param videoProgress
         * @return
         */
        private fun createVideoThumbnail(videoId: Int, videoProgress: Long): Bitmap? {
            var mediaMetadataRetriever: MediaMetadataRetriever? = null
            var srcBitmap: Bitmap? = null
            try {
                mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(mContext,
                                                     ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId.toLong()))
                srcBitmap = mediaMetadataRetriever.getFrameAtTime(videoProgress * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
            } catch (e: Exception) {
                Log.e(TAG, "counldn't get frame of " + videoPath)
            } finally {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release()
                }
            }
            if (srcBitmap == null) {
                srcBitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(), videoId.toLong(),
                                                                     MediaStore.Video.Thumbnails.MINI_KIND, null)
                if (srcBitmap == null) {
                    srcBitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(), videoId.toLong(),
                                                                         MediaStore.Video.Thumbnails.MICRO_KIND, null)
                }
            }
            return scaleBitmap(srcBitmap, mThumbnailParentWidth, mThumbnailParentWidth)
        }
        
        private fun saveVideoThumbnail(bitmap: Bitmap?, thumbnailPath: String?) {
            Log.v(TAG, "saveVideoThumbnail(), thumbnailPath=" + thumbnailPath)
            try {
                val thumbnailFile: File? = File(thumbnailPath)
                val fileOutputStream: FileOutputStream? = FileOutputStream(thumbnailFile)
                bitmap.compress(CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: IOException) {
                Log.e(TAG, "IOException")
            }
        }
        
        init {
            imageViewWeakReference = WeakReference(imageView)
            this.videoId = videoId
            this.videoDuration = videoDuration
            this.videoProgress = videoDuration / 2
            this.videoPath = videoPath
        }
    }
    
    internal class AsyncDrawable constructor(
            res: Resources?, bitmap: Bitmap?,
            thumbnailBitmapWorkTask: ThumbnailBitmapWorkTask?) : BitmapDrawable(res, bitmap) {
        private val thumbnailBitmapWorkTaskWeakReference: WeakReference<ThumbnailBitmapWorkTask?>?
        fun getThumbnailBitmapWorkTask(): ThumbnailBitmapWorkTask? {
            return thumbnailBitmapWorkTaskWeakReference.get()
        }
        
        init {
            thumbnailBitmapWorkTaskWeakReference = WeakReference(thumbnailBitmapWorkTask)
        }
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/StaggeredAdapter"
        private fun cancelPotionalWork(videoId: Int, imageView: ImageView?): Boolean {
            val thumbnailBitmapWorkTask: ThumbnailBitmapWorkTask? = getThumbnailBitmapWorkTask(imageView)
            if (thumbnailBitmapWorkTask != null) {
                val thumnailVideoId: Int = thumbnailBitmapWorkTask.videoId
                if (videoId != thumnailVideoId) {
                    thumbnailBitmapWorkTask.cancel(true)
                } else {
                    return false
                }
            }
            return true
        }
        
        private fun getThumbnailBitmapWorkTask(imageView: ImageView?): ThumbnailBitmapWorkTask? {
            if (imageView != null) {
                val drawable: Drawable? = imageView.getDrawable()
                if (drawable is AsyncDrawable) {
                    val asyncDrawable: AsyncDrawable? = drawable as AsyncDrawable?
                    return asyncDrawable.getThumbnailBitmapWorkTask()
                }
            }
            return null
        }
    }
    
    init {
        mItems = ArrayList()
        mDefaultThumbnailBitmap = scaleBitmap(
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.thumbnail_default),
                mThumbnailParentWidth,
                mThumbnailParentWidth)
        generateItems()
    }
}