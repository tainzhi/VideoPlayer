package com.qfq.muqing.myvideoplayer.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qfq.muqing.myvideoplayer.R;

/**
 * Created by Administrator on 2016/1/24.
 */
public class HorizontalGridViewAdapter extends RecyclerView.Adapter<HorizontalGridViewAdapter.HorizontalViewHolder> {

    private final static String TAG = "VideoPlayer/HorizontalGridViewAdapter";

    private Context mContext;

    private Uri mVideoUri;
    private int mVideoId;
    private int mVideoDuration;
    private int mVideoProgress;

    private int mProgressThumbWidth;
    private int mProgresThumbHeight;

    private final static int THUMB_COUNT = 20;

    VideoProgressThumb[]  ProgressThumbList = new VideoProgressThumb[THUMB_COUNT];

    public HorizontalGridViewAdapter(Context context, Uri uri, int id, int duration, int progress) {
        mContext = context;
        mVideoUri = uri;
        mVideoId = id;
        mVideoDuration = duration;
        mVideoProgress = progress;
    }

    @Override
    public HorizontalViewHolder onCreateViewHolder(ViewGroup container, int valueType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.item_horizontal_videoprogress_window, container, false);
        return HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HorizontalViewHolder viewHolder, int position) {
        RecyclerView.ViewHolder holder = viewHolder;
        viewHolder.setThumbViewSrc(ProgressThumbList[position].progressThumb);
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

    public static class HorizontalViewHolder extends RecyclerView.ViewHolder {
        private ImageView mThumbView;
        public HorizontalViewHolder (View v) {
            super(v);
            mThumbView = (ImageView)v;
        }

        public void setThumbViewSrc(Bitmap bitmap) {
            mThumbView.setImageBitmap(bitmap);
        }
    }

    class VideoProgressThumbWork extends AsyncTask<Uri uri, Void, Void> {
        private void doInBackground(Uri ...) {
            // 20 thumbs, 1 is current, some prior current, the other after current
            // 20 thumbs, so the whole video is divided into (20 + 1) divions
            int thumbDivision = (int)(1.0 * mVideoDuration / (THUMB_COUNT + 1));
            // TODO: 2016/1/24 hightlight current thumb
//            int indexDivisionSum = 0;
//            int priorCount = (int)(1.0 * mVideoProgress / mVideoDuration * THUMB_COUNT);
//            for (int i = 0; i < priorCount; i++) {
//                ProgressThumbList[i].progress = indexDivisionSum + thumbDivision;
//                indexDivisionSum = indexDivisionSum + thumbDivision;
//            }
            Bitmap srcBitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(mContext,
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mVideoId));
                for (int i = 0; i < THUMB_COUNT + 1; i++) {
                    ProgressThumbList[i].progress = i * thumbDivision;
                    int videoProgress = i * thumbDivision;
                    srcBitmap = retriever.getFrameAtTime(videoProgress*1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    ProgressThumbList[i].progressThumb = scaleBitmap(srcBitmap, mProgressThumbWidth, mProgresThumbHeight);
                }

            } catch (Exception e) {
                Log.e(TAG, "getFrameAtTime failed!");
            } finally {
                if (retriever != null) {
                    retriever.release();
                }
            }

        }

        private void PostExecute() {

        }
    }

    class VideoProgressThumb{
        int progress;
        boolean isFocus;
        Bitmap progressThumb;
        public VideoProgressThumb() {
            progress = 0;
            isFocus = false;
            progressThumb = null;
        }
    }
}
