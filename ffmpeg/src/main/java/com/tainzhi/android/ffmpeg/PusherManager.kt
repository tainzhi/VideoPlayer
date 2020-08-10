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

    external fun nativePUshVideo(data: Array<Byte>)
}