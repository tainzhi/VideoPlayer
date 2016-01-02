package com.qfq.muqing.myvideoplayer.adapters;

/**
 * Created by Administrator on 2015/11/15.
 */

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qfq.muqing.myvideoplayer.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.VerticalItemHolder> {

    private ArrayList<VideoItem> mItems;

    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    private Context mContext;

    private int mThumbnailParentWidth;
    private Bitmap mDefaultThumbnailBitmap;


    public StaggeredAdapter(Context context, int thumbnailParentWidth) {
        mContext = context;
        mThumbnailParentWidth = thumbnailParentWidth;
        mItems = new ArrayList<VideoItem>();
        mDefaultThumbnailBitmap = scaleBitmap(
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.thumbnail_default),
                mThumbnailParentWidth,
                mThumbnailParentWidth);
        Log.v("qfq", "thumbnail width=" + mThumbnailParentWidth / 2);
        generateItems();
    }

    public void removeItem(int position) {
        if (position >= mItems.size())
            return;
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        View root = layoutInflater.inflate(R.layout.item_staggredview, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {

        final View thumbnailView = itemHolder.videoThumbnail;
        if (position % 4 == 0) {
            thumbnailView.setMinimumHeight(300);
        } else {
            thumbnailView.setMinimumHeight(100);
        }

        VideoItem item = mItems.get(position);
        Log.d("testQFQ", "position=" + position + " videoName=" + item.videoName);
        itemHolder.setVideoTitle(item.videoName);
        itemHolder.setVideoSize(item.videoSize + "");
        itemHolder.setVideoDuration(item.videoDuration);
        itemHolder.setVideoProgress(item.videoProgress);

//        Bitmap thumbBitmap = ThumbnailUtils.createVideoThumbnail(item.videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
//        if (thumbBitmap != null) {
//            Log.v("qfq", "mThumbnailParentWidth=" + mThumbnailParentWidth);
//            Log.v("qfq", "thumbnail is not null, width=" + thumbBitmap.getWidth() + ", heigth=" + thumbBitmap.getHeight());
//        }
//        itemHolder.setVideoThumbnail(scaleBitmap(thumbBitmap, mThumbnailParentWidth, mThumbnailParentWidth));
        loadThumbnailBitmap(item.videoId, item.videoPath, itemHolder.getVideoThumbnail());
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private LinearLayout videoTitleLayout;
        private LinearLayout videoSizeLayout;
        private LinearLayout videoDurationLayout;
        private LinearLayout videoProgressLayout;
        private ImageView videoThumbnail;

        private StaggeredAdapter mAdapter;

        public VerticalItemHolder(View v,
                                  StaggeredAdapter adapter) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            mAdapter = adapter;
            videoThumbnail = (ImageView)v.findViewById(R.id.item_staggredview_thumbnail);
            videoTitleLayout = (LinearLayout)v.findViewById(R.id.item_video_title);
            videoSizeLayout = (LinearLayout)v.findViewById(R.id.item_video_size);
            videoDurationLayout = (LinearLayout)v.findViewById(R.id.item_video_duration);
            videoProgressLayout = (LinearLayout)v.findViewById(R.id.item_video_progess);

        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        @Override
        public boolean onLongClick(View v) {
            mAdapter.onItemHolderLongClick(this);
            return false;
        }


        public void setVideoThumbnail(Drawable videoThumbnail) {
            this.videoThumbnail.setImageDrawable(videoThumbnail);
        }

        public ImageView getVideoThumbnail() {
            return this.videoThumbnail;
        }

        public void setVideoThumbnail(Bitmap thumbnailBitmap) {
            this.videoThumbnail.setImageBitmap(thumbnailBitmap);
        }

        public void setVideoTitle(String videoTitle) {
            TextView videoItemName = (TextView)this.videoTitleLayout.findViewById(R.id.video_item_name);
            TextView videoItemDeatail = (TextView)this.videoTitleLayout.findViewById(R.id.video_item_detail);
            videoItemName.setText(R.string.video_title_name);
            videoItemDeatail.setText(videoTitle);
        }

        public void setVideoSize(String videoSize) {
            TextView videoItemName = (TextView)this.videoSizeLayout.findViewById(R.id.video_item_name);
            TextView videoItemDeatail = (TextView)this.videoSizeLayout.findViewById(R.id.video_item_detail);
            videoItemName.setText(R.string.video_size_name);
            videoItemDeatail.setText(videoSize);
        }

        public void setVideoDuration(String videoDuration) {
            TextView videoItemName = (TextView)this.videoDurationLayout.findViewById(R.id.video_item_name);
            TextView videoItemDeatail = (TextView)this.videoDurationLayout.findViewById(R.id.video_item_detail);
            videoItemName.setText(R.string.video_duration_name);
            videoItemDeatail.setText(videoDuration);
        }

        public void setVideoProgress(String videoProgress) {
            TextView videoItemName = (TextView)this.videoProgressLayout.findViewById(R.id.video_item_name);
            TextView videoItemDeatail = (TextView)this.videoProgressLayout.findViewById(R.id.video_item_detail);
            videoItemName.setText(R.string.video_progress_name);
            videoItemDeatail.setText(videoProgress);
        }
    }

    public static class VideoItem {
        public int videoId;
        public String videoPath;
        public String videoName;
        public int videoSize;
        public String videoDuration;
        public String videoProgress;

        public VideoItem(int videoId, String videoPath, String videoName, int videoSize, String videoDuration) {
            this.videoPath = videoPath;
            this.videoId = videoId;
            this.videoName =videoName;
            this.videoDuration = videoDuration;
            this.videoSize = videoSize;
            this.videoProgress = null;
        }
    }


    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), mItems.get(itemHolder.getAdapterPosition()).videoId);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void onItemHolderLongClick(VerticalItemHolder itemHolder) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), mItems.get(itemHolder.getAdapterPosition()).videoId);
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    private void generateItems() {
        String[] videoColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
        };


        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null,null);
        int totalCount = cursor.getCount();
        Log.i("totalCount.........", "count");
        cursor.moveToFirst();
        for (int i=0; i<totalCount; i++)
        {
            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String videoDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String videoTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            int videoSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            VideoItem item = new VideoItem(videoId, videoPath, videoTitle, videoSize, videoDuration);
            mItems.add(item);
            cursor.moveToNext();
        }
        cursor.close();
    }

    private Bitmap scaleBitmap(Bitmap originalBitmap, int toWidth, int toHeight) {
        float scaleWidth = ((float)toWidth) / originalBitmap.getWidth();
        float scaleHeight = ((float)toHeight) / originalBitmap.getHeight();

        float scale = 0;
        if (scaleWidth < scaleHeight) {
            scale = scaleWidth;
        } else {
            scale = scaleHeight;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight(), matrix, true);
    }

    private void loadThumbnailBitmap(int videoId, String videoPath, ImageView thumbnailView) {
        if (cancelPotionalWork(videoId, thumbnailView)) {
            final ThumbnailBitmapWorkTask task  = new ThumbnailBitmapWorkTask(videoId, videoPath, thumbnailView);
            final AsyncDrawable asyncDrawable  = new AsyncDrawable(mContext.getResources(),
                    mDefaultThumbnailBitmap, task);
            thumbnailView.setImageDrawable(asyncDrawable);
            task.execute(videoId);
        }
    }

    private static boolean cancelPotionalWork(int videoId, ImageView imageView) {
        final ThumbnailBitmapWorkTask thumbnailBitmapWorkTask = getThumbnailBitmapWorkTask(imageView);

        if (thumbnailBitmapWorkTask != null) {
            final int thumnailVideoId = thumbnailBitmapWorkTask.videoId;
            if (videoId != thumnailVideoId) {
                thumbnailBitmapWorkTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static ThumbnailBitmapWorkTask getThumbnailBitmapWorkTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getThumbnailBitmapWorkTask();
            }
        }
        return null;
    }

    class ThumbnailBitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private int videoId;
        private String videoPath;

        public ThumbnailBitmapWorkTask(int videoId, String videoPath, ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            this.videoId = videoId;
            this.videoPath = videoPath;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int id = params[0].intValue();
            Bitmap thumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MICRO_KIND);
            if (thumbBitmap != null) {
                Log.v("qfq", "mThumbnailParentWidth=" + mThumbnailParentWidth);
                Log.v("qfq", "thumbnail is not null, width=" + thumbBitmap.getWidth() + ", heigth=" + thumbBitmap.getHeight());
            }
            return scaleBitmap(thumbBitmap, mThumbnailParentWidth, mThumbnailParentWidth);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewWeakReference != null && bitmap != null) {
                final  ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<ThumbnailBitmapWorkTask> thumbnailBitmapWorkTaskWeakReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             ThumbnailBitmapWorkTask thumbnailBitmapWorkTask) {
            super(res, bitmap);
            thumbnailBitmapWorkTaskWeakReference = new WeakReference<ThumbnailBitmapWorkTask>(thumbnailBitmapWorkTask);
        }

        public ThumbnailBitmapWorkTask getThumbnailBitmapWorkTask() {
            return thumbnailBitmapWorkTaskWeakReference.get();
        }
    }
}
