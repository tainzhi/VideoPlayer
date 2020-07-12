package com.tanzhi.qmediaplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:10
 * @description: 封装android 系统自带播放器
 */
class IMediaSystem(videoView: VideoView) : IMediaInterface(videoView),
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnVideoSizeChangedListener {
    companion object {
        private const val TAG = "IMediaSystem"
    }

    private var mMediaPlayer: MediaPlayer? = null
    override fun start() {
        logI(TAG, "start()")
        mMediaHandler?.post { mMediaPlayer?.start() }
    }

    override fun prepare() {
        logI(TAG, "prepare()")
        release()
        mMediaHandlerThread = HandlerThread("QVideoPlayer")
        mMediaHandlerThread?.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            try {
                mMediaPlayer = MediaPlayer()
                mMediaPlayer!!.isLooping = mVideoView.loop
                mMediaPlayer!!.setScreenOnWhilePlaying(true)
                mMediaPlayer!!.setOnPreparedListener(this@IMediaSystem)
                mMediaPlayer!!.setOnCompletionListener(this@IMediaSystem)
                mMediaPlayer!!.setOnBufferingUpdateListener(this@IMediaSystem)
                mMediaPlayer!!.setOnSeekCompleteListener(this@IMediaSystem)
                mMediaPlayer!!.setOnErrorListener(this@IMediaSystem)
                mMediaPlayer!!.setOnInfoListener(this@IMediaSystem)
                mMediaPlayer!!.setOnVideoSizeChangedListener(this@IMediaSystem)
                val clazz = MediaPlayer::class.java
                val method = clazz.getDeclaredMethod("setDataSource", Context::class.java, Uri::class.java)
                method.invoke(mMediaPlayer, mVideoView.context, mVideoView.videoUri)
                mVideoView.mSurfaceHolder!!.bindToMediaPlayer(this)
                mMediaPlayer!!.prepareAsync()
            } catch (e: Exception) {
                mVideoView.onError(-1, -1)
                e.printStackTrace()
            }
        }
    }

    override fun resetDataSource(uri: Uri) {
        mMediaPlayer?.setDataSource(mVideoView.context, uri)
    }

    override fun setDisplay(surfaceHolder: SurfaceHolder) {
        mSurfaceHolder = surfaceHolder
        mMediaPlayer?.setDisplay(surfaceHolder)
    }

    override fun setDisplay(surface: Surface) {
        mSurface = surface
        mMediaPlayer?.setSurface(surface)
    }

    override fun pause() {
        logI(TAG, "pause()")
        mMediaHandler?.post { mMediaPlayer?.pause() }
    }

    override val isPlaying
            get() = mMediaPlayer?.isPlaying ?: false

    override fun seekTo(time: Long) {
        mMediaHandler!!.post {
            try {
                mMediaPlayer!!.seekTo(time.toInt())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun release() {
        if (mMediaPlayer == null) {
            logI(TAG, "MediaPlayer not initialized")
            return
        }
        logI(TAG, "release()")
        if (mMediaHandler != null && mMediaHandlerThread != null) {
            val tmpHandlerThread = mMediaHandlerThread!!
            val tmpMediaPlayer = mMediaPlayer!!
            mMediaHandler!!.post {
                tmpMediaPlayer.setSurface(null)
                tmpMediaPlayer.reset()
                tmpMediaPlayer.release()
                tmpHandlerThread.quit()
            }
            mMediaPlayer = null
        }
    }

    override val currentPosition
        get() = mMediaPlayer?.currentPosition?.toLong() ?: 0

    override val duration
        get() = mMediaPlayer?.duration?.toLong() ?: 0

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
        if (mMediaHandler == null) return
        mMediaHandler!!.post {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.setVolume(leftVolume, rightVolume)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun setSpeed(speed: Float) {
        val pp = mMediaPlayer!!.playbackParams
        pp.speed = speed
        mMediaPlayer!!.playbackParams = pp
    }

    override fun onBufferingUpdate(mediaPlayer: MediaPlayer, percent: Int) {
        logI(TAG, "onBufferingUpdate(), percent=$percent")
        mHandler!!.post { mVideoView.setBufferProgress(percent) }
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        logI(TAG, "onCompletion()")
        mHandler!!.post { mVideoView.onAutoCompletion() }
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        logI(TAG, "onError(), what:$what, extra:$extra")
        mHandler!!.post { mVideoView.onError(what, extra) }
        return true
    }

    override fun onInfo(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        logI(TAG, "onInfo(), what:$what, extra:$extra")
        mHandler!!.post { mVideoView.onInfo(what, extra) }
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        logI(TAG, "onPrepared()")
        mHandler!!.post { mVideoView.onPrepared() }
    }

    override fun onSeekComplete(mediaPlayer: MediaPlayer) {
        logD()
        mHandler!!.post { mVideoView.onSeekCompleted() }
    }

    override fun onVideoSizeChanged(mediaPlayer: MediaPlayer, width: Int, height: Int) {
        logI(TAG, "onVideoSizeChanged(), width=$width, height=$height")
        mHandler!!.post { mVideoView.onVideoSizeChanged(width, height) }
    }
}