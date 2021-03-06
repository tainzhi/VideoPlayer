package com.tainzhi.android.ffmpeg

import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/15 17:19
 * @description:
 **/

class FFmpegPlayer: SurfaceHolder.Callback {

    companion object {
        const val TAG = "FFmpegPlayer"
    }


    private val playerManager = FFmpegPlayerManager()
    private var surfaceHolder: SurfaceHolder? = null

    var playerCallback: PlayerCallback ?= null
        set(value) {
            playerManager.playerCallback = value
            field = value
        }

    var dataSource: String? = null

    val isPlaying :Boolean
        get() = playerManager.isPlayingNative()

    fun ffmpegVersion() = playerManager.ffmpegVersion()

    fun setSurfaceView(surfaceView: SurfaceView) {
        if (surfaceHolder != null) surfaceHolder?.removeCallback(this)
        surfaceHolder = surfaceView.holder
        surfaceHolder?.addCallback(this)
    }

    fun setSurface(surface: Surface) {
        playerManager.setSurfaceNative(surface)
    }

    fun prepare() {
        playerManager.prepareNative(dataSource!!)
    }

    fun start() {
        playerManager.startNative()
    }

    fun stop() {
        playerManager.stopNative()
    }

    fun release() {
        playerManager.releaseNative()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        playerManager.setSurfaceNative(surfaceHolder?.surface!!)
    }
}