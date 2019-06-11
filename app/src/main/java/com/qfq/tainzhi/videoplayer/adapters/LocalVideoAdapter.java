package com.qfq.tainzhi.videoplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.bean.LocalVideoBean;

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
    
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }
    
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
    
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title, size, duration, progress;
        
        public MyViewHolder(View itemView) {
            super(itemView);
            thumb =
                    itemView.findViewById(R.id.item_staggredview_thumbnail);
            title = itemView.findViewById(R.id.item_video_title);
            size = itemView.findViewById(R.id.item_video_size);
            duration = itemView.findViewById(R.id.item_video_duration);
            progress =
                    itemView.findViewById(R.id.item_video_progess);
        }
    }
    
    public LocalVideoAdapter(Context c, List<LocalVideoBean> l) {
        mContext = c;
        mLists = l;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        return new MyViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_staggredview, parent,false));
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalVideoBean video = mLists.get(position);
        holder.thumb.setImageResource(R.drawable.ic_launcher);
        holder.title.setText(video.getTitle());
        holder.size.setText(video.getSize());
        holder.duration.setText(video.getDuration());
        holder.progress.setText(video.getProcess());
        
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
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()  {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,
                            position);
                    return true;
                }
            });
        }
    }
    
    @Override
    public int getItemCount() {
        return mLists.size();
    }
    
}
