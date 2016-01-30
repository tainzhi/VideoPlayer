package com.qfq.muqing.myvideoplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

    private int mProgressThumbWidth;
    private int mProgresThumbHeight;

    private final static int THUMB_COUNT = 20;

    private boolean hasCreateThumb = false;

    private VideoProgressThumb[] mVideoProgressThumbList = new VideoProgressThumb[THUMB_COUNT];

    public HorizontalGridViewAdapter(Context context, Uri uri, int duration, int progress) {
        mContext = context;
        mVideoUri = uri;
        mVideoDuration = duration;
        mVideoProgress = progress;

        new VideoProgressThumbWork().execute();
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
        mVideoProgressThumbList[position] = new VideoProgressThumb(position, viewHolder.mThumbView);

    }

    @Override
    public int getItemCount() {
        return THUMB_COUNT;
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

    public class HorizontalViewHolder extends RecyclerView.ViewHolder {
        public ImageView mThumbView;
        public HorizontalViewHolder (View v) {
            super(v);
            mThumbView = (ImageView) v.findViewById(R.id.item_horizontal_videoprogress_id);
        }

        public void setThumbViewSrc(Bitmap bitmap) {
            mThumbView.setImageBitmap(bitmap);
        }

    }

    class VideoProgressThumbWork extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "VideoProgressThumbWork.doInBackground()");
            // 20 thumbs, 1 is current, some prior current, the other after current
            // 20 thumbs, so the whole video is divided into (20 + 1) divions
            int thumbDivision = (int)( mVideoDuration  / 2);
            // TODO: 2016/1/24 hightlight current thumb
//            int indexDivisionSum = 0;
//            int priorCount = (int)(1.0 * mVideoProgress / mVideoDuration * THUMB_COUNT);
//            for (int i = 0; i < priorCount; i++) {
//                ProgressThumbList[i].progress = indexDivisionSum + thumbDivision;
//                indexDivisionSum = indexDivisionSum + thumbDivision;
//            }
            Bitmap srcBitmap = null;
            MediaMetadataRetriever retriever = null;
            try {
                Log.d(TAG, "qfqvideo uri=" + mVideoUri.toString());
                retriever = new MediaMetadataRetriever();
                retriever.setDataSource(mContext, mVideoUri);
                for (int i = 1; i < THUMB_COUNT + 1; i++) {
                    int videoProgress = 1 * thumbDivision;
                    if (videoProgress > mVideoDuration) {
                        videoProgress = mVideoDuration - 100;
                    }
                    Log.d(TAG, "VideoProgressThumbWork.doInBackground(), progress=" + videoProgress);
                    srcBitmap = retriever.getFrameAtTime(videoProgress * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                    if (srcBitmap == null) {
                        Log.e(TAG, "position=" + i + ", failed!");
                    }
                    retriever.release();
                    mVideoProgressThumbList[i - 1].setProgressThumb(scaleBitmap(srcBitmap, mProgressThumbWidth, mProgresThumbHeight));
                }

            } catch (Exception e) {
                Log.e(TAG, "getFrameAtTime failed!");
            } finally {
                if (retriever != null) {
                    retriever.release();
                }
            }
            return null;
        }

        private void PostExecute() {
            hasCreateThumb = true;
            for (int i = 0; i < THUMB_COUNT; i++) {
                mVideoProgressThumbList[i].update();
            }
        }
    }

    class VideoProgressThumb{
        private int mPosition;
        private int mProgress;
        private boolean mIsFocus;
        private Bitmap mProgressThumb;
        private WeakReference<ImageView> mImageViewWeakReference;

        public VideoProgressThumb(int position, ImageView imageView) {
            mPosition = position;
            mImageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        public WeakReference<ImageView> getImageViewWeakReference() {
            return mImageViewWeakReference;
        }

        public void setImageViewReference(WeakReference<ImageView> reference) {
            mImageViewWeakReference = reference;
        }

        public void setProgressThumb(Bitmap bitmap) {
            mProgressThumb = bitmap;
        }

        public Bitmap getProgressThumb() {
            return mProgressThumb;
        }

        public void setProgress(int progress) {
            mProgress = progress;
        }

        public void update() {
            ImageView imageView = mImageViewWeakReference.get();
            imageView.setImageBitmap(mProgressThumb);
        }
    }
}
