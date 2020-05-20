package com.qfq.tainzhi.videoplayer.my_media

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.qfq.tainzhi.videoplayer.R

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 11:41
 * @description:
 */
class IMediaExo(videoView: VideoView) : IMediaInterface(videoView), Player.EventListener, VideoListener {
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var callback: Runnable? = null
    private var previousSeek: Long = 0
    private val tag = this.javaClass.simpleName
    override fun start() {
        simpleExoPlayer.playWhenReady = true
    }

    override fun setDisplay(surfaceHolder: SurfaceHolder) {
        simpleExoPlayer.setVideoSurfaceHolder(surfaceHolder)
    }

    override fun setDisplay(surface: Surface) {
        simpleExoPlayer.setVideoSurface(surface)
    }

    override fun prepare() {
        Log.d(tag, "prepare")
        val context = mVideoView.context
        release()
        mMediaHandlerThread = HandlerThread("VideoPlayer")
        mMediaHandlerThread!!.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val loadControl: LoadControl = DefaultLoadControl(DefaultAllocator(true,
                    C.DEFAULT_BUFFER_SEGMENT_SIZE),
                    360000, 600000, 1000, 5000,
                    C.LENGTH_UNSET,
                    false)
            // 2. Create the player
            val renderersFactory: RenderersFactory = DefaultRenderersFactory(context)
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, renderersFactory, trackSelector, loadControl)
            // Produces DataSource instances through which media data is loaded.
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, context.resources.getString(R.string.app_name)))
            // String currUrl = jzvd.jzDataSource.getCurrentUrl().toString();
            val videoSource: MediaSource
            // if (currUrl.contains(".m3u8")) {
            // 	videoSource = new HlsMediaSource.Factory(dataSourceFactory)
            // 			              .createMediaSource(Uri.parse(currUrl), handler, null);
            // } else {
            // 	videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
            // 			              .createMediaSource(Uri.parse(currUrl));
            // }
            videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mVideoView.mVideoUri)
            simpleExoPlayer.addVideoListener(this)
            // TODO: 2020/5/19  
            // val isLoop = mVideoView.loop
            // if (isLoop) {
            //     simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            // } else {
            //     simpleExoPlayer.repeatMode = Player.REPEAT_MODE_OFF
            // }
            simpleExoPlayer.prepare(videoSource)
            simpleExoPlayer.playWhenReady = true
            callback = OnBufferUpdate()
            mVideoView.mSurfaceHolder!!.bindToMediaPlayer(this)
        }
    }

    override fun pause() {
        simpleExoPlayer.playWhenReady = false
    }

    override val isPlaying: Boolean
        get() = simpleExoPlayer.playWhenReady

    override fun seekTo(time: Long) {
        if (time != previousSeek) {
            simpleExoPlayer.seekTo(time)
            previousSeek = time
            // mVideoView.seekInAdvance = time;
        }
    }

    override fun release() {
        if (mMediaHandler != null && mMediaHandlerThread != null && simpleExoPlayer != null) {
            val tmpHandlerThread = mMediaHandlerThread!!
            val tmpMediaPlayer: SimpleExoPlayer? = simpleExoPlayer
            // VideoView.SAVED_SURFACE = null;
            mMediaHandler!!.post {
                tmpMediaPlayer?.release()
                tmpHandlerThread.quit()
            }
            simpleExoPlayer.release()
        }
    }

    override val currentPosition: Long
        get() = if (simpleExoPlayer != null) {
            simpleExoPlayer.currentPosition
        } else {
            0
        }

    override val duration: Long
        get() = if (simpleExoPlayer != null) {
            simpleExoPlayer.duration
        } else {
            0
        }

    override fun setVolume(leftVoluem: Float, rightVolume: Float) {
        simpleExoPlayer.volume = leftVoluem
        simpleExoPlayer.volume = rightVolume
    }

    override fun setSpeed(speed: Float) {
        val parameters = PlaybackParameters(speed, 1.0f)
        simpleExoPlayer.playbackParameters = parameters
    }

    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) { // handler.post(() -> jzvd.onVideoSizeChanged(width, height));
    }

    override fun onRenderedFirstFrame() {
        Log.d(tag, "onRenderedFirstFrame")
    }

    override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {
        Log.d(tag, "onTimelineChanged")
    }

    override fun onTracksChanged(trackGroupArray: TrackGroupArray,
                                 selectionArray: TrackSelectionArray) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        Log.d(tag, "onLoadingChanged")
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        Log.d(tag, "onPlayStateChanged")
        mHandler!!.post {
            when (playbackState) {
                Player.STATE_IDLE -> {
                }
                Player.STATE_BUFFERING -> mHandler!!.post(callback)
                Player.STATE_READY -> if (playWhenReady) { // mVideoView.onStatePlaying();
                } else {
                }
                Player.STATE_ENDED -> {
                    mVideoView.onAutoCompletion()
                }
            }
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {}
    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
    override fun onPlayerError(error: ExoPlaybackException) {
        Log.e(tag, "onPlayError$error")
        mHandler!!.post { mVideoView.onError(1000, 1000) }
    }

    override fun onPositionDiscontinuity(reason: Int) {}
    override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {}
    override fun onSeekProcessed() {
        mHandler!!.post { mVideoView.onSeekCompleted() }
    }

    private inner class OnBufferUpdate : Runnable {
        override fun run() {
            if (simpleExoPlayer != null) {
                val percent = simpleExoPlayer.bufferedPercentage
                mHandler!!.post { mVideoView.setBufferProgress(percent) }
                if (percent < 100) {
                    mHandler!!.postDelayed(callback, 300)
                } else {
                    mHandler!!.removeCallbacks(callback)
                }
            }
        }
    }
}