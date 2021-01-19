package com.tainzhi.android.videoplayer.ui

import android.app.Activity
import android.app.Application
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.callback.EmptyActivityLifecycleCallback
import com.tainzhi.android.videoplayer.databinding.ActivityMainBinding
import com.tainzhi.android.videoplayer.repository.PreferenceRepository
import com.tainzhi.android.videoplayer.util.Theme
import com.tainzhi.android.videoplayer.util.setupWithNavController
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

@Suppress("DUPLICATE_LABEL_IN_WHEN")
class MainActivity : BaseVmBindingActivity<MainViewModel, ActivityMainBinding>() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        initTheme()

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setSupportActionBar(mBinding.toolbar)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.local, R.navigation.douyu, R.navigation.tv, R.navigation.movie, R.navigation.me)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this) { navController ->
            mBinding.toolbar.setupWithNavController(navController)
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun initVM(): MainViewModel = getViewModel()

    override fun getLayoutResId() = R.layout.activity_main

    override fun startObserve() {
        mViewModel.run {
            title.observe(this@MainActivity) { title ->
                mBinding.toolbarCenterTitle.run {
                    text = title
                }
            }
            showCenterTitle.observe(this@MainActivity) { show ->
                mBinding.toolbarCenterTitle.visibility = if (show) View.VISIBLE else View.GONE
            }


        }
    }

    private fun initTheme() {
        val preferenceRepository: PreferenceRepository by inject()
        preferenceRepository.theme.observe(this) { t ->
            when (t) {
                Theme.MODE_GRAY -> {
                    setGrayTheme()
                }
                Theme.MODE_NIGHT_NO, Theme.MODE_NIGHT_YES -> {
                    unsetGrayTheme()
                    delegate.localNightMode = t
                }
            }
        }
    }

    private fun setGrayTheme() {
        if (!App.isGrayTheme) {
            (this.applicationContext as Application).registerActivityLifecycleCallbacks(
                    object : EmptyActivityLifecycleCallback() {
                        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                            val cm = ColorMatrix().apply {
                                // 0f 置灰, 1f 不置灰
                                setSaturation(0f)
                            }
                            val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }
                            activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
                        }
                    })

            App.isGrayTheme = true
            ActivityCompat.recreate(this)
        }
    }

    private fun unsetGrayTheme() {
        if (App.isGrayTheme) {
            (this.applicationContext as Application).registerActivityLifecycleCallbacks(
                    object : EmptyActivityLifecycleCallback() {
                        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                            val cm = ColorMatrix().apply {
                                // 0f 置灰, 1f 不置灰
                                setSaturation(1f)
                            }
                            val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }
                            activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
                        }
                    })
            App.isGrayTheme = false
            ActivityCompat.recreate(this)
        }
    }

}
