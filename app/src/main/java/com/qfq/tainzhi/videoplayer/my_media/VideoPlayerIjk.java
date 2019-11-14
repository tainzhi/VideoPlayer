package com.qfq.tainzhi.videoplayer.my_media;

import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;


import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 06:55
 * @description:
 **/

public class VideoPlayerIjk extends VideoPlayerBase implements
		IMediaPlayer.OnPreparedListener,
				IMediaPlayer.OnVideoSizeChangedListener,
				IMediaPlayer.OnCompletionListener,
				IMediaPlayer.OnErrorListener,
				IMediaPlayer.OnInfoListener,
				IMediaPlayer.OnBufferingUpdateListener,
				IMediaPlayer.OnSeekCompleteListener,
				IMediaPlayer.OnTimedTextListener {
	
	public IjkMediaPlayer mIjkMediaPlayer;
	
	public VideoPlayerIjk(BaseVideoView baseVideoView) {
		super(baseVideoView);
	}
	
	@Override
	public void start() {
		if (mIjkMediaPlayer != null) mIjkMediaPlayer.start();
	}
	
	@Override
	public void prepare() {
		release();
		mMediaHandlerThread = new HandlerThread("BaseVideoView");
		mMediaHandlerThread.start();
		mMediaHandler = new Handler(mMediaHandlerThread.getLooper());
		mHandler = new Handler();
		
		mMediaHandler.post(() -> {
			
			mIjkMediaPlayer = new IjkMediaPlayer();
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);
			mIjkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
			
			mIjkMediaPlayer.setOnPreparedListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnVideoSizeChangedListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnCompletionListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnErrorListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnInfoListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnBufferingUpdateListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnSeekCompleteListener(VideoPlayerIjk.this);
			mIjkMediaPlayer.setOnTimedTextListener(VideoPlayerIjk.this);
			
			// try {
			{
				// mIjkMediaPlayer.setDataSource(jzvd.jzDataSource.getCurrentUrl().toString());
				mIjkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mIjkMediaPlayer.setScreenOnWhilePlaying(true);
				mIjkMediaPlayer.prepareAsync();
				
				// mIjkMediaPlayer.setSurface(new Surface(jzvd.textureView.getSurfaceTexture()));
				// mIjkMediaPlayer.setDisplay(hodler);
				// } catch (IOException e) {
				// 	e.printStackTrace();
				}
		});
	}
	
	@Override
	public void pause() {
		mIjkMediaPlayer.pause();
	}
	
	@Override
	public boolean isPlaying() {
		return mIjkMediaPlayer.isPlaying();
	}
	
	@Override
	public void seekTo(long time) {
		mIjkMediaPlayer.seekTo(time);
	}
	
	@Override
	public void release() {
		if (mMediaHandler != null && mMediaHandlerThread != null && mIjkMediaPlayer != null) {
			HandlerThread tmpHandlThread = mMediaHandlerThread;
			IjkMediaPlayer tmpMediaPlayer = mIjkMediaPlayer;
			// JZMediaInterface.saved_surface = null;
			
			mMediaHandler.post(() -> {
				tmpMediaPlayer.setSurface(null);
				tmpMediaPlayer.release();
				tmpHandlThread.quit();
			});
			mIjkMediaPlayer = null;
		}
	}
	
	@Override
	public long getCurrentPosition() {
		return mIjkMediaPlayer.getCurrentPosition();
	}
	
	@Override
	public long getDuration() {
		return mIjkMediaPlayer.getDuration();
	}
	
	@Override
	public void setVolume(float leftVoluem, float rightVolume) {
		mIjkMediaPlayer.setVolume(leftVoluem, rightVolume);
	}
	
	@Override
	public void setSpeed(float speed) {
		mIjkMediaPlayer.setSpeed(speed);
	}
	
	@Override
	public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
		mHandler.post(() -> mBaseVideoView.setBufferProgress(percent));
	}
	
	@Override
	public void onCompletion(IMediaPlayer iMediaPlayer) {
	
	}
	
	@Override
	public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
		mHandler.post(() -> mBaseVideoView.onError(what, extra));
		return true;
	}
	
	@Override
	public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
		mHandler.post(() -> mBaseVideoView.onInfo(what, extra));
		return true;
	}
	
	@Override
	public void onPrepared(IMediaPlayer iMediaPlayer) {
		mHandler.post(() -> mBaseVideoView.onPrepared());
	}
	
	@Override
	public void onSeekComplete(IMediaPlayer iMediaPlayer) {
		mHandler.post(() -> mBaseVideoView.onSeekComplete());
	}
	
	@Override
	public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
	}
	
	@Override
	public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
		mHandler.post(() -> mBaseVideoView.onVideoSizeChanged(iMediaPlayer.getVideoWidth(),
				iMediaPlayer.getVideoHeight()));
	}
}
