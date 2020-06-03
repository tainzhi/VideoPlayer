package com.tanzhi.qmediaplayer.render

import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import com.tanzhi.qmediaplayer.IMediaInterface
import com.tanzhi.qmediaplayer.render.glrender.GLViewRender
import com.tanzhi.qmediaplayer.render.glrender.effect.ShaderInterface

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
interface IRenderView {

    fun shouldWaitForResize(): Boolean
    fun setVideoSize(videoWidth: Int, videoHeight: Int)
    fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int)
    fun setVideoRotation(degree: Int)
    fun setAspectRatio(aspectRatio: Int)
    fun addRenderCallback(callback: IRenderCallback)
    fun removeRenderCallback(callback: IRenderCallback)

    // 截屏
    open fun takeShot(videoShotListener: VideoShotListener, highShot: Boolean = false)

    // Rnderer effect
    open var renderEffect: ShaderInterface?
    // Render
    open var render: GLViewRender?


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

    interface VideoShotListener {
        fun getBitmap(bitmap: Bitmap)
    }
}