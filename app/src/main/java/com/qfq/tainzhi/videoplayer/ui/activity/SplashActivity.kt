package com.qfq.tainzhi.videoplayer.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qfq.tainzhi.videoplayer.mvp.presenter.SplashPresenter
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
@RuntimePermissions
class SplashActivity : AppCompatActivity(), ISplashActivityView {
    private var mSplashPresenter: ISplashPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashPresenter = SplashPresenter(this)
        mSplashPresenter.setDelay()
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
    }
    
    override fun onDestroy() {
        super.onDestroy()
    }
    
    override fun enterApp() {
        SplashActivityPermissionsDispatcher.startActivityWithPermissionCheck(this)
    }
    
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun startActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}