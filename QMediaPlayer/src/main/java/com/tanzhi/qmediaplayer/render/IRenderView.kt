package com.tanzhi.qmediaplayer.render

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.tanzhi.qmediaplayer.IMediaInterface

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
interface IRenderView {
    val view: View
    fun shouldWaitForResize(): Boolean
    fun setVideoSize(videoWidth: Int, videoHeight: Int)
    fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int)
    fun setVideoRotation(degree: Int)
    fun setAspectRatio(aspectRatio: Int)
    fun addRenderCallback(callback: IRenderCallback)
    fun removeRenderCallback(callback: IRenderCallback)

    interface ISurfaceHolder {
        // fun bindToMediaPlayer(mp: MediaPlayer)
        // fun bindToMediaPlayer(mp: IMediaPlayer)
        // fun bindToMediaPlayer(mp: ExoPlayer)
        fun bindToMediaPlayer(mp: IMediaInterface)
        val renderView: IRenderView
        val surfaceHolder: SurfaceHolder?
        fun openSurface(): Surface?
        val surfaceTexture: SurfaceTexture?
    }

    interface IRenderCallback {
        /**
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        fun onSurfaceCreated(holder: ISurfaceHolder, width: Int, height: Int)

        /**
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        fun onSurfaceChanged(holder: ISurfaceHolder, format: Int, width: Int, height: Int)

        fun onSurfaceDestroyed(holder: ISurfaceHolder)
    }
}