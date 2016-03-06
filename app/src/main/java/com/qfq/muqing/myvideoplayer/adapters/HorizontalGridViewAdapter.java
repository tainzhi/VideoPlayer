package com.qfq.muqing.myvideoplayer.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qfq.muqing.myvideoplayer.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalGridViewAdapter extends RecyclerView.Adapter<HorizontalGridViewAdapter.HorizontalViewHolder> {

    private final static String TAG = "VideoPlayer/HorizontalGridViewAdapter";

    private Context mContext;

    private HorizontalGridViewAdapter mAdapter = this;

    private Uri mVideoUri;
    private int mVideoDuration;
    private int mVideoProgress;

    private int mProgressThumbWidth = 300;
    private int mProgresThumbHeight = 200;
    private Bitmap mDefaultThumbnailBitmap;

    private final static int THUMB_COUNT = 30;
    private int[] mThumbPosition = new int[THUMB_COUNT + 1];

    public HorizontalGridViewAdapter(Context context, Uri uri, int duration, int progress) {
        mContext = context;
        mVideoUri = uri;
        mVideoDuration = duration;
        mVideoProgress = progress;
        mDefaultThumbnailBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.horizontal_video_progress_thumb);

        int division = duration / (THUMB_COUNT + 1);
        float position = (float)progress / duration;
        int countBefore = (int)(progress / division);
        for (int i=0; i <countBefore; i++) {
            mThumbPosition[i] = (i+1) * division;
        }
        mThumbPosition[countBefore] = progress;
        for (int i = countBefore + 1; i<THUMB_COUNT+1; i++) {
            mThumbPosition[i] = (i+1) * division;
        }
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup container, int valueType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_horizontal_videoprogress_window, container, false);
        return new HorizontalViewHolder(root);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder viewHolder, int position) {
        RecyclerView.ViewHolder holder = viewHolder;
        ImageView imageView = viewHolder.mThumbView;
        imageView.setImageResource(R.drawable.horizontal_video_progress_thumb);
        loadThumbnailBitmap(mVideoUri, position, mThumbPosition[position], imageView);
    }

    @Override
    public int getItemCount() {
        return THUMB_COUNT + 1;
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbView;
        public HorizontalViewHolder (View v) {
            super(v);
            mThumbView = (ImageView) v.findViewById(R.id.item_horizontal_videoprogress_id);
        }
    }

    class ThumbnailBitmapWorkTask extends AsyncTask<Integer, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;
        private Uri mVideoUri;
        private int mId;

        public int getId() {
            return mId;
        }

        public ThumbnailBitmapWorkTask(Uri uri, int id, ImageView imageView) {
            Log.v(TAG, "ThumbnailBitmapTask, id=" + id);
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            mVideoUri = uri;
            mId = id;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            int progress = params[0];
            Bitmap bitmap = createBitmap(mVideoUri, progress);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.v(TAG, "AsyncTask, id=" + mId + ", bitmap=" + ((bitmap != null) + ", reference=" + (imageViewWeakReference !=null)));
            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void loadThumbnailBitmap(Uri uri, int index,  int progress, ImageView thumbnailView) {
        Log.v(TAG, "position=" + index + ", progress=" + progress);
        if (cancelPotionalWork(index, thumbnailView)) {
            Log.v(TAG, "loadThumbnailBitmpa enter");
            final ThumbnailBitmapWorkTask task  = new ThumbnailBitmapWorkTask(uri, index, thumbnailView);
            final AsyncDrawable asyncDrawable  = new AsyncDrawable(mContext.getResources(),
                    mDefaultThumbnailBitmap, task);
            thumbnailView.setImageDrawable(asyncDrawable);
            task.execute(progress);
        }
    }

    private static boolean cancelPotionalWork(int id, ImageView imageView) {
        Log.v(TAG, "cancelPositionalWork, id=" +id);
        final ThumbnailBitmapWorkTask thumbnailBitmapWorkTask = getThumbnailBitmapWorkTask(imageView);
        Log.v(TAG, "thumbnailBitmapWorkTask=" + (thumbnailBitmapWorkTask == null));
        if (thumbnailBitmapWorkTask != null) {
            if (id != thumbnailBitmapWorkTask.mId) {
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

    private Bitmap createBitmap(Uri videoUri, long videoProgress) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        Bitmap srcBitmap = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(mContext, videoUri);
            srcBitmap = mediaMetadataRetriever.getFrameAtTime(videoProgress*1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            Log.e(TAG, "counldn't get frame at " + videoProgress);
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return scaleBitmap(srcBitmap, mProgresThumbHeight,mProgresThumbHeight);
    }


    private Bitmap scaleBitmap(Bitmap originalBitmap, int toWidth, int toHeight) {
        float scaleWidth = ((float)toWidth) / originalBitmap.getWidth();
        float scaleHeight = ((float)toHeight) / originalBitmap.getHeight();

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

}
