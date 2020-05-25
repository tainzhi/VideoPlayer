package com.tanzhi.qmediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/22 17:03
 * @description: 播放控制器, 添加控制按钮, 控制[VideoView]播放
 **/

class MediaController(val context: Context) {

    private lateinit var root: ViewGroup
    var isShowing = false

    lateinit var videoView: VideoView

    private lateinit var playPauseBtn: ImageButton

    companion object {
        const val DefaultTimeout = 3000
    }

    fun setParentView(parent: ViewGroup) {
        root = parent
        val content = makeControllerView()
        parent.addView(content, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun makeControllerView() : View{
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // TODO: 2020/5/23 orientaion inflate port or land
        val root = inflate.inflate(R.layout.media_controller_port, null)
        initControllView(root)
        return root
    }

    private fun initControllView(view: View) {
        view.findViewById<TextView>(R.id.videoTitleTv).text = videoView.videoTitle
        playPauseBtn = view.findViewById<ImageButton>(R.id.playPauseIv)
        playPauseBtn.setOnClickListener {
            doPausePause()
        }
        updatePausePlay()
    }

    private fun updatePausePlay() {
        if (videoView.isPlaying) {
            playPauseBtn.setImageResource(R.drawable.ic_play)
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun doPausePause() {
        if (videoView.isPlaying) {
            videoView.start()
        } else {
            videoView.pause()
        }
    }

    fun hide() {

    }

    fun show() {

    }

    // TODO: 2020/5/23
    // private val layoutChangeListener =
    //         OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
    //             updateFloatingWindowLayout()
    //             if (isShowing) {
    //                 windowManager.updateViewLayout(decor, decorLayoutParams)
    //             }
    //         }

}