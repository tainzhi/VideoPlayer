package com.tainzhi.qmediaplayer.controller

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.view.ViewGroup

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/11 13:05
 * @description:
 **/

abstract class IController(context: Context) {

    // 当前MediaController是否显示
    var isShowing = false
    abstract fun hide()
    abstract fun show()
    abstract fun bindVideoView(viewGroup: ViewGroup)
    abstract fun changeOrientation()
    // 网络视频加载中的dialog
    open fun showLoading() {}
    open fun dismissLoading() {}
    protected val audioManager by lazy {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).apply {
            requestAudioFocus(onAudioChangeListener,
                   AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
        }
    }
    private val onAudioChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            // TODO: 2020/8/4 音频焦点处理
            // 获得音频焦点
            AudioManager.AUDIOFOCUS_GAIN -> {
                // mediaPlayer.start()
                // mediaPlayer.setVolume(1.0f, 1.6f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                // mediaPlayer.release()
                // releaseAllVideos()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level

            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                // Lost focus for a short time, but we have to stop playback.
                // We don't release the media player because playback is likely to resume
                // if (mediaPlayer.isPlaying) mediaPlayer.setVolume(0.1f, 0.1f)
            }
        }
    }


    lateinit var requestDrawOverlayPermission: () -> Unit
    lateinit var mediaControllerFloatWindowCallback: () -> Unit
    lateinit var mediaControllerCloseCallback: () -> Unit
    lateinit var backToFullScreenCallback: (starter: Context, uri: Uri, name: String, progress: Long) -> Unit

    // 悬浮窗申请权限后回调
    open val requestDrawOverlayPermissionCallback: () -> Unit = { }
}