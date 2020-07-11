package com.tanzhi.qmediaplayer.controller

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
    protected val audioManager by lazy {
        (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).apply {
            requestAudioFocus(onAudioChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
        }
    }
    private val onAudioChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> Unit
            AudioManager.AUDIOFOCUS_LOSS -> {
                // TODO: 2020/7/11  失去声音焦点怎么处理
                // releaseAllVideos()
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