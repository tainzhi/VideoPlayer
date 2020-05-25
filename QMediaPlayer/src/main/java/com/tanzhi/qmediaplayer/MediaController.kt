package com.tanzhi.qmediaplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
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

    private var isDragging = false

    lateinit var videoView: VideoView

    private lateinit var playPauseBtn: ImageButton
    private lateinit var progressSeekbar: SeekBar
    private lateinit var endTimeTv: TextView
    private lateinit var currentTimeTv: TextView

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

    private val seekBarChangeListener = object: SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            val duration = videoView.videoDuration
            val newPosition = duration * progress / 1000L
            videoView.seekTo(duration * progress / 1000L)
            currentTimeTv.text = Util.stringForTime(newPosition)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            isDragging = true
            root.removeCallbacks(showProgress)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            isDragging = false
            setProgress()
            root.post(showProgress)
        }
    }

    private val showProgress = object :Runnable {
        override fun run() {
            val pos = setProgress()
            if (!isDragging && isShowing && videoView.isPlaying) {
                root.postDelayed(this , 1000L - (pos % 1000))
            }
        }

    }

    private fun setProgress() : Int{
        val position = videoView.videoCurrentPosition
        val duration = videoView.videoDuration
        if (duration > 0) {
            progressSeekbar.progress = 1000 * position.toInt() / duration.toInt()
        }
        currentTimeTv.text = Util.stringForTime(position)
        endTimeTv.text = Util.stringForTime(duration)
        return position.toInt()
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