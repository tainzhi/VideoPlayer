package com.qfq.tainzhi.videoplayer.ui.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.qfq.tainzhi.videoplayer.R
import com.qfq.tainzhi.videoplayer.R2
import com.qfq.tainzhi.videoplayer.ui.fragment.*
import com.qfq.tainzhi.videoplayer.util.SettingUtil

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    @kotlin.jvm.JvmField
    @BindView(R2.id.toolbar)
    var toolbar: Toolbar? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.container)
    var container: FrameLayout? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.bottom_navigation)
    var mBottomNavigationView: BottomNavigationView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.nav_view)
    var mNavigationView: NavigationView? = null
    
    @kotlin.jvm.JvmField
    @BindView(R2.id.drawer_layout)
    var mDrawerLayout: DrawerLayout? = null
    private val mTitles: IntArray? = intArrayOf(
            R.string.title_local_video,
            R.string.title_douyu,
            R.string.title_tv,
            R.string.title_ustv,
            R.string.title_like)
    private var mLocalVideoFragment: LocalVideoFragment? = null
    private var mDouyuFragment: DouyuFragment? = null
    private var mTVFragment: TVFragment? = null
    private var mUSTVFragment: USTVFragment? = null
    private var mLikeFragment: LikeFragment? = null
    private val mExitTime: Long = 0
    private var mFirstClickTime: Long = 0
    private var mPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initView()
        if (savedInstanceState != null) {
            mLocalVideoFragment = supportFragmentManager.findFragmentByTag(LocalVideoFragment::class.java.name) as LocalVideoFragment?
            mDouyuFragment = supportFragmentManager.findFragmentByTag(DouyuFragment::class.java.name) as DouyuFragment?
            mTVFragment = supportFragmentManager.findFragmentByTag(TVFragment::class.java.name) as TVFragment?
            mUSTVFragment = supportFragmentManager.findFragmentByTag(USTVFragment::class.java.name) as USTVFragment?
            mLikeFragment = supportFragmentManager.findFragmentByTag(LikeFragment::class.java.name) as LikeFragment?
            showFragment(savedInstanceState.getInt(POSITION))
            mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECTED_ITEM))
        } else {
            showFragment(FRAGMENT_LOCAL_VIDEO)
        }
        if (SettingUtil.Companion.newInstance().getIsFirstTime()) {
            showTapTarget()
        }
    }
    
    override fun onConfigurationChanged(newConfig: Configuration?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onConfigurationChanged(newConfig)
    }
    
    // TODO: 2019/6/10
    // @Override
    // protected void onSavedInstanceState(Bundle outState) {
    //     super.onSaveInstanceState(outState);
    //     outState.putInt(POSITION, mPosition);
    //     outState.putInt(SELECTED_ITEM, mBottomNavigationView.getSelectedItemId())
    // }
    private fun showTapTarget() {
        // TODO: 2019/6/10 添加引导界面
    }
    
    private fun initView() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(this)
        mBottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem? ->
            when (item.getItemId()) {
                R.id.action_local_video -> {
                    showFragment(FRAGMENT_LOCAL_VIDEO)
                    doubleClick(FRAGMENT_LOCAL_VIDEO)
                }
                R.id.action_douyu -> {
                    showFragment(FRAGMENT_DOUYU)
                    doubleClick(FRAGMENT_DOUYU)
                }
                R.id.action_tv -> {
                    showFragment(FRAGMENT_TV)
                    doubleClick(FRAGMENT_TV)
                }
                R.id.action_ustv -> {
                    showFragment(FRAGMENT_USTV)
                    doubleClick(FRAGMENT_USTV)
                }
                R.id.action_like -> {
                    showFragment(FRAGMENT_LIKE)
                    doubleClick(FRAGMENT_LIKE)
                }
            }
            true
        })
    }
    
    fun doubleClick(index: Int) {
        val secondClickTime = System.currentTimeMillis()
        if (secondClickTime - mFirstClickTime < 500) {
            when (index) {
                FRAGMENT_LOCAL_VIDEO -> mLocalVideoFragment.onDoubleClick()
                FRAGMENT_DOUYU -> mDouyuFragment.onDoubleClick()
                FRAGMENT_TV -> mTVFragment.onDoubleClick()
                FRAGMENT_USTV -> mUSTVFragment.onDoubleClick()
                FRAGMENT_LIKE -> mLikeFragment.onDoubleClick()
            }
        } else {
            mFirstClickTime = secondClickTime
        }
    }
    
    private fun showFragment(index: Int) {
        val ft = supportFragmentManager.beginTransaction()
        hideFragment(ft)
        mPosition = index
        when (index) {
            FRAGMENT_LOCAL_VIDEO -> {
                toolbar.setTitle(R.string.title_local_video)
                if (mLocalVideoFragment == null) {
                    mLocalVideoFragment = LocalVideoFragment.Companion.newInstance()
                    ft.add(R.id.container, mLocalVideoFragment,
                           LocalVideoFragment::class.java.name)
                } else {
                    ft.show(mLocalVideoFragment)
                }
            }
            FRAGMENT_DOUYU -> {
                toolbar.setTitle(R.string.title_douyu)
                if (mDouyuFragment == null) {
                    mDouyuFragment = DouyuFragment.Companion.newInstance()
                    ft.add(R.id.container, mDouyuFragment,
                           DouyuFragment::class.java.name)
                } else {
                    ft.show(mDouyuFragment)
                }
            }
            FRAGMENT_TV -> {
                toolbar.setTitle(R.string.title_tv)
                if (mTVFragment == null) {
                    mTVFragment = TVFragment.Companion.newInstance()
                    ft.add(R.id.container, mTVFragment,
                           TVFragment::class.java.name)
                } else {
                    ft.show(mTVFragment)
                }
            }
            FRAGMENT_USTV -> {
                toolbar.setTitle(R.string.title_ustv)
                if (mUSTVFragment == null) {
                    mUSTVFragment = USTVFragment.Companion.newInstance()
                    ft.add(R.id.container, mUSTVFragment,
                           USTVFragment::class.java.name)
                } else {
                    ft.show(mUSTVFragment)
                }
            }
            FRAGMENT_LIKE -> {
                toolbar.setTitle(R.string.title_like)
                if (mLikeFragment == null) {
                    mLikeFragment = LikeFragment.Companion.newInstance()
                    ft.add(R.id.container, mLikeFragment,
                           LikeFragment::class.java.name)
                } else {
                    ft.show(mLikeFragment)
                }
            }
        }
        ft.commit()
    }
    
    private fun hideFragment(ft: FragmentTransaction?) {
        for (fragment in supportFragmentManager.fragments) {
            val tag = fragment.tag
            if (tag != null) {
                if (tag == DouyuChannelFragment.Companion.DOUYU_CHANNEL_TAG || tag == DouyuChannelFragment.Companion.DOUYU_CHANNEL_TO_LIVE) {
                    ft.remove(fragment)
                }
            }
        }
        if (mLocalVideoFragment != null) {
            ft.hide(mLocalVideoFragment)
        }
        if (mDouyuFragment != null) {
            ft.hide(mDouyuFragment)
        }
        if (mTVFragment != null) {
            ft.hide(mTVFragment)
        }
        if (mUSTVFragment != null) {
            ft.hide(mUSTVFragment)
        }
        if (mLikeFragment != null) {
            ft.hide(mLikeFragment)
        }
    }
    
    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
    
    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        // Handle navigation view item clicks here.
        val id = item.getItemId()
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_tools) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }
        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    
    companion object {
        const val FRAGMENT_LOCAL_VIDEO = 0
        const val FRAGMENT_DOUYU = 1
        const val FRAGMENT_TV = 2
        const val FRAGMENT_USTV = 3
        const val FRAGMENT_LIKE = 4
        private val POSITION: String? = "position"
        private val SELECTED_ITEM: String? = "bottomNavigationSelectItem"
    }
}