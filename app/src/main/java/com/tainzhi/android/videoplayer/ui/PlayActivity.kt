package com.tainzhi.android.videoplayer.ui

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.smarx.notchlib.NotchScreenManager
import com.tainzhi.android.videoplayer.R
import com.tanzhi.qmediaplayer.AutoFullScreenListener
import com.tanzhi.qmediaplayer.Constant
import com.tanzhi.qmediaplayer.MediaController
import com.tanzhi.qmediaplayer.VideoView
import com.tanzhi.qmediaplayer.render.glrender.effect.NoEffect

class PlayActivity : AppCompatActivity() {
    private lateinit var autoFullScreenListener: AutoFullScreenListener
    private lateinit var sensorManager: SensorManager
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)
        videoView = findViewById<VideoView>(R.id.video_view)
        val intent = intent
        val mVideoUri = intent.data
        val bundle = intent.extras
        val mVideoTitle = bundle!!.getString(VIDEO_NAME)
        val mVideoDuration = bundle.getLong(VIDEO_DURATION, 0)
        val mVideoProgress = bundle.getLong(VIDEO_PROGRESS, 0)
        val resolution = bundle.getString(VIDEO_RESOLUTION)
        var orientation = 0
        // 根据视频的高宽,判断是竖屏还是横屏播放
        // resolution 1920x1080, 1920为height, 1080为width
        if (!resolution.isNullOrEmpty()) {
            val heightAndwidth = resolution.split("x")
            val height = heightAndwidth[0]
            val width = heightAndwidth[1]
            if (height > width) {
                orientation = 90
            }
        }
        videoView.run {
            videoTitle = mVideoTitle!!
            renderType = Constant.RenderType.GL_SURFACE_VIEW
            screenOrientation = orientation
            startFullScreenDirectly(this@PlayActivity, mVideoUri!!)
            setEffect(NoEffect())
            mediaController = MediaController(this@PlayActivity).apply {
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
                backToFullScreenCallback = { starter, uri, name, duration, progress ->
                    val intent = Intent(starter, PlayActivity::class.java)
                    intent.data = uri
                    intent.putExtra(VIDEO_NAME, name)
                    intent.putExtra(VIDEO_DURATION, duration)
                    intent.putExtra(VIDEO_PROGRESS, progress)
                    intent.putExtra(VIDEO_RESOLUTION, resolution)
                    starter.startActivity(intent)
                }
                mediaControllerCloseCallback = {
                    this@PlayActivity.onPause()
                    finish()
                }
            }
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        autoFullScreenListener = AutoFullScreenListener(videoView)

        NotchScreenManager.getInstance().setDisplayInNotch(this)
    }

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

    @RequiresApi(api = 23)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            videoView.mediaController?.requestDrawOverlayPermissionCallback?.invoke()
        }
    }

    companion object {
        private const val VIDEO_RUL = "url"
        private const val VIDEO_NAME = "name"
        private const val VIDEO_DURATION = "duration"
        private const val VIDEO_PROGRESS = "progress"
        private const val VIDEO_RESOLUTION = "resolution"
        @JvmStatic
        fun startPlay(starter: Context, uri: Uri, name: String, duration: Long = 100, progress: Long = 100, resolution: String = "") {
            val intent = Intent(starter, PlayActivity::class.java)
            intent.data = uri
            intent.putExtra(VIDEO_NAME, name)
            intent.putExtra(VIDEO_DURATION, duration)
            intent.putExtra(VIDEO_PROGRESS, progress)
            intent.putExtra(VIDEO_RESOLUTION, resolution)
            starter.startActivity(intent)
        }
    }
}