package com.tainzhi.android.ffmpeg

import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/15 17:19
 * @description:
 **/

class Player: SurfaceHolder.Callback {

    val playerManager = FFmpegPlayerManager()

    var dataSource: String? = null

    val isPlaying :Boolean
        get() = playerManager.isPlayingNative()

    fun ffmpegVersion() = playerManager.ffmpegVersion()

    fun setSurfaceView(surfaceView: SurfaceView) {

    }

    fun prepare() {
        playerManager.prepareNative(dataSource!!)
    }

    fun start() {
        playerManager.startNative()
    }

    fun release() {
        playerManager.releaseNative()
    }

    companion object {
        const val TAG = "FFmpegPlayer"
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        TODO("Not yet implemented")
    }
}