package com.tainzhi.android.ffmpeg

import android.util.Log


object FFmpegInvoker {
    private var sCallback: Callback? = null
    external fun exec(argc: Int, argv: Array<String>): Int
    external fun exit()
    external fun getConfigInfo(): String
    external fun getAVCodecInfo(): String
    external fun getAVFormatInfo(): String
    external fun getAVFilterInfo(): String

    fun ffmpegVersion() {
        exec("ffmpeg -version")
    }

    fun exec(cmd: String, listener: Callback ?= null) {
        sCallback = listener
        val cmds: Array<String> = cmd.split(" ".toRegex()).toTypedArray()
        Log.i(TAG, "ffmpeg cmd:$cmd")
        exec(cmds.size, cmds)
    }

    /**
     * FFmpeg执行结束回调，由C代码中调用
     */
    @JvmStatic
    fun onExecuted(ret: Int) {
        if (sCallback != null) {
            if (ret == 0) {
                sCallback?.onProgress(1f)
                sCallback?.onSuccess()
            } else {
                sCallback?.onFailure()
            }
        }
    }

    /**
     * FFmpeg执行进度回调，由C代码调用
     */
    @JvmStatic
    fun onProgress(percent: Float) {
        if (sCallback != null) {
            sCallback?.onProgress(percent)
        }
    }

    interface Callback {
        fun onSuccess()
        fun onFailure()
        fun onProgress(percent: Float)
    }

    const val TAG = "FFmpegInvoker"

    init {
        System.loadLibrary("avdevice")
        System.loadLibrary("avutil")
        System.loadLibrary("avcodec")
        System.loadLibrary("swresample")
        System.loadLibrary("avformat")
        System.loadLibrary("swscale")
        System.loadLibrary("avfilter")
        System.loadLibrary("postproc")

        System.loadLibrary("rtmp")

        System.loadLibrary("ffmpeg-invoke")
    }

}