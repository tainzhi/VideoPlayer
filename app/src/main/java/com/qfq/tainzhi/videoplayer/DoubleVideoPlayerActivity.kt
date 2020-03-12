package com.qfq.tainzhi.videoplayer

import android.app.Activity
import android.content.Intent
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import com.qfq.tainzhi.videoplayer.DoubleDecode.MoviePlayer
import com.qfq.tainzhi.videoplayer.DoubleDecode.SpeedControlCallback
import java.io.File
import java.io.IOException

/**
 * Created by muqing on 1/3/16.
 */
/**
 * Decodes two video streams simultaneously to two TextureViews.
 *
 *
 * One key feature is that the video decoders do not stop when the activity is restarted due
 * to an orientation change.  This is to simulate playback of a real-time video stream.  If
 * the Activity is pausing because it's "finished" (indicating that we're leaving the Activity
 * for a nontrivial amount of time), the video decoders are shut down.
 *
 *
 * TODO: consider shutting down when the screen is turned off, to preserve battery.
 */
class DoubleVideoPlayerActivity constructor() : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_double_moive_player)
        val intent: Intent? = getIntent()
        val file: String? = intent.getStringExtra("file")
        if (!sVideoRunning) {
            sBlob.get(0) = VideoBlob(findViewById<View?>(R.id.activity_double_movie_player_slid_1) as TextureView?,
                                     file, 0)
            sBlob.get(1) = VideoBlob(findViewById<View?>(R.id.activity_double_movie_player_slid_2) as TextureView?,
                                     file, 1)
            sVideoRunning = true
        } else {
            sBlob.get(0).recreateView(findViewById<View?>(R.id.activity_double_movie_player_slid_1) as TextureView?)
            sBlob.get(1).recreateView(findViewById<View?>(R.id.activity_double_movie_player_slid_2) as TextureView?)
        }
    }
    
    override fun onPause() {
        super.onPause()
        val finishing: Boolean = isFinishing()
        Log.d(TAG, "isFinishing: " + finishing)
        for (i in 0 until VIDEO_COUNT) {
            if (finishing) {
                sBlob.get(i).stopPlayback()
                sBlob.get(i) = null
            }
        }
        sVideoRunning = !finishing
        Log.d(TAG, "onPause complete")
    }
    
    /**
     * Video playback blob.
     *
     *
     * Encapsulates the video decoder and playback surface.
     *
     *
     * We want to avoid tearing down and recreating the video decoder on orientation changes,
     * because it can be expensive to do so.  That means keeping the decoder's output Surface
     * around, which means keeping the SurfaceTexture around.
     *
     *
     * It's possible that the orientation change will cause the UI thread's EGL context to be
     * torn down and recreated (the app framework docs don't seem to make any guarantees here),
     * so we need to detach the SurfaceTexture from EGL on destroy, and reattach it when
     * the new SurfaceTexture becomes available.  Happily, TextureView does this for us.
     */
    private class VideoBlob constructor(view: TextureView?, filePath: String?, ordinal: Int) : SurfaceTextureListener {
        private var mTextureView: TextureView? = null
        private val mFilePath: String?
        private var mSavedSurfaceTexture: SurfaceTexture? = null
        private var mPlayThread: PlayMovieThread? = null
        private val mCallback: SpeedControlCallback?
        
        /**
         * Performs partial construction.  The VideoBlob is already created, but the Activity
         * was recreated, so we need to update our view.
         */
        fun recreateView(view: TextureView?) {
            mTextureView = view
            mTextureView.setSurfaceTextureListener(this)
            if (mSavedSurfaceTexture != null) {
                view.setSurfaceTexture(mSavedSurfaceTexture)
            }
        }
        
        /**
         * Stop playback and shut everything down.
         */
        fun stopPlayback() {
            mPlayThread.requestStop()
            // TODO: wait for the playback thread to stop so we don't kill the Surface
            //       before the video stops
            
            // We don't need this any more, so null it out.  This also serves as a signal
            // to let onSurfaceTextureDestroyed() know that it can tell TextureView to
            // free the SurfaceTexture.
            mSavedSurfaceTexture = null
        }
        
        public override fun onSurfaceTextureAvailable(st: SurfaceTexture?, width: Int, height: Int) {
            
            // If this is our first time though, we're going to use the SurfaceTexture that
            // the TextureView provided.  If not, we're going to replace the current one with
            // the original.
            if (mSavedSurfaceTexture == null) {
                mSavedSurfaceTexture = st
                mPlayThread = PlayMovieThread(File(mFilePath), Surface(st), mCallback)
            } else {
                // Can't do it here in Android <= 4.4.  The TextureView doesn't add a
                // listener on the new SurfaceTexture, so it never sees any updates.
                // Needs to happen from activity onCreate() -- see recreateView().
                //Log.d(LTAG, "using saved st=" + mSavedSurfaceTexture);
                //mTextureView.setSurfaceTexture(mSavedSurfaceTexture);
            }
        }
        
        public override fun onSurfaceTextureSizeChanged(st: SurfaceTexture?, width: Int, height: Int) {}
        public override fun onSurfaceTextureDestroyed(st: SurfaceTexture?): Boolean {
            // The SurfaceTexture is already detached from the EGL context at this point, so
            // we don't need to do that.
            //
            // The saved SurfaceTexture will be null if we're shutting down, so we want to
            // return "true" in that case (indicating that TextureView can release the ST).
            return (mSavedSurfaceTexture == null)
        }
        
        public override fun onSurfaceTextureUpdated(st: SurfaceTexture?) {
            //Log.d(TAG, "onSurfaceTextureUpdated st=" + st);
        }
        
        /**
         * Constructs the VideoBlob.
         *
         * @param view     The TextureView object we want to draw into.
         * @param filePath Which movie file path.
         * @param ordinal  The blob's ordinal (only used for log messages).
         */
        init {
            mCallback = SpeedControlCallback()
            mFilePath = filePath
            recreateView(view)
        }
    }
    
    /**
     * Thread object that plays a movie from a file to a surface.
     *
     *
     * Currently loops until told to stop.
     */
    private class PlayMovieThread constructor(private val mFile: File?, private val mSurface: Surface?, private val mCallback: SpeedControlCallback?) : Thread() {
        private var mMoviePlayer: MoviePlayer? = null
        
        /**
         * Asks MoviePlayer to halt playback.  Returns without waiting for playback to halt.
         *
         *
         * Call from UI thread.
         */
        fun requestStop() {
            mMoviePlayer.requestStop()
        }
        
        public override fun run() {
            try {
                mMoviePlayer = MoviePlayer(mFile, mSurface, mCallback)
                mMoviePlayer.setLoopMode(true)
                mMoviePlayer.play()
            } catch (ioe: IOException) {
                Log.e(TAG, "movie playback failed", ioe)
            } finally {
                mSurface.release()
                Log.d(TAG, "PlayMovieThread stopping")
            }
        }
        
        /**
         * Creates thread and starts execution.
         *
         *
         * The object takes ownership of the Surface, and will access it from the new thread.
         * When playback completes, the Surface will be released.
         */
        init {
            start()
        }
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/DoubleVideoPlayerActivity"
        private val VIDEO_COUNT: Int = 2
        
        // Must be static storage so they'll survive Activity restart.
        private var sVideoRunning: Boolean = false
        private val sBlob: Array<VideoBlob?>? = arrayOfNulls<VideoBlob?>(VIDEO_COUNT)
    }
}