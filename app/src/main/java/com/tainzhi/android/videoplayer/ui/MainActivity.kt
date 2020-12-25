package com.tainzhi.android.videoplayer.ui

import android.app.Activity
import android.app.Application
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tainzhi.android.common.base.ui.BaseVmBindingActivity
import com.tainzhi.android.videoplayer.R
import com.tainzhi.android.videoplayer.callback.EmptyActivityLifecycleCallback
import com.tainzhi.android.videoplayer.databinding.ActivityMainBinding
import com.tainzhi.android.videoplayer.repository.PreferenceRepository
import com.tainzhi.android.videoplayer.util.Theme
import com.tainzhi.android.videoplayer.util.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseVmBindingActivity<MainViewModel, ActivityMainBinding>() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        initTheme()

        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        setSupportActionBar(mBinding.toolbar)
        lifecycleScope.launch {
            whenStarted { }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState)
        }
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
            this.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).run {
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

    private fun initTheme() {
        val preferenceRepository: PreferenceRepository by inject()
        preferenceRepository.theme.observe(this, Observer { t ->
            when (t) {
                Theme.MODE_GRAY -> setGrayTheme()
                Theme.MODE_NIGHT_YES, Theme.MODE_NIGHT_NO -> {
                    // 是置灰状态, 恢复原样不置灰
                    if (isGrayTheme) setGrayTheme()
                    delegate.localNightMode = t
                }
            }
        })
    }

    private var isGrayTheme = false

    private fun setGrayTheme() {
        (this.applicationContext as Application).registerActivityLifecycleCallbacks(object :
            EmptyActivityLifecycleCallback() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val cm = ColorMatrix().apply {
                    setSaturation(if (isGrayTheme) 1f else 0f)
                }
                val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(cm) }
                activity.window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
            }
        })
        // 使得能够 onActivityCreated
        delegate.localNightMode = Theme.MODE_NIGHT_NO
        // 在置灰和不置灰 切换
        isGrayTheme = !isGrayTheme
    }
}
