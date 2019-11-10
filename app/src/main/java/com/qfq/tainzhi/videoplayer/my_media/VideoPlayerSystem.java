package com.qfq.tainzhi.videoplayer.my_media;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.lang.reflect.Method;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:10
 * @description: android 系统自带播放器
 **/

public class VideoPlayerSystem extends VideoPlayerBase implements
		MediaPlayer.OnPreparedListener,
				MediaPlayer.OnCompletionListener,
				MediaPlayer.OnBufferingUpdateListener,
				MediaPlayer.OnSeekCompleteListener,
				MediaPlayer.OnErrorListener,
				MediaPlayer.OnInfoListener,
				MediaPlayer.OnVideoSizeChangedListener {
	
	public MediaPlayer mMediaPlayer;
	
	public VideoPlayerSystem(BaseVideoView baseVideoView) {
		super(baseVideoView);
	}
	
	@Override
	public void start() {
		mMediaHandler.post(() -> { mMediaPlayer.start();});
	}
	
	@Override
	public void prepare() {
		
		release();
		mMediaHandlerThread = new HandlerThread("QVideoPlayer");
		mMediaHandlerThread.start();
		mMediaHandler = new Handler(mMediaHandlerThread.getLooper());
		mHandler = new Handler();
		
		mMediaHandler.post(() -> {
			try {
				mMediaPlayer = new MediaPlayer();
				// TODO: 2019-11-10 是否循环
				mMediaPlayer.setLooping(false);
				mMediaPlayer.setScreenOnWhilePlaying(true);
				mMediaPlayer.setOnPreparedListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnCompletionListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnBufferingUpdateListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnSeekCompleteListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnErrorListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnInfoListener(VideoPlayerSystem.this);
				mMediaPlayer.setOnVideoSizeChangedListener(VideoPlayerSystem.this);
				Class<MediaPlayer> clazz = MediaPlayer.class;
				Method method = clazz.getDeclaredMethod("setDataSource", String.class);
				//fixme
				method.invoke(mMediaPlayer, mBaseVideoView.mDataSource.getUrl());
				mMediaPlayer.prepareAsync();
				// fixme
				mMediaPlayer.setSurface(new Surface());
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
	
	@Override
	public void pause() {
	}
	
	@Override
	public boolean isPlaying() {
		return false;
	}
	
	@Override
	public void seekTo(long time) {
	
	}
	
	@Override
	public void release() {
	
	}
	
	@Override
	public long getCurrentPosition() {
		return 0;
	}
	
	@Override
	public long getDuration() {
		return 0;
	}
	
	@Override
	public void setVolume(float leftVoluem, float rightVolume) {
	
	}
	
	@Override
	public void setSpeed(float speed) {
	
	}
	
	@Override
	public void setRenderView(IRenderView renderView) {
	
	}
	
	
	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
	
	}
	
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
	
	}
	
	@Override
	public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
		return false;
	}
	
	@Override
	public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
		return false;
	}
	
	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
	
	}
	
	@Override
	public void onSeekComplete(MediaPlayer mediaPlayer) {
	
	}
	
	@Override
	public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
	
	}
}
