package com.qfq.tainzhi.videoplayer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.TextView
import com.qfq.tainzhi.videoplayer.ui.activity.DefaultPlayActivity
import com.qfq.tainzhi.videoplayer.util.SettingUtil
import java.io.IOException

/**
 * Created by Administrator on 2016/1/22.
 */
class FloatWindow constructor(private val mContext: Context?, private val mVideoUri: Uri?, private val mVideoTitle: String?, private val mVideoProgress: Int, private val mVideoDuration: Long) : SurfaceHolder.Callback {
    private var mFloatWindowView: View? = null
    private var mFloatWindowBack: ImageView? = null
    private var mFloatWindowViewTitle: TextView? = null
    private var mFloatWindowClose: ImageView? = null
    private var mFloatWindowPlayView: SurfaceView? = null
    private val mWindowManager: WindowManager?
    private var mLayoutParams: WindowManager.LayoutParams? = null
    private var mWindowWidth: Int = 0
    private var mWindowHeight: Int = 0
    private var mFloatWindowPositionX: Int = 0
    private var mFloatWindowPositionY: Int = 0
    private var mFloatWindowWidth: Int = 0
    private var mFloatWindowHeight: Int = 0
    private var mMediaPlayer: MediaPlayer? = null
    private val mPlayView: SurfaceView? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private val mOnPreparedListener: MediaPlayer.OnPreparedListener? = object : MediaPlayer.OnPreparedListener {
        public override fun onPrepared(mp: MediaPlayer?) {
            Log.v(TAG, "MediaPlayer.onPrepared()")
            mMediaPlayer.seekTo(mVideoProgress)
            mMediaPlayer.start()
            Log.v(TAG, "MediaPlayer.start()")
        }
    }
    private val mOnCompletionListener: MediaPlayer.OnCompletionListener? = object : MediaPlayer.OnCompletionListener {
        public override fun onCompletion(mp: MediaPlayer?) {
            Log.v(TAG, "MediaPlayer.onCompletion()")
            closeFloatWindow()
        }
    }
    private val mOnTouchListener: OnTouchListener? = object : OnTouchListener {
        public override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            val lastX: Float = event.getRawX()
            val lastY: Float = event.getRawY()
            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> Log.v(TAG, "onTouch ACTION_DOWN")
                MotionEvent.ACTION_MOVE -> {
                    Log.v(TAG, "onTouch ACTION_MOVE")
                    // TODO: 2016/1/24 fix ACTION_MOVE shake issue
                    val distanceX: Float = lastX - mFloatWindowPositionX
                    val distanceY: Float = lastY - mFloatWindowPositionY
                    if (Math.abs(distanceX) > 50 && Math.abs(distanceY) > 50) {
                        mFloatWindowPositionX = event.getRawX() as Int
                        mFloatWindowPositionY = event.getRawY() as Int
                        if (mFloatWindowPositionX > mWindowWidth - mFloatWindowWidth) {
                            mFloatWindowPositionX = mWindowWidth - mFloatWindowWidth
                        }
                        if (mFloatWindowPositionY > mWindowHeight - mFloatWindowHeight) {
                            mFloatWindowPositionY = mWindowHeight - mFloatWindowHeight
                        }
                        mLayoutParams.x = mFloatWindowPositionX
                        mLayoutParams.y = mFloatWindowPositionY
                        updateFloatWindow()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.v(TAG, "onTouch ACTION_UP")
                    mFloatWindowPositionX = event.getRawX() as Int
                    mFloatWindowPositionY = event.getRawY() as Int
                    setFloatWindowPosition()
                }
                else -> {
                }
            }
            return true
        }
    }
    
    fun showFloatWindow() {
        Log.v(TAG, "showFloatWindow()")
        initView()
        initMediaPlayer()
    }
    
    fun updateFloatWindow() {
        Log.v(TAG, "updateFloatWindow()")
        mWindowManager.updateViewLayout(mFloatWindowView, mLayoutParams)
    }
    
    fun closeFloatWindow() {
        Log.v(TAG, "closeFloatWindow()")
        setFloatWindowPosition()
        mMediaPlayer.release()
        mWindowManager.removeView(mFloatWindowView)
        mInstance = null
    }
    
    private fun initView() {
        Log.v(TAG, "initView")
        mFloatWindowView = View.inflate(mContext, R.layout.float_window_layout, null)
        mFloatWindowBack = mFloatWindowView.findViewById<View?>(R.id.float_window_title_back) as ImageView?
        mFloatWindowViewTitle = mFloatWindowView.findViewById<View?>(R.id.float_window_video_title) as TextView?
        mFloatWindowViewTitle.setText(mVideoTitle)
        mFloatWindowClose = mFloatWindowView.findViewById<View?>(R.id.float_window_title_close) as ImageView?
        mFloatWindowPlayView = mFloatWindowView.findViewById<View?>(R.id.float_window_play_view) as SurfaceView?
        mFloatWindowPlayView.setOnTouchListener(mOnTouchListener)
        mFloatWindowBack.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                Log.v(TAG, "back to full screen play window")
                // TODO: 2016/1/23 back to full play window
                val backIntent: Intent? = Intent(mContext, DefaultPlayActivity::class.java)
                backIntent.setData(mVideoUri)
                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                backIntent.putExtra("title", mVideoTitle)
                backIntent.putExtra("duration", mVideoDuration)
                backIntent.putExtra("progress", mMediaPlayer.getCurrentPosition())
                mContext.startActivity(backIntent)
                closeFloatWindow()
            }
        })
        mFloatWindowClose.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                Log.v(TAG, "close float window")
                closeFloatWindow()
            }
        })
        mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams.format = PixelFormat.TRANSPARENT
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        //        mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        mLayoutParams.gravity = Gravity.LEFT or Gravity.TOP
        // width : heidht = 16:9, this is for video
        // but the whole floatwindow height will take title height into account
        mFloatWindowWidth = mContext.getResources().getDimension(R.dimen.float_window_width) as Int
        mLayoutParams.width = mFloatWindowWidth
        mFloatWindowHeight = mContext.getResources().getDimension(R.dimen.float_window_height) as Int
        mLayoutParams.height = mFloatWindowHeight
        getWindowWidthAndHeight()
        getFloatWindowPosition()
        mLayoutParams.x = mFloatWindowPositionX
        mLayoutParams.y = mFloatWindowPositionY
        mWindowManager.addView(mFloatWindowView, mLayoutParams)
    }
    
    private fun initMediaPlayer() {
        Log.v(TAG, "initMediaPlayer")
        mSurfaceHolder = mFloatWindowPlayView.getHolder()
        mSurfaceHolder.addCallback(this)
        mMediaPlayer = MediaPlayer()
        mMediaPlayer.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer.setOnCompletionListener(mOnCompletionListener)
        try {
            mMediaPlayer.setDataSource(mContext, mVideoUri)
            mMediaPlayer.prepareAsync()
        } catch (e: IOException) {
            Log.e(TAG, mVideoUri.toString() + " IOException!!!")
        }
    }
    
    private fun setFloatWindowPosition() {
        val sp: SharedPreferences? = mContext.getSharedPreferences(SettingUtil.Companion.VIDEO_PLAYER_SETTING_PREFERENCE, Context.MODE_PRIVATE)
        sp.edit().putInt(SettingUtil.Companion.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X, mFloatWindowPositionX).commit()
        sp.edit().putInt(SettingUtil.Companion.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y, mFloatWindowPositionY).commit()
    }
    
    private fun getFloatWindowPosition() {
        val sp: SharedPreferences? = mContext.getSharedPreferences(SettingUtil.Companion.VIDEO_PLAYER_SETTING_PREFERENCE, Context.MODE_PRIVATE)
        mFloatWindowPositionX = sp.getInt(SettingUtil.Companion.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_X, -1)
        mFloatWindowPositionY = sp.getInt(SettingUtil.Companion.VIDEO_PLAYER_SETTING_FLOAT_WINDOW_POSITION_Y, -1)
        
        // First enter float window, set the window in center
        if (mFloatWindowPositionX == -1 || mFloatWindowPositionY == -1) {
            // layout float window center initially
            mFloatWindowPositionX = (mWindowWidth - mLayoutParams.width) / 2
            mFloatWindowPositionY = (mWindowHeight - mLayoutParams.height) / 2
        }
    }
    
    private fun getWindowWidthAndHeight() {
        val dispalyMetrics: DisplayMetrics? = DisplayMetrics()
        mWindowManager.getDefaultDisplay().getMetrics(dispalyMetrics)
        var windowWidth: Int = dispalyMetrics.widthPixels
        var windowHeight: Int = dispalyMetrics.heightPixels
        
        // the current window orientation is landscape, but we should layout float window in protrait orientation
        // so the windowWidth is height in protrait orientation
        val temp: Int = windowWidth
        windowWidth = windowHeight
        windowHeight = temp
        mWindowWidth = windowWidth
        mWindowHeight = windowHeight
    }
    
    public override fun surfaceCreated(holder: SurfaceHolder?) {
        // TODO Auto-generated method stub
        mMediaPlayer.setDisplay(mSurfaceHolder)
    }
    
    public override fun surfaceChanged(
            holder: SurfaceHolder?, format: Int, width: Int,
            height: Int) {
        // TODO Auto-generated method stub
    }
    
    public override fun surfaceDestroyed(holder: SurfaceHolder?) {
        // TODO Auto-generated method stub
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/FloatWindow"
        private var mInstance: FloatWindow? = null
        fun newInstance(context: Context?, uri: Uri?, title: String?, progress: Int, duration: Long): FloatWindow? {
            if (mInstance == null) {
                mInstance = FloatWindow(context, uri, title, progress, duration)
            }
            return mInstance
        }
    }
    
    init {
        mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        Log.v(TAG, "video uri=" + mVideoUri + ", title=" + mVideoTitle + ", progress=" + mVideoProgress)
    }
}