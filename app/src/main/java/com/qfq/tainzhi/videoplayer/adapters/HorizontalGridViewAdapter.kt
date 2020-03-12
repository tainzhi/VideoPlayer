package com.qfq.tainzhi.videoplayer.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.adapters.HorizontalGridViewAdapter.HorizontalViewHolder
import com.qfq.tainzhi.videoplayer.util.DiskLruImageCache
import com.qfq.tainzhi.videoplayer.util.SettingUtil
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Administrator on 2016/1/24.
 */
class HorizontalGridViewAdapter constructor(
        private val mContext: Context?, private val mVideoUri: Uri?, private val mVideoDuration: Int, private val mVideoProgress: Int,
        private val mProgressThumbWidth: Int,
        private val mProgresThumbHeight: Int) : RecyclerView.Adapter<HorizontalViewHolder?>() {
    // FIXME: 2016/3/9
    // The cache must be cleared before activity finished
    private val mDiskLruCache: DiskLruImageCache?
    private val mAdapter: HorizontalGridViewAdapter? = this
    private val mThumbTaskRefereceHashMap: WeakHashMap<ImageView?, ThumbnailBitmapWorkTask?>? = WeakHashMap()
    private val mHorizontalViewHolderReferenceHashMap: WeakHashMap<ImageView?, HorizontalViewHolder?>? = WeakHashMap()
    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null
    private val mThumbPosition: IntArray? = IntArray(THUMB_COUNT + 1)
    public override fun onCreateViewHolder(container: ViewGroup?, valueType: Int): HorizontalViewHolder? {
        val inflater: LayoutInflater? = LayoutInflater.from(container.getContext())
        val root: View? = inflater.inflate(R.layout.item_horizontal_videoprogress_window, container, false)
        return HorizontalViewHolder(root)
    }
    
    public override fun onBindViewHolder(viewHolder: HorizontalViewHolder?, position: Int) {
        val holder: RecyclerView.ViewHolder? = viewHolder
        val imageView: ImageView? = viewHolder.mThumbView
        viewHolder.mThumbView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                onItemHolderClick(position, mThumbPosition.get(position))
            }
        })
        loadThumbnailBitmap(viewHolder, mVideoUri, position, mThumbPosition.get(position), imageView)
    }
    
    public override fun getItemCount(): Int {
        return THUMB_COUNT + 1
    }
    
    private fun onItemHolderClick(position: Int, videoProgress: Int) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, null,
                                             position, videoProgress.toLong())
        }
    }
    
    fun setOnItemClickListener(listener: AdapterView.OnItemClickListener?) {
        mOnItemClickListener = listener
    }
    
    private fun loadThumbnailBitmap(viewHolder: HorizontalViewHolder?, uri: Uri?, index: Int, progress: Int, thumbnailView: ImageView?) {
        Log.v(TAG, "position=" + index + ", progress=" + progress)
        if (!mThumbTaskRefereceHashMap.containsKey(thumbnailView)) {
            Log.v(TAG, "loadThumbnailBitmpa enter")
            val task: ThumbnailBitmapWorkTask? = ThumbnailBitmapWorkTask(uri, index, thumbnailView)
            mThumbTaskRefereceHashMap.put(thumbnailView, task)
            mHorizontalViewHolderReferenceHashMap.put(thumbnailView, viewHolder)
            task.execute(progress)
        }
    }
    
    private fun createBitmap(videoUri: Uri?, videoProgress: Long): Bitmap? {
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        var srcBitmap: Bitmap? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(mContext, videoUri)
            srcBitmap = mediaMetadataRetriever.getFrameAtTime(videoProgress * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            Log.e(TAG, "counldn't get frame at " + videoProgress)
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release()
            }
        }
        if (null == srcBitmap) {
            return null
        } else {
            return scaleBitmap(srcBitmap, mProgressThumbWidth, mProgresThumbHeight)
        }
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
    
    inner class HorizontalViewHolder constructor(v: View?) : RecyclerView.ViewHolder(v) {
        var mThumbView: ImageView?
        var mThumbLoadingBar: ProgressBar?
        
        init {
            mThumbView = v.findViewById<View?>(R.id.item_horizontal_videoprogress_id) as ImageView?
            mThumbLoadingBar = v.findViewById<View?>(R.id.item_horizontal_videoprogress_default_loading_bar) as ProgressBar?
            mThumbView.setVisibility(View.GONE)
            v.setMinimumWidth(mProgressThumbWidth)
            v.setMinimumHeight(mProgresThumbHeight)
        }
    }
    
    internal inner class ThumbnailBitmapWorkTask constructor(uri: Uri?, id: Int, imageView: ImageView?) : AsyncTask<Int?, Void?, Bitmap?>() {
        private val imageViewWeakReference: WeakReference<ImageView?>?
        private val mVideoUri: Uri?
        private val mId: Int
        fun getId(): Int {
            return mId
        }
        
        override fun doInBackground(vararg params: Int?): Bitmap? {
            val progress: Int = params.get(0)
            val bitmap: Bitmap?
            if (mDiskLruCache != null && mDiskLruCache.getBitmap(progress) == null) {
                bitmap = createBitmap(mVideoUri, progress.toLong())
                if (bitmap != null) {
                    mDiskLruCache.put(progress, bitmap)
                }
            } else {
                bitmap = mDiskLruCache.getBitmap(progress)
            }
            return bitmap
        }
        
        override fun onPostExecute(bitmap: Bitmap?) {
            Log.v(TAG, "AsyncTask, id=" + mId + ", bitmap=" + ((bitmap != null).toString() + ", reference=" + (imageViewWeakReference != null)))
            if (imageViewWeakReference != null && bitmap != null) {
                val imageView: ImageView? = imageViewWeakReference.get()
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap)
                    imageView.setVisibility(View.VISIBLE)
                    mHorizontalViewHolderReferenceHashMap.get(imageView).mThumbLoadingBar.setVisibility(View.GONE)
                }
            }
        }
        
        init {
            Log.v(TAG, "ThumbnailBitmapTask, id=" + id)
            imageViewWeakReference = WeakReference(imageView)
            mVideoUri = uri
            mId = id
        }
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/HorizontalGridViewAdapter"
        private val THUMB_COUNT: Int = 30
    }
    
    init {
        val division: Int = mVideoDuration / (THUMB_COUNT + 1)
        val position: Float = mVideoProgress as Float / mVideoDuration
        val countBefore: Int = (mVideoProgress / division) as Int
        for (i in 0 until countBefore) {
            mThumbPosition.get(i) = (i + 1) * division
        }
        mThumbPosition.get(countBefore) = mVideoProgress
        for (i in countBefore + 1 until THUMB_COUNT + 1) {
            mThumbPosition.get(i) = (i + 1) * division
        }
        mDiskLruCache = DiskLruImageCache(mContext, SettingUtil.Companion.DISK_CACHE_DIR, SettingUtil.Companion.DISK_CACHE_SIZE)
    }
}