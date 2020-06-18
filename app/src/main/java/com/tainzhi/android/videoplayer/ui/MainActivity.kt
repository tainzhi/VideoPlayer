package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.qfq.tainzhi.videoplayer.R
import com.tainzhi.android.common.base.ui.NavigationHost
import com.tainzhi.android.videoplayer.util.setupWithNavController

class MainActivity : AppCompatActivity() , NavigationHost {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            // setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.localVideoFragment,
            R.id.douyuFragment,
            R.id.TVFragment,
            R.id.movieFragment,
            R.id.likeFragment
    )

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        // val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        currentNavController?.value?.let { toolbar.setupWithNavController(it) }
    }
}
