package com.tanzhi.qmediaplayer

import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceHolder
import com.tanzhi.qmediaplayer.render.IRenderView

/**
 * @author: tainzhi
 * @mail: qfq61@qq.com
 * @date: 2019-11-10 18:50
 * @description: 所有 player 的基类
 */
abstract class IMediaInterface(var mVideoView: VideoView) {
    protected var mMediaHandlerThread: HandlerThread? = null
    protected var mMediaHandler: Handler? = null
    protected var mHandler: Handler? = null
    protected var mSurface: Surface? = null
    protected var mSurfaceHolder: SurfaceHolder? = null
    abstract fun start()
    abstract fun prepare()
    abstract fun pause()
    abstract fun resetDataSource(uri: Uri)
    abstract val isPlaying: Boolean
    abstract fun seekTo(time: Long)
    abstract fun release()
    abstract val currentPosition: Long
    abstract val duration: Long
    abstract fun setVolume(leftVolume: Float, rightVolume: Float)
    abstract fun setSpeed(speed: Float)
    abstract fun setDisplay(surfaceHolder: SurfaceHolder)
    abstract fun setDisplay(surface: Surface)

    companion object {
        var sIRenderView: IRenderView? = null
    }

    fun initialized(): Boolean {
        return mSurface != null || mSurfaceHolder != null
    }

}