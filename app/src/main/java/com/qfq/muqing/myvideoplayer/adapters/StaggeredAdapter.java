package com.qfq.muqing.myvideoplayer.adapters;

/**
 * Created by Administrator on 2015/11/15.
 */

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.VerticalItemHolder> {

    private ArrayList<VideoItem> mItems;

    private AdapterView.OnItemClickListener mOnItemClickLister;

    public StaggeredAdapter() {
        mItems = new ArrayList<VideoItem>();
    }

    public void removeItem(int position) {
        if (position >= mItems.size())
            return;
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public static class VerticalItemHoler extends RecyclerView.ViewHolder implements View.OnClickListener {
        
    }

    public static class VideoItem {
        public String videoPath;
        public String videoId;
        public String videoName;
        public String videoDuration;

        public VideoItem(String videoPath, String videoId, String videoName, String videoDuration) {
            this.videoPath = videoPath;
            this.videoId = videoId;
            this.videoName =videoName;
            this.videoDuration = videoDuration;
        }
    }
    String[] thumbColumns = new String[]{
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID
    };

    String[] mediaColumns = new String[]{
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION,
    };


    Cursor cursor = this.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, null, null,null);
    int totalCount = cursor.getCount();
    Log.i("totalCount.........", "count");
    cursor.moveToFirst();
    for (int i=0; i<totalCount; i++)
    {
        VideoInfo info = new VideoInfo();
        info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        info.duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
        info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));

        //get the video id, and according it to get the thumb
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
        String selection = MediaStore.Video.Thumbnails.VIDEO_ID +"=?";
        String[] selectionArgs = new String[]{
                id+""
        };
        Cursor thumbCursor = this.managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection, selectionArgs, null);
        if(thumbCursor.moveToFirst()){
            info.thumbPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
        }

        //然后将其加入到videoList
        videoInfoList.add(info);
        Log.i("path of video file......", info.title);
        cursor.moveToNext();
    }
    cursor.close();
    <<<<<<< Original
}
