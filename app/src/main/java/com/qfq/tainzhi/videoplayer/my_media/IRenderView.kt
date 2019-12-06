package com.qfq.tainzhi.videoplayer.my_media

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import tv.danmaku.ijk.media.player.IMediaPlayer

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
        fun bindToMediaPlayer(mp: MediaPlayer?)
        fun bindToMediaPlayer(mp: IMediaPlayer?)
        fun bindToMediaPlayer(mp: ExoPlayer?)
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

    companion object {
        const val AR_ASPECT_FIT_PARENT = 0 // without clip
        const val AR_ASPECT_FILL_PARENT = 1 // may clip
        const val AR_ASPECT_WRAP_CONTENT = 2
        const val AR_MATCH_PARENT = 3
        const val AR_16_9_FIT_PARENT = 4 // w/h
        const val AR_4_3_FIT_PARENT = 5
    }
}