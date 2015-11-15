package com.qfq.muqing.myvideoplayer;

/**
 * Created by muqing on 14-12-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivityTemp extends Activity {
    static class VideoInfo{
        String filePath;
        String thumbPath;
        String title;
        String duration;
    }

    ArrayList<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

    public void onCreate(Bundle savedInstanceState)
    {
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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Log.i("end end ************ end", "end");
        ListView listView = (ListView)findViewById(R.id.list_view);
        VideoAdapter videoAdapter = new VideoAdapter(this, videoInfoList);
        listView.setAdapter(videoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = videoInfoList.get(position).title;
                Log.i("path of video file......", title);
                video(videoInfoList.get(position).filePath);
            }
        });
    }

    public void video(String videoPath)
    {
        Intent i=new Intent(this,PlayVideo.class);
        i.putExtra("videoPath",videoPath);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 定义一个Adapter来显示缩略图和视频title信息
     * @author Administrator
     *
     */
    static class VideoAdapter extends BaseAdapter {

        private Activity activity;
        private List<VideoInfo> videoItems;
        private static LayoutInflater inflater = null;

        public VideoAdapter(Activity activity, List<VideoInfo> data){
            this.activity = activity;
            this.videoItems = data;
            this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return videoItems.size();
        }
        @Override
        public Object getItem(int p) {
            // TODO Auto-generated method stub
            return videoItems.get(p);
        }
        @Override
        public long getItemId(int p) {
            // TODO Auto-generated method stub
            return p;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //ViewHolder holder = null;
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.list_raw,null);

            ImageView thumbImage = (ImageView)vi.findViewById(R.id.list_image);
            TextView titleText = (TextView)vi.findViewById(R.id.title);
            TextView filePathText = (TextView)vi.findViewById(R.id.filepath);
            TextView durationText = (TextView)vi.findViewById(R.id.duration);


            //显示信息
            titleText.setText(videoItems.get(position).title);
            filePathText.setText(videoItems.get(position).filePath);
            durationText.setText(videoItems.get(position).duration);
            Log.d("thumbpath:---------------", videoItems.get(position).thumbPath);
            if(videoItems.get(position).thumbPath != null){
                thumbImage.setImageURI(Uri.parse(videoItems.get(position).thumbPath));
            }
            return vi;
        }
    }
}

