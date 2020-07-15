package com.tainzhi.android.ffmpeg

import android.provider.SyncStateContract
import android.view.Surface

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/15 11:09
 * @description:
 **/

class FFmpegPlayerManager {

    companion object {
        init {
            System.loadLibrary("ffmpeg-player")
        }
    }
    external fun ffmpegVersion(): String
    external fun setSurfaceNative(surface: Surface)
    external fun prepareNative(dataSource: String)
    external fun startNative()
    external fun restartNative()
    external fun stopNative()
    external fun releaseNative()
    external fun isPlayingNative(): Boolean
    external fun getDuration(): Int
    external fun seek(progress: Int)

    var playerCallback: PlayerCallback ?= null

    fun onPrepared() {
        playerCallback?.onPrepared()
    }

    fun onProgress(progress: Int) {
        playerCallback?.onProgress(progress)
    }

    fun onError(errorCode: Int) {
        val errorText = when (errorCode) {
            Constants.PlayerState.FFMPEG_ALLOC_CODEC_CONTEXT_FAIL -> "无法根据解码器创建上下文"
            Constants.PlayerState.FFMPEG_CAN_NOT_FIND_STREAMS -> "找不到媒体流信息"
            Constants.PlayerState.FFMPEG_CAN_NOT_OPEN_URL -> "打不开媒体数据源"
            Constants.PlayerState.FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL -> "根据流信息 配置上下文参数失败"
            Constants.PlayerState.FFMPEG_FIND_DECODER_FAIL -> "找不到解码器"
            Constants.PlayerState.FFMPEG_NOMEDIA -> "没有音视频"
            Constants.PlayerState.FFMPEG_READ_PACKETS_FAIL -> "读取媒体数据包失败"
            else -> "未知错误，自己去检测你的垃圾代码..."
        }
        playerCallback?.onError(errorText)
    }

}

interface PlayerCallback {
    fun onProgress(progress: Int)
    fun onPrepared()
    fun onError(errorText: String)
}