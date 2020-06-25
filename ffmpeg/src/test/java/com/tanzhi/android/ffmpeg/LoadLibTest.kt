package com.tanzhi.android.ffmpeg

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LoadLibTest {
    @Test
    fun loadLib() {

    }
    // static {
    //     System.loadLibrary("avdevice");
    //     System.loadLibrary("avutil");
    //     System.loadLibrary("avcodec");
    //     System.loadLibrary("swresample");
    //     System.loadLibrary("avformat");
    //     System.loadLibrary("swscale");
    //     System.loadLibrary("avfilter");
    //     System.loadLibrary("postproc");
    //     System.loadLibrary("ffmpeg-invoke");
    // }
    //
    // private static native int run(int cmdLen, String[] cmd);
    //
    // public static int runCmd(String[] cmd){
    //     return run(cmd.length,cmd);
    // }
    companion object {
        System.loadLibrary("avdevice")
    }

}