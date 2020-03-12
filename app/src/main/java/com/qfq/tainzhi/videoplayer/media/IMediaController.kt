package com.qfq.tainzhi.videoplayer.media

import android.view.View
import android.widget.MediaController.MediaPlayerControl

/**
 * Created by muqing on 2019/6/1.
 * Email: qfq61@qq.com
 */
open interface IMediaController {
    open fun hide()
    open fun isShowing(): Boolean
    open fun setAnchorView(view: View?)
    open fun setEnabled(enabled: Boolean)
    open fun setMediaPlayer(player: MediaPlayerControl?)
    open fun show(timeout: Int)
    open fun show()
    
    //----------
    // Extends
    //----------
    open fun showOnce(view: View?)
}