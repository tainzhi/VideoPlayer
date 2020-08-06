package com.tainzhi.android.videoplayer.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tainzhi.android.ffmpeg.FFmpegPlayer
import com.tainzhi.android.ffmpeg.PlayerCallback
import com.tainzhi.android.videoplayer.R
import kotlinx.android.synthetic.main.activity_play_f_fmepg_player.*

class PlayFFmepgPlayerActivity : AppCompatActivity() {
    private val player = FFmpegPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_f_fmepg_player)

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
        if (intent.extras?.getString(TYPE) == PlayRtmp) {
            // rtmp不能播放, librtmp编译失败或者ffmpeg连接rtmp失败
            player.dataSource = "rtmp://202.69.69.180:443/webcast/bshdlive-pc"
        } else {
            player.dataSource = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8"
        }
        player.prepare()

    }

    override fun onStop() {
        player.stop()
        super.onStop()
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    companion object {
        const val PlayRtmp = "rtmp"
        const val PlayHlv = "hlv"
        private const val TYPE = "type"
        @JvmStatic
        fun startPlay(starter: Context, type: String) {
            val intent = Intent(starter, PlayFFmepgPlayerActivity::class.java)
            intent.putExtra(TYPE, type)
            starter.startActivity(intent)
        }
    }
}