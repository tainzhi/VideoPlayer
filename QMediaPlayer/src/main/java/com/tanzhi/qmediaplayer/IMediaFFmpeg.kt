package com.tanzhi.qmediaplayer

import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceHolder
import com.tainzhi.android.ffmpeg.FFmpegPlayer
import com.tainzhi.android.ffmpeg.PlayerCallback

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/16 20:13
 * @description: [com.tainzhi.android.ffmpeg.FFmpegPlayer]作为播放器
 **/

class IMediaFFmpeg(videoView: VideoView): IMediaInterface(videoView), PlayerCallback  {
    companion object {
        const val TAG = "FFmpegPlayer"
    }

    private var fFmpegPlayer: FFmpegPlayer? = null

    override fun prepare() {
        logI(TAG, "prepare()")
        release()
        mMediaHandlerThread = HandlerThread("QMediaPlayer")
        mMediaHandlerThread?.start()
        mMediaHandler = Handler(mMediaHandlerThread!!.looper)
        mHandler = Handler()
        mMediaHandler!!.post {
            fFmpegPlayer = FFmpegPlayer().apply {
                dataSource = mVideoView.videoUri.toString()
                prepare()
            }
        }

    }

    override fun start() {
        fFmpegPlayer?.start()
    }


    override fun pause() {
    }

    override fun resetDataSource(uri: Uri) {
    }

    override val isPlaying: Boolean
        get() = fFmpegPlayer?.isPlaying ?: false

    override fun seekTo(time: Long) {
    }

    override fun release() {
    }

    override val currentPosition: Long = 0L
    override val duration: Long =0L

    override fun setVolume(leftVolume: Float, rightVolume: Float) {
    }

    override fun setSpeed(speed: Float) {
    }

    override fun setDisplay(surfaceHolder: SurfaceHolder) {
    }

    override fun setDisplay(surface: Surface) {
        fFmpegPlayer?.setSurface(surface)
    }

    override fun onProgress(progress: Int) {
    }

    override fun onPrepared() {
        mVideoView.onPrepared()
    }

    override fun onError(errorText: String) {
    }
}