package com.tanzhi.qmediaplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceHolder
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.IjkTimedText
import java.lang.reflect.InvocationTargetException

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-11 06:55
 * @description:
 */
class IMediaIjk(videoView: VideoView) : IMediaInterface(videoView), IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    private var mIjkMediaPlayer: IjkMediaPlayer? = null
    override fun start() {
        logI(TAG, "start()")
        mIjkMediaPlayer?.start()
    }

    override fun setDisplay(surfaceHolder: SurfaceHolder) {
        mIjkMediaPlayer?.setDisplay(surfaceHolder)
    }

    override fun setDisplay(surface: Surface) {
        mIjkMediaPlayer?.setSurface(surface)
    }

    override fun prepare() {
        logI(TAG, "prepare()")
        release()
        mMediaHandlerThread = HandlerThread("VideoView")
        mMediaHandlerThread!!.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            mIjkMediaPlayer = IjkMediaPlayer().apply {
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024.toLong())
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1)
                setOnPreparedListener(this@IMediaIjk)
                setOnVideoSizeChangedListener(this@IMediaIjk)
                setOnCompletionListener(this@IMediaIjk)
                setOnErrorListener(this@IMediaIjk)
                setOnInfoListener(this@IMediaIjk)
                setOnBufferingUpdateListener(this@IMediaIjk)
                setOnSeekCompleteListener(this@IMediaIjk)
                setOnTimedTextListener(this@IMediaIjk)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setScreenOnWhilePlaying(true)
            }
            try {
                val clazz = IjkMediaPlayer::class.java
                val method = clazz.getDeclaredMethod("setDataSource", Context::class.java, Uri::class.java)
                method.invoke(mIjkMediaPlayer, mVideoView.context, mVideoView.videoUri)
                mVideoView.mSurfaceHolder?.bindToMediaPlayer(this)
                mIjkMediaPlayer?.prepareAsync()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }
    }

    override fun pause() {
        logI(TAG, "pause()")
        mIjkMediaPlayer?.pause()
    }

    override val isPlaying: Boolean
        get() = mIjkMediaPlayer?.isPlaying ?: false

    override fun seekTo(time: Long) {
        mIjkMediaPlayer?.seekTo(time)
    }

    override fun release() {
        logI(TAG, "release()")
        if (mMediaHandler != null && mMediaHandlerThread != null && mIjkMediaPlayer != null) {
            mMediaHandler!!.post {
                mMediaHandlerThread?.quit()
                mIjkMediaPlayer?.run {
                    setSurface(null)
                    reset()
                    release()
                }
            }
        }
    }

    override val currentPosition: Long
        get() = mIjkMediaPlayer?.currentPosition ?: 0

    override val duration: Long
        get() = mIjkMediaPlayer?.duration ?: 0

    override fun setVolume(leftVoluem: Float, rightVolume: Float) {
        mIjkMediaPlayer?.setVolume(leftVoluem, rightVolume)
    }

    override fun setSpeed(speed: Float) {
        mIjkMediaPlayer?.setSpeed(speed)
    }

    override fun onBufferingUpdate(iMediaPlayer: IMediaPlayer, percent: Int) {
        mHandler!!.post { mVideoView.setBufferProgress(percent) }
    }

    override fun onCompletion(iMediaPlayer: IMediaPlayer) {}
    override fun onError(iMediaPlayer: IMediaPlayer, what: Int, extra: Int): Boolean {
        mHandler!!.post { mVideoView.onError(what, extra) }
        return true
    }

    override fun onInfo(iMediaPlayer: IMediaPlayer, what: Int, extra: Int): Boolean {
        mHandler!!.post { mVideoView.onInfo(what, extra) }
        return true
    }

    override fun onPrepared(iMediaPlayer: IMediaPlayer) {
        mHandler!!.post { mVideoView.onPrepared() }
    }

    override fun onSeekComplete(iMediaPlayer: IMediaPlayer) {
        mHandler!!.post { mVideoView.onSeekCompleted() }
    }

    override fun onTimedText(iMediaPlayer: IMediaPlayer, ijkTimedText: IjkTimedText) {}
    override fun onVideoSizeChanged(iMediaPlayer: IMediaPlayer, width: Int, height: Int, sarNum: Int, sarDen: Int) {
        mHandler!!.post {
            // TODO: 2020/5/20 怎么处理sarNum, sarDen
            mVideoView.onVideoSizeChanged(width, height)
        }
    }
}