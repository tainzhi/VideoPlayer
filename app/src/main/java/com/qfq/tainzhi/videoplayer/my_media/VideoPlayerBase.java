package com.qfq.tainzhi.videoplayer.my_media;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.TextureView;

import com.qfq.tainzhi.videoplayer.media.BaseVideoView;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 18:50
 * @description: 所有 player 的基类
 **/

public abstract class VideoPlayerBase {
	
	public static IRenderView sIRenderView;
	protected HandlerThread mMediaHandlerThread;
	protected Handler mMediaHandler;
	protected Handler mHandler;
	public BaseVideoView mBaseVideoView;
	public VideoPlayerBase(BaseVideoView baseVideoView) {
		mBaseVideoView = baseVideoView;
	}
	
	public abstract void start();
	
	public abstract void prepare();
	
	public abstract void pause();
	
	public abstract boolean isPlaying();
	
	public abstract void seekTo(long time);
	
	public abstract void release();
	
	public abstract long getCurrentPosition();
	
	public abstract long getDuration();
	
	public abstract void setVolume(float leftVoluem, float rightVolume);
	
	public abstract void setSpeed(float speed);
}
