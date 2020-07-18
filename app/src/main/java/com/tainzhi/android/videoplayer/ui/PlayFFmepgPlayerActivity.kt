package com.tainzhi.android.videoplayer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tainzhi.android.ffmpeg.FFmpegPlayer
import com.tainzhi.android.ffmpeg.PlayerCallback
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.activity_play_f_fmepg_player.*

class PlayFFmepgPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_f_fmepg_player)

        val player = FFmpegPlayer()
        player.setSurfaceView(ffmpegSurfaceView!!)
        player.playerCallback = object: PlayerCallback {
            override fun onProgress(progress: Int) {
                runOnUiThread {
                    // TODO: 2020/7/18 删除此处log
                    Log.d("test", "$progress")
                }
            }

            override fun onPrepared() {
                runOnUiThread {  }
                player.start()
            }

            override fun onError(errorText: String) {
            }
        }
        player.dataSource = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
        player.prepare()

    }
}