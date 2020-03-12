package com.qfq.tainzhi.videoplayer.media

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
open interface IRenderView {
    open fun getView(): View?
    open fun shouldWaitForResize(): Boolean
    open fun setVideoSize(videoWidth: Int, videoHeight: Int)
    open fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int)
    open fun setVideoRotation(degree: Int)
    open fun setAspectRatio(aspectRatio: Int)
    open fun addRenderCallback(callback: IRenderCallback)
    open fun removeRenderCallback(callback: IRenderCallback)
    open interface ISurfaceHolder {
        open fun bindToMediaPlayer(mp: IMediaPlayer?)
        open fun getRenderView(): IRenderView
        open fun getSurfaceHolder(): SurfaceHolder?
        open fun openSurface(): Surface?
        open fun getSurfaceTexture(): SurfaceTexture?
    }
    
    open interface IRenderCallback {
        /**
         * @param holder
         * @param width  could be 0
         * @param height could be 0
         */
        open fun onSurfaceCreated(holder: ISurfaceHolder, width: Int, height: Int)
        
        /**
         * @param holder
         * @param format could be 0
         * @param width
         * @param height
         */
        open fun onSurfaceChanged(holder: ISurfaceHolder, format: Int, width: Int, height: Int)
        open fun onSurfaceDestroyed(holder: ISurfaceHolder)
    }
    
    companion object {
        val AR_ASPECT_FIT_PARENT: Int = 0 // without clip
        val AR_ASPECT_FILL_PARENT: Int = 1 // may clip
        val AR_ASPECT_WRAP_CONTENT: Int = 2
        val AR_MATCH_PARENT: Int = 3
        val AR_16_9_FIT_PARENT: Int = 4 // w/h
        val AR_4_3_FIT_PARENT: Int = 5
    }
}