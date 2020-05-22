package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean;
import com.qfq.tainzhi.videoplayer.util.StringUtil;

import java.io.File;
import java.util.List;


/**
 * Created by muqing on 2019/6/11.
 * Email: qfq61@qq.com
 */
public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.MyViewHolder> {
    
    private Context mContext;
    private List<LocalVideoBean> mLists;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnBottomReachedListener mOnBottomReachedListener;
    
    public LocalVideoAdapter(Context c, List<LocalVideoBean> l) {
        mContext = c;
        mLists = l;
        generateItems();
    }
    
    private void generateItems() {
        // DATA is path
        String[] videoColumns = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
				MediaStore.Video.Media.ORIENTATION,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                                        MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                                        MediaStore.Video.Media.DATE_TAKEN,
        };
        
        
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, null);
        int totalCount = cursor.getCount();
        Logger.d("local has %d videos", totalCount);
        cursor.moveToFirst();
        for (int i = 0; i < totalCount; i++) {
            int id =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            String path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            // 视频大小是以B为单位的整数数字, 故故需转成10KB, 10MB, 10GB
            long size =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            String size_s = StringUtil.formatMediaSize(size);
            
            String title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            // 视频大小是以秒(s)为单位的整数数字, 故需转成00:00:00
            long duration =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            String resolution =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
            String orientation =
					cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ORIENTATION));
            String date_added =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
            String date_modified =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
            String date_taken =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN));
            String create_date = getVideoCreatDate(path);
            String thumbnailsPath = getThumbnailPath(id);
            LocalVideoBean item = new LocalVideoBean(id, path, size_s, title,
                    duration, resolution, orientation, date_added,
                    date_modified, date_taken, create_date, thumbnailsPath);
            mLists.add(item);
            cursor.moveToNext();
        }
        cursor.close();
    }
    
    public String getThumbnailPath(int videoId) {
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};
        Cursor thumbCursor =
                mContext.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID +
                                              "=" + videoId, null, null);
        String path = null;
        if (thumbCursor.moveToFirst()) {
            path =
                    thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
        }
        return path;
    }
    
    public String getVideoCreatDate(String path) {
        return null;
        // Logger.d("%s", path);
        // MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        // retriever.setDataSource(path);
        // String createDate =
        //         retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        // // 解析出的时间格式为, 20180503T044105.000Z, 需转换
        // return StringUtil.formatDate(createDate);
    }
    
    
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
    
    public void setOnBottomReachedListener(OnBottomReachedListener mOnBottomReachedListener) {
        this.mOnBottomReachedListener = mOnBottomReachedListener;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        return new MyViewHolder(LayoutInflater.from(mContext)
                                        .inflate(R.layout.item_local_video, parent, false));
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalVideoBean video = mLists.get(position);
        if (video.getThumbnailPath() != null) {
            holder.thumb.setImageURI(Uri.fromFile(new File(video.getThumbnailPath())));
        } else {
            holder.thumb.setImageResource(R.drawable.ic_video_default_thumbnail);
        }
        holder.title.setText(video.getTitle());
        holder.size.setText(video.getSize());
        holder.duration.setText(StringUtil.formatMediaTime(video.getDuration()));
        holder.date_added.setText(video.getCreateDate());
        
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,
                            position);
                    return true;
                }
            });
        }
        
        if (position ==  mLists.size() - 1) {
            mOnBottomReachedListener.onBottomReached(position);
        }
    }
    
    @Override
    public int getItemCount() {
        return mLists.size();
    }
    
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    
    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }
    
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, size, duration, date_added;
        
        public MyViewHolder(View itemView) {
            super(itemView);
            thumb =
                    itemView.findViewById(R.id.item_staggredview_thumbnail);
            title = itemView.findViewById(R.id.item_video_title);
            size = itemView.findViewById(R.id.item_video_size);
            duration = itemView.findViewById(R.id.item_video_duration);
            date_added =
                    itemView.findViewById(R.id.item_video_date_added);
        }
    }
    
}
