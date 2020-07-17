package com.tainzhi.android.ffmpeg

import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/15 17:19
 * @description:
 **/

class FFmpegPlayer {

    companion object {
        const val TAG = "FFmpegPlayer"
    }


    private val playerManager = FFmpegPlayerManager()

    var playerCallback: PlayerCallback ?= null
        set(value) {
            playerManager.playerCallback = value
            field = value
        }

    var dataSource: String? = null

    val isPlaying :Boolean
        get() = playerManager.isPlayingNative()

    fun ffmpegVersion() = playerManager.ffmpegVersion()

    fun setSurface(surface: Surface) {
        playerManager.setSurfaceNative(surface)
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
}