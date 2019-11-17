package com.qfq.tainzhi.videoplayer.my_media;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.qfq.tainzhi.videoplayer.R;

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 11:41
 * @description:
 **/

public class MediaExo extends MediaInterface implements
		Player.EventListener,
				VideoListener {
	
	public SimpleExoPlayer simpleExoPlayer;
	private Runnable callback;
	private long previousSeek = 0;
	
	private String TAG = this.getClass().getSimpleName();
	
	public MediaExo(BaseVideoView baseVideoView) { super(baseVideoView); }
	
	@Override
	public void start() {
		simpleExoPlayer.setPlayWhenReady(true);
	}
	
	@Override
	public void prepare() {
		Log.d(TAG, "prepare");
		Context context = mBaseVideoView.getContext();
		release();
		mMediaHandlerThread = new HandlerThread("VideoPlayer");
		mMediaHandlerThread.start();
		mMediaHandler = new Handler(mMediaHandlerThread.getLooper());
		mHandler = new Handler();
		mMediaHandler.post(() -> {
			BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
			TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
			TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
			LoadControl loadControl = new DefaultLoadControl(new DefaultAllocator(true,
					C.DEFAULT_BUFFER_SEGMENT_SIZE),
					360000, 600000, 1000, 5000,
					C.LENGTH_UNSET,
					false);
			
			// 2. Create the player
			
			RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
			simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl);
			// Produces DataSource instances through which media data is loaded.
			DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
					Util.getUserAgent(context, context.getResources().getString(R.string.app_name)));
			
			// String currUrl = jzvd.jzDataSource.getCurrentUrl().toString();
			MediaSource videoSource;
			// if (currUrl.contains(".m3u8")) {
			// 	videoSource = new HlsMediaSource.Factory(dataSourceFactory)
			// 			              .createMediaSource(Uri.parse(currUrl), handler, null);
			// } else {
			// 	videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
			// 			              .createMediaSource(Uri.parse(currUrl));
			// }
			videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
					              .createMediaSource(mBaseVideoView.videoUri);
			
			simpleExoPlayer.addVideoListener(this);
			// Boolean isLoop = mBaseVideoView.looping;
			// if (isLoop) {
			// 	simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
			// } else {
			// 	simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_OFF);
			// }
			simpleExoPlayer.prepare(videoSource);
			simpleExoPlayer.setPlayWhenReady(true);
			callback = new OnBufferUpdate();
			mBaseVideoView.mSurfaceHodler.bindToMediaPlayer(simpleExoPlayer);
		});
	}
	
	@Override
	public void pause() {
		simpleExoPlayer.setPlayWhenReady(false);
	}
	
	@Override
	public boolean isPlaying() {
		return simpleExoPlayer.getPlayWhenReady();
	}
	
	@Override
	public void seekTo(long time) {
		if (time != previousSeek) {
			simpleExoPlayer.seekTo(time);
			previousSeek = time;
			// mBaseVideoView.seekInAdvance = time;
		}
	}
	
	@Override
	public void release() {
		if (mMediaHandler != null && mMediaHandlerThread != null && simpleExoPlayer != null) {
			HandlerThread tmpHandlerThread = mMediaHandlerThread;
			SimpleExoPlayer tmpMediaPlayer = simpleExoPlayer;
			// BaseVideoView.SAVED_SURFACE = null;
			
			mMediaHandler.post(() -> {
				tmpMediaPlayer.release();
				tmpHandlerThread.quit();
			});
			simpleExoPlayer = null;
		}
	}
	
	@Override
	public long getCurrentPosition() {
		if (simpleExoPlayer != null) {
			return simpleExoPlayer.getCurrentPosition();
		} else {
			return 0;
		}
	}
	
	@Override
	public long getDuration() {
		if (simpleExoPlayer != null) {
			return simpleExoPlayer.getDuration();
		} else {
			return 0;
		}
	}
	
	@Override
	public void setVolume(float leftVoluem, float rightVolume) {
		simpleExoPlayer.setVolume(leftVoluem);
		simpleExoPlayer.setVolume(rightVolume);
	}
	
	@Override
	public void setSpeed(float speed) {
		PlaybackParameters parameters = new PlaybackParameters(speed, 1.0F);
		simpleExoPlayer.setPlaybackParameters(parameters);
	}
	
	@Override
	public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
		// handler.post(() -> jzvd.onVideoSizeChanged(width, height));
	}
	
	@Override
	public void onRenderedFirstFrame() {
		Log.d(TAG, "onRenderedFirstFrame");
	}
	
	@Override
	public void onTimelineChanged(final Timeline timeline, Object manifest, final int reason) {
		Log.d(TAG, "onTimelineChanged");
	}
	
	@Override
	public void onTracksChanged(TrackGroupArray trackGroupArray,
	                            TrackSelectionArray selectionArray) {}
	
	;
	
	@Override
	public void onLoadingChanged(boolean isLoading) {
		Log.d(TAG, "onLoadingChanged");
	}
	
	@Override
	public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
		Log.d(TAG, "onPlayStateChanged");
		mHandler.post(() -> {
			switch (playbackState) {
				case Player.STATE_IDLE: {}
				break;
				case Player.STATE_BUFFERING:
					mHandler.post(callback);
					break;
				case Player.STATE_READY:
					if (playWhenReady) {
						// mBaseVideoView.onStatePlaying();
					} else {
					
					}
					break;
				case Player.STATE_ENDED:
					mBaseVideoView.onAutoCompletion();
					;
					break;
				
			}
		});
	}
	
	@Override
	public void onRepeatModeChanged(int repeatMode) {
	
	}
	
	@Override
	public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
	
	}
	
	@Override
	public void onPlayerError(ExoPlaybackException error) {
		Log.e(TAG, "onPlayError" + error.toString());
		mHandler.post(() -> mBaseVideoView.onError(1000, 1000 ));
	}
	
	@Override
	public void onPositionDiscontinuity(int reason) {
	
	}
	
	@Override
	public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
	
	}
	
	@Override
	public void onSeekProcessed() {
		mHandler.post(() -> mBaseVideoView.onSeekComplete());
	}
	
	// @Override
	// public void setSurface(Surface surface) {
	// 	simpleExoPlayer.setVideoSurface(surface);
	// }
	
	
	private class OnBufferUpdate implements Runnable {
		
		@Override
		public void run() {
			if (simpleExoPlayer != null) {
				final int percent = simpleExoPlayer.getBufferedPercentage();
				mHandler.post(() -> mBaseVideoView.setBufferProgress(percent));
				if (percent < 100) {
					mHandler.postDelayed(callback, 300);
				} else {
					mHandler.removeCallbacks(callback);
				}
			}
			
		}
	}
}
