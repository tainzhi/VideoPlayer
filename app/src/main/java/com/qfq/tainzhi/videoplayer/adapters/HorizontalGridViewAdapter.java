package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.utils.DiskLruImageCache;
import com.qfq.tainzhi.videoplayer.utils.Util;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalGridViewAdapter extends RecyclerView.Adapter<HorizontalGridViewAdapter.HorizontalViewHolder> {
    
    private final static String TAG = "VideoPlayer/HorizontalGridViewAdapter";
    private final static int THUMB_COUNT = 30;
    // FIXME: 2016/3/9
    // The cache must be cleared before activity finished
    private DiskLruImageCache mDiskLruCache;
    private Context mContext;
    private HorizontalGridViewAdapter mAdapter = this;
    private WeakHashMap<ImageView, ThumbnailBitmapWorkTask> mThumbTaskRefereceHashMap = new WeakHashMap<ImageView, ThumbnailBitmapWorkTask>();
    private WeakHashMap<ImageView, HorizontalViewHolder> mHorizontalViewHolderReferenceHashMap = new WeakHashMap<ImageView, HorizontalViewHolder>();
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Uri mVideoUri;
    private int mVideoDuration;
    private int mVideoProgress;
    private int mProgressThumbWidth;
    private int mProgresThumbHeight;
    private int[] mThumbPosition = new int[THUMB_COUNT + 1];
    
    public HorizontalGridViewAdapter(Context context, Uri uri, int duration, int progress,
                                     int progressThumbWidth,
                                     int progressThumbHeight) {
        mContext = context;
        mVideoUri = uri;
        mVideoDuration = duration;
        mVideoProgress = progress;
        mProgressThumbWidth = progressThumbWidth;
        mProgresThumbHeight = progressThumbHeight;
        
        int division = duration / (THUMB_COUNT + 1);
        float position = (float) progress / duration;
        int countBefore = (int) (progress / division);
        for (int i = 0; i < countBefore; i++) {
            mThumbPosition[i] = (i + 1) * division;
        }
        mThumbPosition[countBefore] = progress;
        for (int i = countBefore + 1; i < THUMB_COUNT + 1; i++) {
            mThumbPosition[i] = (i + 1) * division;
        }
        
        mDiskLruCache = new DiskLruImageCache(mContext, Util.DISK_CACHE_DIR, Util.DISK_CACHE_SIZE);
    }
    
    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup container, int valueType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_horizontal_videoprogress_window, container, false);
        return new HorizontalViewHolder(root);
    }
    
    @Override
    public void onBindViewHolder(final HorizontalViewHolder viewHolder, final int position) {
        RecyclerView.ViewHolder holder = viewHolder;
        ImageView imageView = viewHolder.mThumbView;
        viewHolder.mThumbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemHolderClick(position, mThumbPosition[position]);
            }
        });
        loadThumbnailBitmap(viewHolder, mVideoUri, position, mThumbPosition[position], imageView);
    }
    
    @Override
    public int getItemCount() {
        return THUMB_COUNT + 1;
    }
    
    private void onItemHolderClick(int position, int videoProgress) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, null,
                    position, videoProgress);
        }
    }
    
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    
    private void loadThumbnailBitmap(HorizontalViewHolder viewHolder, Uri uri, int index, int progress, ImageView thumbnailView) {
        Log.v(TAG, "position=" + index + ", progress=" + progress);
        if (!mThumbTaskRefereceHashMap.containsKey(thumbnailView)) {
            Log.v(TAG, "loadThumbnailBitmpa enter");
            final ThumbnailBitmapWorkTask task = new ThumbnailBitmapWorkTask(uri, index, thumbnailView);
            mThumbTaskRefereceHashMap.put(thumbnailView, task);
            mHorizontalViewHolderReferenceHashMap.put(thumbnailView, viewHolder);
            task.execute(progress);
        }
    }
    
    private Bitmap createBitmap(Uri videoUri, long videoProgress) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap srcBitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mContext, videoUri);
            srcBitmap = mediaMetadataRetriever.getFrameAtTime(videoProgress * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            Log.e(TAG, "counldn't get frame at " + videoProgress);
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        if (null == srcBitmap) {
            return null;
        } else {
            return scaleBitmap(srcBitmap, mProgressThumbWidth, mProgresThumbHeight);
        }
    }
    
    private Bitmap scaleBitmap(Bitmap originalBitmap, int toWidth, int toHeight) {
        float scaleWidth = ((float) toWidth) / originalBitmap.getWidth();
        float scaleHeight = ((float) toHeight) / originalBitmap.getHeight();

//        float scale = 0;
//        if (scaleWidth < scaleHeight) {
//            scale = scaleWidth;
//        } else {
//            scale = scaleHeight;
//        }
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight(), matrix, true);
    }
    
    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbView;
        public ProgressBar mThumbLoadingBar;
        
        public HorizontalViewHolder(View v) {
            super(v);
            mThumbView = (ImageView) v.findViewById(R.id.item_horizontal_videoprogress_id);
            mThumbLoadingBar = (ProgressBar) v.findViewById(R.id.item_horizontal_videoprogress_default_loading_bar);
            mThumbView.setVisibility(View.GONE);
            v.setMinimumWidth(mProgressThumbWidth);
            v.setMinimumHeight(mProgresThumbHeight);
        }
        
    }
    
    class ThumbnailBitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {
        
        private final WeakReference<ImageView> imageViewWeakReference;
        private Uri mVideoUri;
        private int mId;
        
        public ThumbnailBitmapWorkTask(Uri uri, int id, ImageView imageView) {
            Log.v(TAG, "ThumbnailBitmapTask, id=" + id);
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            mVideoUri = uri;
            mId = id;
        }
        
        public int getId() {
            return mId;
        }
        
        @Override
        protected Bitmap doInBackground(Integer... params) {
            int progress = params[0];
            Bitmap bitmap;
            if (mDiskLruCache != null && mDiskLruCache.getBitmap(progress) == null) {
                bitmap = createBitmap(mVideoUri, progress);
                if (bitmap != null) {
                    mDiskLruCache.put(progress, bitmap);
                }
            } else {
                bitmap = mDiskLruCache.getBitmap(progress);
            }
            return bitmap;
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.v(TAG, "AsyncTask, id=" + mId + ", bitmap=" + ((bitmap != null) + ", reference=" + (imageViewWeakReference != null)));
            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    
                    mHorizontalViewHolderReferenceHashMap.get(imageView).mThumbLoadingBar.setVisibility(View.GONE);
                }
            }
        }
    }
    
}
