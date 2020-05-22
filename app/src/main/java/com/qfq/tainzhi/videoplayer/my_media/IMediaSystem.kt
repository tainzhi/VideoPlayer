package com.qfq.tainzhi.videoplayer.my_media

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
    private var mMediaPlayer: MediaPlayer? = null
    override fun start() {
        logD()
        mMediaHandler!!.post { mMediaPlayer!!.start() }
    }

    override fun prepare() {
        logD()
        release()
        mMediaHandlerThread = HandlerThread("QVideoPlayer")
        mMediaHandlerThread!!.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            try {
                mMediaPlayer = MediaPlayer()
                // TODO: 2020/5/19
                // mMediaPlayer!!.isLooping = mVideoView.loop
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
                e.printStackTrace()
            }
        }
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
        mMediaHandler!!.post { mMediaPlayer!!.pause() }
    }

    override val isPlaying = mMediaPlayer?.isPlaying ?: false

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
        if (mMediaHandler != null && mMediaHandlerThread != null && mMediaHandler != null) {
            val tmpHandlerThread = mMediaHandlerThread!!
            val tmpMediaPlayer = mMediaPlayer
            sIRenderView = null
            mMediaHandler!!.post {
                tmpMediaPlayer!!.setSurface(null)
                tmpMediaPlayer.release()
                tmpHandlerThread.quit()
            }
            mMediaPlayer = null
        }
    }

    override val currentPosition = mMediaPlayer?.currentPosition?.toLong() ?: 0

    override val duration = mMediaPlayer?.duration?.toLong() ?: 0

    override fun setVolume(leftVoluem: Float, rightVolume: Float) {
        if (mMediaHandler == null) return
        mMediaHandler!!.post {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.setVolume(leftVoluem, rightVolume)
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
        logD("percent=$percent")
        mHandler!!.post { mVideoView.setBufferProgress(percent) }
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        logD()
        mHandler!!.post { mVideoView.onAutoCompletion() }
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        logD("what:$what, extra:$extra")
        mHandler!!.post { mVideoView.onError(what, extra) }
        return true
    }

    override fun onInfo(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        logD("what:$what, extra:$extra")
        mHandler!!.post { mVideoView.onInfo(what, extra) }
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        logD()
        mHandler!!.post { mVideoView.onPrepared() }
    }

    override fun onSeekComplete(mediaPlayer: MediaPlayer) {
        logD()
        mHandler!!.post { mVideoView.onSeekCompleted() }
    }

    override fun onVideoSizeChanged(mediaPlayer: MediaPlayer, width: Int, height: Int) {
        logD("width=$width, height=$height")
        mHandler!!.post { mVideoView.onVideoSizeChanged(width, height) }
    }
}