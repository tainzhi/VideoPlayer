package com.qfq.tainzhi.videoplayer.my_media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.RequiresApi
import com.orhanobut.logger.Logger
import com.qfq.tainzhi.videoplayer.my_media.MediaSystem

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 19:10
 * @description: android 系统自带播放器
 */
class MediaSystem(baseVideoView: BaseVideoView?) : MediaInterface(baseVideoView), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    var mMediaPlayer: MediaPlayer? = null
    override fun start() {
        Logger.d("")
        mMediaHandler!!.post { mMediaPlayer!!.start() }
    }

    override fun prepare() {
        Logger.d("create mediaplayer")
        release()
        mMediaHandlerThread = HandlerThread("QVideoPlayer")
        mMediaHandlerThread!!.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            try {
                mMediaPlayer = MediaPlayer()
                // TODO: 2019-11-10 是否循环
                mMediaPlayer!!.isLooping = false
                mMediaPlayer!!.setScreenOnWhilePlaying(true)
                mMediaPlayer!!.setOnPreparedListener(this@MediaSystem)
                mMediaPlayer!!.setOnCompletionListener(this@MediaSystem)
                mMediaPlayer!!.setOnBufferingUpdateListener(this@MediaSystem)
                mMediaPlayer!!.setOnSeekCompleteListener(this@MediaSystem)
                mMediaPlayer!!.setOnErrorListener(this@MediaSystem)
                mMediaPlayer!!.setOnInfoListener(this@MediaSystem)
                mMediaPlayer!!.setOnVideoSizeChangedListener(this@MediaSystem)
                val clazz = MediaPlayer::class.java
                val method = clazz.getDeclaredMethod("setDataSource", Context::class.java, Uri::class.java)
                method.invoke(mMediaPlayer, mBaseVideoView!!.context, mBaseVideoView!!.videoUri)
                mBaseVideoView!!.mSurfaceHodler!!.bindToMediaPlayer(mMediaPlayer)
                mMediaPlayer!!.prepareAsync()
                // fixme
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun pause() {
        mMediaHandler!!.post { mMediaPlayer!!.pause() }
    }

    override val isPlaying: Boolean
        get() = mMediaPlayer!!.isPlaying

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
            MediaInterface.Companion.sIRenderView = null
            mMediaHandler!!.post(Runnable {
                tmpMediaPlayer!!.setSurface(null)
                tmpMediaPlayer.release()
                tmpHandlerThread.quit()
            })
            mMediaPlayer = null
        }
    }

    override val currentPosition: Long
        get() = if (mMediaPlayer != null) {
            mMediaPlayer!!.currentPosition.toLong()
        } else {
            0
        }

    override val duration: Long
        get() = if (mMediaPlayer != null) {
            mMediaPlayer!!.duration.toLong()
        } else {
            0
        }

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
        Logger.d("")
        mHandler!!.post { mBaseVideoView!!.setBufferProgress(percent) }
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        mHandler!!.post { mBaseVideoView!!.onAutoCompletion() }
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        Logger.d("")
        mHandler!!.post { mBaseVideoView!!.onError(what, extra) }
        return true
    }

    override fun onInfo(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        mHandler!!.post { mBaseVideoView!!.onInfo(what, extra) }
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Logger.d("")
        mHandler!!.post { mBaseVideoView!!.onPrepared() }
    }

    override fun onSeekComplete(mediaPlayer: MediaPlayer) {
        mHandler!!.post { mBaseVideoView!!.onSeekComplete() }
    }

    override fun onVideoSizeChanged(mediaPlayer: MediaPlayer, width: Int, height: Int) {
        Logger.d("width=$width, height=$height")
        mHandler!!.post { mBaseVideoView!!.onVideoSizeChanged(width, height) }
    }
}