package com.qfq.tainzhi.videoplayer.ui.activity

import android.animation.AnimatorInflater
import android.animation.LayoutTransition
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import com.qfq.tainzhi.videoplayer.FloatWindow
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.ui.activity.DefaultPlayActivity
import java.io.IOException

class DefaultPlayActivity : Activity(), SurfaceHolder.Callback {
    private var mContext: Context? = null
    private var mVideoUri: Uri? = null
    private var mVideoTitle: String? = null
    private var mVideoDuration: Long = 0
    private var mVideoProgress = 0
    private var mMediaPlayer: MediaPlayer? = null
    private var mPlayView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mParentView: View? = null
    private var mControllerTitleLayout: RelativeLayout? = null
    private var mControllerTitle: TextView? = null
    private var mControllerBarLayout: LinearLayout? = null
    private var mControllerControl: ImageView? = null
    private var mControllerProgress: SeekBar? = null
    private var mControllerFloatWindow: ImageView? = null
    
    // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
    // private LinearLayout mVideoProgressPreviewLayout;
    // private ImageView mVideoProgressPreviewImageView;
    private var mVideoPlayOrPause = false // play state is true, stop state is false;
    private val mHandler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg.what) {
                CONTROLLER_CONTROL ->                     // pause
                    if (mVideoPlayOrPause) {
                        mVideoPlayOrPause = false
                        mMediaPlayer.pause()
                        mControllerControl.setImageResource(R.drawable.ic_video_player_pause)
                    } else {
                        // play
                        mVideoPlayOrPause = true
                        mMediaPlayer.start()
                        mControllerControl.setImageResource(R.drawable.ic_video_player_play)
                    }
                CONTROLLER_SEEK_TO -> {
                    val videoProgress = msg.arg1
                    mMediaPlayer.seekTo(videoProgress)
                }
                HIDE_CONTROLLER_BAR -> {
                    Log.v(TAG, "to hide controller bar")
                    mControllerTitleLayout.setVisibility(View.INVISIBLE)
                    mControllerBarLayout.setVisibility(View.INVISIBLE)
                }
                SHOW_CONTROLLER_BAR -> {
                    Log.v(TAG, "to show controller bar")
                    mControllerTitleLayout.setVisibility(View.VISIBLE)
                    mControllerBarLayout.setVisibility(View.VISIBLE)
                }
                else -> {
                }
            }
        }
    }
    private var mIsTouchOnSeekBar = false
    private var mUpdateSeekBarThread: UpdateSeekBarThread? = null
    private var mLayoutTransition: LayoutTransition? = null
    private val mContollerControlClickListener: View.OnClickListener? = View.OnClickListener {
        Log.v(TAG, "onClick() ControllerClick")
        val msg = mHandler.obtainMessage()
        msg.what = CONTROLLER_CONTROL
        mHandler.sendMessage(msg)
    }
    private val mControllerFloatWindowClickListener: View.OnClickListener? = View.OnClickListener {
        Log.v(TAG, "hide current window, to open float window")
        // SingleVideoPlayActivity finish, then back to home screen
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        this@DefaultPlayActivity.startActivity(homeIntent)
        finish()
        FloatWindow.Companion.newInstance(applicationContext,
                                          mVideoUri, mVideoTitle, mMediaPlayer.getCurrentPosition(), mVideoDuration).showFloatWindow()
    }
    private val mOnTouchListener: OnTouchListener? = OnTouchListener { v, event ->
        Log.v(TAG, "onTouch")
        val state = event.action
        when (state) {
            MotionEvent.ACTION_UP -> {
                mHandler.removeMessages(SHOW_CONTROLLER_BAR)
                mHandler.sendEmptyMessage(SHOW_CONTROLLER_BAR)
                mHandler.removeMessages(HIDE_CONTROLLER_BAR)
                mHandler.sendEmptyMessageDelayed(HIDE_CONTROLLER_BAR, HIDE_CONTROLLER_BAR_DELAY.toLong())
            }
            else -> {
            }
        }
        true
    }
    private val mOnPreparedListener: MediaPlayer.OnPreparedListener? = MediaPlayer.OnPreparedListener {
        Log.v(TAG, "MediaPlayer.onPrepared()")
        mMediaPlayer.seekTo(mVideoProgress)
        mMediaPlayer.start()
        mVideoPlayOrPause = true
        mUpdateSeekBarThread = UpdateSeekBarThread("UpdateSeekBarThread")
        mUpdateSeekBarThread.start()
        // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
        // startGetVideoPreviewFrame(mVideoUri, mVideoDuration);
        Log.v(TAG, "MediaPlayer.start()")
    }
    private val mOnCompletionListener: MediaPlayer.OnCompletionListener? = MediaPlayer.OnCompletionListener {
        Log.v(TAG, "MediaPlayer.onCompletion()")
        mUpdateSeekBarThread.stop_thread()
        finish()
    }
    private val mOnSeekBarChangeListener: OnSeekBarChangeListener? = object : OnSeekBarChangeListener {
        override fun onProgressChanged(
                seekBar: SeekBar?, progress: Int,
                b: Boolean) {
            if (b) {
                val videoProgress = (progress * 1.0 / 100 * mVideoDuration) as Int
                val msg = Message()
                msg.what = CONTROLLER_SEEK_TO
                msg.arg1 = videoProgress
                mHandler.sendMessage(msg)
                mVideoProgress = progress
                
                // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
                // int width = seekBar.getWidth();
                // long time = progress * mVideoDuration / 100;
                // int offset =(int)(width - 75) /100;
                // showVideoPreviewFrame(mVideoUri, time, mVideoProgressPreviewImageView);
                // RelativeLayout.LayoutParams layoutParams =
                //         (RelativeLayout.LayoutParams) mVideoProgressPreviewLayout.getLayoutParams();
                // layoutParams.leftMargin = offset;
                // mVideoProgressPreviewLayout.setLayoutParams(layoutParams);
            }
        }
        
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            mIsTouchOnSeekBar = true
            // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
            // mVideoProgressPreviewLayout.setVisibility(View.VISIBLE);
        }
        
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            mIsTouchOnSeekBar = false
            if (mVideoProgress >= 0) {
                seekBar.setProgress((mVideoProgress / mVideoDuration * 100) as Int)
            }
            // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
            // mVideoProgressPreviewLayout.setVisibility(View.GONE);
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(TAG, "onCreate()")
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mContext = this
        val intent = intent
        mVideoUri = intent.data
        val bundle = intent.extras
        mVideoTitle = bundle.getString("title")
        mVideoDuration = bundle.getLong("duration", 0)
        mVideoProgress = bundle.getInt("progress", 0)
        setContentView(R.layout.activity_default_video_player)
        mParentView = findViewById(R.id.activity_default_video_parent) as View
        mPlayView = findViewById<View?>(R.id.activity_default_video_player_player_view) as SurfaceView
        mControllerTitleLayout = findViewById<View?>(R.id.l_video_player_top_panel) as RelativeLayout
        mControllerTitle = findViewById<View?>(R.id.video_player_top_panel_video_title) as TextView
        mControllerBarLayout = findViewById<View?>(R.id.l_video_player_bottom_panel) as LinearLayout
        mControllerControl = findViewById<View?>(R.id.video_player_bottom_panel_play) as ImageView
        mControllerProgress = findViewById<View?>(R.id.activity_default_video_player_player_controller_progress) as SeekBar
        mControllerFloatWindow = findViewById<View?>(R.id.video_player_bottom_panel_float_window) as ImageView
        // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
        // mVideoProgressPreviewLayout =
        //         (LinearLayout)findViewById(R.id.video_player_progress_preview_layout);
        // mVideoProgressPreviewImageView =
        //         (ImageView)findViewById(R.id.video_player_progress_preview_iv);
        
        //Hide TitleBar and ControllerBar 5s later
        mHandler.sendEmptyMessageDelayed(HIDE_CONTROLLER_BAR, HIDE_CONTROLLER_BAR_DELAY.toLong())
        mPlayView.setOnTouchListener(mOnTouchListener)
        mControllerTitle.setText(mVideoTitle)
        mControllerControl.setOnClickListener(mContollerControlClickListener)
        mControllerFloatWindow.setOnClickListener(mControllerFloatWindowClickListener)
        mControllerProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
        mSurfaceHolder = mPlayView.getHolder()
        mSurfaceHolder.addCallback(this)
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener)
        try {
            mMediaPlayer.setDataSource(this, mVideoUri)
            mMediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.e(TAG, mVideoUri.toString() + " IOException!!!")
        }
        initTransition()
    }
    
    override fun onDestroy() {
        Log.v(TAG, "onDestroy()")
        super.onDestroy()
        if (mMediaPlayer != null) {
            mMediaPlayer.release()
        }
        mMediaPlayer = null
    }
    
    override fun surfaceCreated(holder: SurfaceHolder?) {
        // TODO Auto-generated method stub
        mMediaPlayer.setDisplay(mSurfaceHolder)
    }
    
    override fun surfaceChanged(
            holder: SurfaceHolder?, format: Int, width: Int,
            height: Int) {
        // TODO Auto-generated method stub
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        // TODO Auto-generated method stub
    }
    
    private fun initTransition() {
        mLayoutTransition = LayoutTransition()
        val parentContainer = findViewById<View?>(R.id.activity_default_video_parent) as FrameLayout
        parentContainer.layoutTransition = mLayoutTransition
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING,
                                      AnimatorInflater.loadAnimator(mContext,
                                                                    R.animator.animator_single_video_player_layout_appear))
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING,
                                      AnimatorInflater.loadAnimator(mContext,
                                                                    R.animator.animator_single_video_player_layout_disappearing))
    }
    
    private inner class UpdateSeekBarThread(threadName: String?) : Thread() {
        @Volatile
        var isRuuning = true
        override fun run() {
            super.run()
            while (mMediaPlayer != null && isRuuning) {
                try {
                    if (!mIsTouchOnSeekBar && mMediaPlayer.isPlaying()) {
                        val videoProgress = mMediaPlayer.getCurrentPosition()
                        mControllerProgress.setProgress((videoProgress * 1.0 / mVideoDuration * 100) as Int)
                    }
                    try {
                        this.sleep(100)
                    } catch (e: InterruptedException) {
                        //FIXME: exception
                    }
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "MediaPlay state wrong ", e)
                }
            }
        }
        
        fun stop_thread() {
            isRuuning = false
        }
        
        init {
            this.name = threadName
            isRuuning = true
        }
    } // REFACTOR: 2019/6/17 待重构: 进度条小窗口预览视频
    
    // private void startGetVideoPreviewFrame(Uri url, long videoDuration) {
    //     for (int i = 0; i < 100; i++) {
    //         long time = i * videoDuration / 100;
    //         int width = WindowUtil.dip2px(this, 150);
    //         int height = WindowUtil.dip2px(this, 100);
    //         Glide.with(this.getApplicationContext())
    //                 .setDefaultRequestOptions(
    //                         new RequestOptions()
    //                                 .frame(1000 * time)
    //                                 .override(width, height)
    //                                 .centerCrop())
    //                 .load(url).preload(width, height);
    //     }
    // }
    //
    // private void showVideoPreviewFrame(Uri url, long time,
    //                                    ImageView imageView) {
    //     int width = WindowUtil.dip2px(this, 150);
    //     int height = WindowUtil.dip2px(this, 100);
    //     Glide.with(this.getApplicationContext())
    //             .setDefaultRequestOptions(
    //                     new RequestOptions()
    //                             .onlyRetrieveFromCache(true)
    //                             .frame(1000 * time)
    //                             .override(width, height)
    //                             .dontAnimate()
    //                             .centerCrop())
    //             .load(url)
    //             .into(imageView);
    // }
    companion object {
        private val TAG: String? = "VideoPlayer/DefaultPlayActivity"
        private const val CONTROLLER_CONTROL = 0
        private const val CONTROLLER_SEEK_TO = 1
        private const val HIDE_CONTROLLER_BAR = 2
        private const val SHOW_CONTROLLER_BAR = 3
        private const val HIDE_CONTROLLER_BAR_DELAY = 5000
    }
}