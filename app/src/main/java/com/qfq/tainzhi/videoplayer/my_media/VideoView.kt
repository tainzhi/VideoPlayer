package com.qfq.tainzhi.videoplayer.my_media

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 * Description: 基本 VideoView, 没有各种操作控件 Controller
 */

class VideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mediaPlayerClass: Class<*>? = null
    private var iMediaPlayer: IMediaPlayer? = null

    var mediaPlayerType = Constant.PlayerType.SYSTEM_PLAYER

    var enableSurfaceView = true
    var enableTextureView = false

    private val renderType = Constant.RenderType.SURFACE_VIEW
    init {
        setMediaPlayer()
        initRender()
    }

    /**
     * 初始化渲染器
     */
    private fun initRender() {
        when(renderType) {
            Constant.RenderType.SURFACE_VIEW -> {
                setRenderView(SurfaceRenderView(context))
            }
            Constant.RenderType.TEXTURE_VIEW -> {
                // TextureRenderView renderView = new TextureRenderView(getContext());
                // if (mMediaPlayer != null) {
                //     renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer);
                //     renderView.setVideoSize(mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
                //     renderView.setVideoSampleAspectRatio(mMediaPlayer.getVideoSarNum(), mMediaPlayer.getVideoSarDen());
                //     renderView.setAspectRatio(mCurrentAspectRatio);
                // }
                // setRenderView(renderView);            }
            }
        }
    }

    private fun setRenderView(renderView: IRenderView) {

    }

    /**
     * 是实话MediaPlayer
     */
    private fun setMediaPlayer() {
        when (mediaPlayerType) {
            Constant.PlayerType.SYSTEM_PLAYER -> {
                mediaPlayerClass = IMediaSystem::class.java
            }
            Constant.PlayerType.IJK_PLAYER -> {
                mediaPlayerClass = IMediaIjk::class.java
            }
            Constant.PlayerType.EXO_PLAYER -> {
                mediaPlayerClass = IMediaExo::class.java
            }
        }
    }
}
