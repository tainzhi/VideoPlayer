package com.tainzhi.android.videoplayer.ui.play

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tainzhi.android.common.base.ui.BaseVMActivity
import com.tainzhi.android.videoplayer.R
import com.tanzhi.qmediaplayer.AutoFullScreenListener
import com.tanzhi.qmediaplayer.controller.MediaController
import com.tanzhi.qmediaplayer.VideoView
import com.tanzhi.qmediaplayer.controller.NetMediaController
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

        videoView.videoTitle = mVideoTitle!!
        videoView.mediaController = NetMediaController(this@PlayDouyuActivity).apply {
            @RequiresApi(api = 23)
            requestDrawOverlayPermission = {
                startActivityForResult(
                        Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION").apply {
                            data = Uri.parse("package:" + context.packageName)
                        },
                        1
                )
            }
            mediaControllerFloatWindowCallback = {
                finish()
            }
            backToFullScreenCallback = { starter, uri, name, progress ->
                // val intent = Intent(starter, PlayActivity::class.java)
                // intent.data = uri
                // intent.putExtra(VIDEO_NAME, name)
                // intent.putExtra(VIDEO_PROGRESS, progress)
                // intent.putExtra(VIDEO_RESOLUTION, resolution)
                // starter.startActivity(intent)
            }
            mediaControllerCloseCallback = {
                this@PlayDouyuActivity.onPause()
                finish()
            }
        }

        mViewModel.getRoomCircuit(id!!)
    }

    override fun getLayoutResId() = R.layout.activity_video_test

    override fun startObserve() {
        mViewModel.roomCircuitId.observe(this@PlayDouyuActivity, Observer {
            val uri = Uri.parse(it)
            videoView.startFullScreenDirectly(this@PlayDouyuActivity, uri)
            // 如果先加载MediaController, 将不会显示
        })
        mViewModel.error.observe(this@PlayDouyuActivity, Observer {
            Snackbar.make(videoView, it, Snackbar.LENGTH_SHORT)
                    .setAction("退出播放界面") {
                        finish()
                    }
                    .apply {
                        // anchorView = bottomNavView
                    }.show()

        })
    }
}