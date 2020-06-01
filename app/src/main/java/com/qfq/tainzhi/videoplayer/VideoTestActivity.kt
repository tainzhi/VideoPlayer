package com.qfq.tainzhi.videoplayer

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tanzhi.qmediaplayer.AutoFullScreenListener
import com.tanzhi.qmediaplayer.Constant
import com.tanzhi.qmediaplayer.MediaController
import com.tanzhi.qmediaplayer.VideoView
import com.tanzhi.qmediaplayer.render.glrender.effect.BlackAndWhiteEffect

class VideoTestActivity : AppCompatActivity() {
    private lateinit var autoFullScreenListener: AutoFullScreenListener
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)
        val videoView = findViewById<VideoView>(R.id.video_view)
        val intent = intent
        val mVideoUri = intent.data
        val bundle = intent.extras
        var mVideoTitle = bundle!!.getString(VIDEO_NAME)
        val mVideoDuration = bundle.getLong(VIDEO_DURATION, 0)
        val mVideoProgress = bundle.getLong(VIDEO_PROGRESS, 0)
        videoView.run {
            videoTitle = mVideoTitle!!
            renderType = Constant.RenderType.GL_SURFACE_VIEW
            startFullScreenDirectly(this@VideoTestActivity, mVideoUri!!)
            setEffect(BlackAndWhiteEffect())
            mediaController = MediaController(this@VideoTestActivity)
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        autoFullScreenListener = AutoFullScreenListener(videoView)
    }

    override fun onResume() {
        super.onResume()
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(autoFullScreenListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(autoFullScreenListener)
    }

    companion object {
        private const val VIDEO_RUL = "url"
        private const val VIDEO_NAME = "name"
        private const val VIDEO_DURATION = "duration"
        private const val VIDEO_PROGRESS = "progress"
        @JvmStatic
        fun startPlay(starter: Context, uri: Uri, name: String?, duration: Long, progress: Long) {
            val intent = Intent(starter, VideoTestActivity::class.java)
            intent.data = uri
            intent.putExtra(VIDEO_NAME, name)
            intent.putExtra(VIDEO_DURATION, duration)
            intent.putExtra(VIDEO_PROGRESS, progress)
            starter.startActivity(intent)
        }
    }
}