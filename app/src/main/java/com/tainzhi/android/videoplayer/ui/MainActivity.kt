package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.databinding.ActivityMainBinding
import com.tainzhi.android.common.base.ui.BaseVMActivity
import com.tainzhi.android.videoplayer.util.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseVMActivity<MainViewModel>(useBinding = true) {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
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

        val navGraphIds = listOf(R.navigation.local, R.navigation.douyu, R.navigation.tv, R.navigation.movie, R.navigation.like)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            val toolbar = (mBinding as ActivityMainBinding).toolbar
            toolbar.setupWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun initVM(): MainViewModel = getViewModel()

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {
    }

    override fun initData() {
    }

    override fun startObserve() {
        val toolbar = (mBinding as ActivityMainBinding).toolbar
        mViewModel.run {
            title.observe(this@MainActivity, Observer { title ->
                toolbar.title = title
                (mBinding as ActivityMainBinding).toolbarCenterTitle.run {
                    text = title
                    visibility = View.VISIBLE
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}
