package com.tainzhi.android.videoplayer.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tainzhi.android.videoplayer.widget.showCheckPermissionDialog

class SplashActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_REQUIRED_PERMISSIONS = 42
    }

    private val requestPermissions = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var isMissingPermissions = true
    private var showRationale = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        if (!isMissingPermissions) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    /**
     * 检查是否缺少权限
     */
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            isMissingPermissions = false
        } else {
            requestMissingRequiredPermissions()
        }
    }

    private fun requestMissingRequiredPermissions() {
        val missing = HashSet<String>()
        showRationale = false
        for (permission in requestPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                missing.add(permission)
                showRationale = showRationale or (shouldShowRequestPermissionRationale(permission))
            }
        }
        if (missing.isNotEmpty()) {
            requestPermissions(missing.toTypedArray(), REQUEST_REQUIRED_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_REQUIRED_PERMISSIONS) {
            var granted = true
            var _showRationale = false

            for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    _showRationale = _showRationale || shouldShowRequestPermissionRationale(permissions[i])

                }
            }
            if (granted) {
                navigateToMain()
            } else {
                // If we were not supposed to show the rationale before requestPermissions(...) and
                // we still shouldn't show the rationale it means the user previously selected
                // "don't ask again" in the permission request dialog. In this case we bring up the
                // system permission settings for this package.
                if (!_showRationale && !showRationale) {
                    showCheckPermissionDialog(supportFragmentManager)
                } else {
                    finish()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
