package com.tainzhi.android.videoplayer.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tainzhi.tainzhi.videoplayer.R
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.util.setupWithNavController
import com.tainzhi.tainzhi.videoplayer.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : BaseVmBindingActivity<MainViewModel, ActivityMainBinding>() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setSupportActionBar(mBinding.toolbar)
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
            mBinding.toolbar.setupWithNavController(navController)
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
        mViewModel.run {
            title.observe(this@MainActivity, Observer { title ->
                mBinding.toolbarCenterTitle.run {
                    text = title
                }
            })
            showCenterTitle.observe(this@MainActivity, Observer { show ->
                mBinding.toolbarCenterTitle.visibility = if (show) View.VISIBLE else View.GONE
            })
            showSearchView.observe(this@MainActivity, Observer { show ->
                searchMenu?.isVisible = show
                if (!show) {
                    mBinding.toolbar.collapseActionView()
                }
            })
        }

    }

    private var searchMenu: MenuItem? = null
    private var searchView: SearchView? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        searchMenu = menu?.findItem(R.id.search)?.apply {
            isVisible = true
        }
        searchView = ((searchMenu?.actionView) as SearchView).apply {
            // setSearchableInfo(searchManager.getSearchableInfo(gameName))
            maxWidth = Integer.MAX_VALUE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mViewModel.postSearchString(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    mViewModel.postSearchString(newText)
                    return false
                }
            })
            this.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(R.id.search_src_text).run {
                setTextColor(android.graphics.Color.WHITE)
                setHintTextColor(android.graphics.Color.WHITE)
                hint = "请输入视频名称"
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    // null 使得光标与字体同色
                    textCursorDrawable = null
                }
            }
            this.findViewById<android.widget.ImageView>(R.id.search_button).setImageResource(R.drawable.ic_search)
            this.findViewById<android.widget.ImageView>(R.id.search_close_btn).setImageResource(R.drawable.ic_close)
            // this.findViewById<ImageView>(R.id.search_mag_icon).setImageResource(R.drawable.ic_search)
            // 去掉下划线
            this.findViewById<View>(R.id.search_plate).setBackgroundColor(android.graphics.Color.TRANSPARENT)

        }
        return super.onCreateOptionsMenu(menu)
    }
}
