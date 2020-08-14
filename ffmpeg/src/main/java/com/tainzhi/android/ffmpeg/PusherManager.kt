package com.tainzhi.android.ffmpeg

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/8/10 23:16
 * @description:
 **/

class PusherManager {
    companion object {
        const val TAG = "PusherManager"

        init {
            System.loadLibrary("pusher")
        }
    }

    external fun nativeInit(isMediaCodec: Boolean)
    external fun nativeStart(path: String)
    external fun nativePushVideo(data: ByteArray)
    external fun nativeSetVideoEncoderInfo(width: Int, height: Int, fps: Int, bit: Int)
    external fun nativeSetAudioEncoderInfo(sampleRate: Int, channels: Int)
    external fun nativePushAudio(data: ByteArray)
    external fun pushH264(h264Data: ByteArray, type: Int, timeStamp: Int)
    external fun pushAACData(audio: ByteArray, length: Int, timeStamp: Int)
    external fun nativeRelease()
}