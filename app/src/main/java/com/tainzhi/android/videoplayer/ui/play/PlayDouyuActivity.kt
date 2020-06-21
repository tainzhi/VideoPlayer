package com.tainzhi.android.videoplayer.ui.play

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.tainzhi.android.common.base.ui.BaseVMActivity
import com.tainzhi.android.videoplayer.R
import com.tanzhi.qmediaplayer.AutoFullScreenListener
import com.tanzhi.qmediaplayer.MediaController
import com.tanzhi.qmediaplayer.VideoView
import org.koin.androidx.viewmodel.ext.android.getViewModel

class PlayDouyuActivity : BaseVMActivity<PlayDouyuViewModel>() {

    companion object {
        private const val VIDEO_ID = "id"
        private const val VIDEO_NAME = "name"

        @JvmStatic
        fun startPlay(starter: Context, roomId: String, roomName: String) {
            val intent = Intent(starter, PlayDouyuActivity::class.java)
            intent.putExtra(VIDEO_ID, roomId)
            intent.putExtra(VIDEO_NAME, roomName)
            starter.startActivity(intent)
        }
    }

    private lateinit var autoFullScreenListener: AutoFullScreenListener
    private lateinit var sensorManager: SensorManager
    private lateinit var videoView: VideoView

    override fun onResume() {
        super.onResume()
        videoView.onResume()
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(autoFullScreenListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        videoView.onResume()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(autoFullScreenListener)
        videoView.onPause()
    }

    override fun onStop() {
        super.onStop()
        videoView.onStop()
    }

    override fun initVM(): PlayDouyuViewModel = getViewModel()

    override fun initView() {
        videoView = findViewById<VideoView>(R.id.video_view)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        autoFullScreenListener = AutoFullScreenListener(videoView)
    }

    override fun initData() {
        val intent = intent
        val bundle = intent.extras
        val id = bundle?.getString(VIDEO_ID)
        val mVideoTitle = bundle?.getString(VIDEO_NAME)

        videoView.run {
            videoTitle = mVideoTitle!!
            // System MediaPlayer也可以播放视频
            // mediaPlayerType = Constant.PlayerType.IJK_PLAYER
            // setEffect(NoEffect())
        }

        mViewModel.getRoomCircuit(id!!)
    }

    override fun getLayoutResId() = R.layout.activity_video_test

    override fun startObserve() {
        mViewModel.roomCircuitId.observe(this@PlayDouyuActivity, Observer {
            val uri = Uri.parse(it)
            Logger.d("play douyu: ${uri.toString()}")
            videoView.startFullScreenDirectly(this@PlayDouyuActivity, uri)
            // 如果先加载MediaController, 将不会显示
            videoView.mediaController = MediaController(this@PlayDouyuActivity)
        })
    }
}