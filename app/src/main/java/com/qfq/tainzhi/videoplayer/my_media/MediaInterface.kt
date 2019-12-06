package com.qfq.tainzhi.videoplayer.my_media

import android.os.Handler
import android.os.HandlerThread

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 18:50
 * @description: 所有 player 的基类
 */
abstract class MediaInterface(var mBaseVideoView: BaseVideoView?) {
    protected var mMediaHandlerThread: HandlerThread? = null
    protected var mMediaHandler: Handler? = null
    protected var mHandler: Handler? = null
    abstract fun start()
    abstract fun prepare()
    abstract fun pause()
    abstract val isPlaying: Boolean
    abstract fun seekTo(time: Long)
    abstract fun release()
    abstract val currentPosition: Long
    abstract val duration: Long
    abstract fun setVolume(leftVoluem: Float, rightVolume: Float)
    abstract fun setSpeed(speed: Float)

    companion object {
        var sIRenderView: IRenderView? = null
    }

}