package com.tainzhi.android.videoplayer.ui.play

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.tainzhi.android.videoplayer.base.ui.BaseViewBindingActivity
import com.tainzhi.android.videoplayer.databinding.ActivityVideoTestBinding
import com.tainzhi.android.videoplayer.network.ResultOf
import com.tainzhi.qmediaplayer.AutoFullScreenListener
import com.tainzhi.qmediaplayer.controller.NetMediaController
import org.koin.android.ext.android.inject

class PlayDouyuActivity : BaseViewBindingActivity<ActivityVideoTestBinding>() {
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
    private val videoView by lazy { mBinding.videoView }
    private val viewModel: PlayDouyuViewModel by inject()

    override fun onResume() {
        super.onResume()
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(autoFullScreenListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(autoFullScreenListener)
    }

    override fun initView() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        autoFullScreenListener = AutoFullScreenListener(videoView)
    }

    override fun initData() {
        val intent = intent
        val bundle = intent.extras
        val id = bundle?.getString(VIDEO_ID)
        val mVideoTitle = bundle?.getString(VIDEO_NAME)

        lifecycle.addObserver(videoView)

        videoView.videoTitle = mVideoTitle!!
        videoView.mediaController = NetMediaController(this@PlayDouyuActivity).apply {
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

        viewModel.run {
            getRoomCircuit(id!!)
            roomUrl.observe(this@PlayDouyuActivity) { state ->
                when (state) {
                    is ResultOf.Success -> {
                        // val uri = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8")
                        val uri = Uri.parse(state.data)
                        videoView.startFullScreenDirectly(this@PlayDouyuActivity, uri)
                        // 如果先加载MediaController, 将不会显示
                    }
                    is ResultOf.Error -> {
                        Snackbar.make(videoView, state.message, Snackbar.LENGTH_SHORT)
                                .setAction("退出播放界面") {
                                    finish()
                                }
                                .apply {
                                    // anchorView = bottomNavView
                                }.show()
                    }
                    else -> Unit
                }
            }
        }
    }
}