package com.tainzhi.android.ffmpeg

import android.util.Log

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/25 15:16
 * @description:
 **/

class FFmpegCmd {
    companion object {
        const val TAG = "FFmpegCmd"
        init {
            System.loadLibrary("avdevice")
            System.loadLibrary("avutil")
            System.loadLibrary("avcodec")
            System.loadLibrary("swresample")
            System.loadLibrary("avformat")
            System.loadLibrary("swscale")
            System.loadLibrary("avfilter")
            System.loadLibrary("postproc")
            System.loadLibrary("ffmpeg-invoke")
        }

    }

    fun execute(cmd: Array<String>) {
        Log.d(TAG, "${cmd.toString()}")
        runCmd(cmd.size, cmd)
    }

    private external fun runCmd(cmdLen: Int, cmd: Array<String>): Int
}