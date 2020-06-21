package com.tainzhi.android.videoplayer.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.activity_videoview_mediacontroller.*

/**
 * 使用[android.widget.VideoView] 和 [android.widget.MediaController]简单地播放视频
 */
class PlayVideoViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoUri = intent.data
        val videoName = intent.getStringExtra(VIDEO_NAME)
        val videoDuration = intent.getStringExtra(VIDEO_DURATION)
        val videoProcess = intent.getStringExtra(VIDEO_PROGRESS)

        setContentView(R.layout.activity_videoview_mediacontroller)

        val mediaController = MediaController(this)
        videoView.run {
            setMediaController(mediaController)
            setVideoURI(videoUri)
            setOnPreparedListener { _->
                start()
            }
            videoView.surfaceControl
        }

    }

    companion object {
        const val VIDEO_NAME = "video_name"
        const val VIDEO_DURATION = "video_duration"
        const val VIDEO_PROGRESS = "video_progress"

        @JvmStatic
        fun startPlay(starter: Context, uri: Uri, name: String, duration: Long = 100, progress: Long = 100) {
            val intent = Intent(starter, PlayVideoViewActivity::class.java)
                    .setData(uri)
                    .putExtra(VIDEO_NAME, name)
                    .putExtra(VIDEO_DURATION, duration)
                    .putExtra(VIDEO_PROGRESS, progress)
            starter.startActivity(intent)
        }
    }
}