package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import com.tainzhi.android.videoplayer.base.ui.BaseViewBindingActivity
import com.tainzhi.android.videoplayer.util.hideNavigation
import com.tainzhi.android.videoplayer.util.startKtxActivity
import com.tainzhi.android.videoplayer.databinding.ActivitySplashBinding

class SplashActivity : BaseViewBindingActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.splashMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                startKtxActivity<MainActivity>()
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            hideNavigation()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.splashMotionLayout.startLayoutAnimation()
    }
}
