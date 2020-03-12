package com.qfq.tainzhi.videoplayer.DoubleDecode

import android.util.Log

/**
 * Created by muqing on 1/3/16.
 */
/*
 * Copyright 2013 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Movie player callback.
 *
 *
 * The goal here is to play back frames at the original rate.  This is done by introducing
 * a pause before the frame is submitted to the renderer.
 *
 *
 * This is not coordinated with VSYNC.  Since we can't control the display's refresh rate, and
 * the source material has time stamps that specify when each frame should be presented,
 * we will have to drop or repeat frames occasionally.
 *
 *
 * Thread restrictions are noted in the method descriptions.  The FrameCallback overrides should
 * only be called from the MoviePlayer.
 */
class SpeedControlCallback constructor() : MoviePlayer.FrameCallback {
    private var mPrevPresentUsec: Long = 0
    private var mPrevMonoUsec: Long = 0
    private var mFixedFrameDurationUsec: Long = 0
    private var mLoopReset: Boolean = false
    
    /**
     * Sets a fixed playback rate.  If set, this will ignore the presentation time stamp
     * in the video file.  Must be called before playback thread starts.
     */
    fun setFixedPlaybackRate(fps: Int) {
        mFixedFrameDurationUsec = ONE_MILLION / fps
    }
    
    // runs on decode thread
    public override fun preRender(presentationTimeUsec: Long) {
        // For the first frame, we grab the presentation time from the video
        // and the current monotonic clock time.  For subsequent frames, we
        // sleep for a bit to try to ensure that we're rendering frames at the
        // pace dictated by the video stream.
        //
        // If the frame rate is faster than vsync we should be dropping frames.  On
        // Android 4.4 this may not be happening.
        if (mPrevMonoUsec == 0L) {
            // Latch current values, then return immediately.
            mPrevMonoUsec = System.nanoTime() / 1000
            mPrevPresentUsec = presentationTimeUsec
        } else {
            // Compute the desired time delta between the previous frame and this frame.
            var frameDelta: Long
            if (mLoopReset) {
                // We don't get an indication of how long the last frame should appear
                // on-screen, so we just throw a reasonable value in.  We could probably
                // do better by using a previous frame duration or some sort of average;
                // for now we just use 30fps.
                mPrevPresentUsec = presentationTimeUsec - ONE_MILLION / 30
                mLoopReset = false
            }
            if (mFixedFrameDurationUsec != 0L) {
                // Caller requested a fixed frame rate.  Ignore PTS.
                frameDelta = mFixedFrameDurationUsec
            } else {
                frameDelta = presentationTimeUsec - mPrevPresentUsec
            }
            if (frameDelta < 0) {
                Log.w(TAG, "Weird, video times went backward")
                frameDelta = 0
            } else if (frameDelta == 0L) {
                // This suggests a possible bug in movie generation.
                Log.i(TAG, "Warning: current frame and previous frame had same timestamp")
            } else if (frameDelta > 10 * ONE_MILLION) {
                // Inter-frame times could be arbitrarily long.  For this player, we want
                // to alert the developer that their movie might have issues (maybe they
                // accidentally output timestamps in nsec rather than usec).
                Log.i(TAG, ("Inter-frame pause was " + (frameDelta / ONE_MILLION) +
                        "sec, capping at 5 sec"))
                frameDelta = 5 * ONE_MILLION
            }
            val desiredUsec: Long = mPrevMonoUsec + frameDelta // when we want to wake up
            var nowUsec: Long = System.nanoTime() / 1000
            while (nowUsec < (desiredUsec - 100) /*&& mState == RUNNING*/) {
                // Sleep until it's time to wake up.  To be responsive to "stop" commands
                // we're going to wake up every half a second even if the sleep is supposed
                // to be longer (which should be rare).  The alternative would be
                // to interrupt the thread, but that requires more work.
                //
                // The precision of the sleep call varies widely from one device to another;
                // we may wake early or late.  Different devices will have a minimum possible
                // sleep time. If we're within 100us of the target time, we'll probably
                // overshoot if we try to sleep, so just go ahead and continue on.
                var sleepTimeUsec: Long = desiredUsec - nowUsec
                if (sleepTimeUsec > 500000) {
                    sleepTimeUsec = 500000
                }
                try {
                    if (CHECK_SLEEP_TIME) {
                        val startNsec: Long = System.nanoTime()
                        Thread.sleep(sleepTimeUsec / 1000, (sleepTimeUsec % 1000) as Int * 1000)
                        val actualSleepNsec: Long = System.nanoTime() - startNsec
                        Log.d(TAG, ("sleep=" + sleepTimeUsec + " actual=" + (actualSleepNsec / 1000) +
                                " diff=" + (Math.abs(actualSleepNsec / 1000 - sleepTimeUsec)) +
                                " (usec)"))
                    } else {
                        Thread.sleep(sleepTimeUsec / 1000, (sleepTimeUsec % 1000) as Int * 1000)
                    }
                } catch (ie: InterruptedException) {
                }
                nowUsec = System.nanoTime() / 1000
            }
            
            // Advance times using calculated time values, not the post-sleep monotonic
            // clock time, to avoid drifting.
            mPrevMonoUsec += frameDelta
            mPrevPresentUsec += frameDelta
        }
    }
    
    // runs on decode thread
    public override fun postRender() {}
    public override fun loopReset() {
        mLoopReset = true
    }
    
    companion object {
        private val TAG: String? = "VideoPlayer/DoubleDecode/SpeedControlCallback"
        private val CHECK_SLEEP_TIME: Boolean = false
        private val ONE_MILLION: Long = 1000000L
    }
}