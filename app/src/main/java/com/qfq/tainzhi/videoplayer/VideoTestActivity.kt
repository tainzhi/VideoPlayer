package com.qfq.tainzhi.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qfq.tainzhi.videoplayer.my_media.BaseVideoView

class VideoTestActivity constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)
        val videoView: BaseVideoView? = findViewById(R.id.video_view)
        val intent: Intent? = getIntent()
        val mVideoUri: Uri? = intent.getData()
        val bundle: Bundle? = intent.getExtras()
        val mVideoTitle: String? = bundle.getString("title")
        val mVideoDuration: Long = bundle.getLong("duration", 0)
        val mVideoProgress: Int = bundle.getInt("progress", 0)
        videoView.setUp(mVideoUri)
    }
}