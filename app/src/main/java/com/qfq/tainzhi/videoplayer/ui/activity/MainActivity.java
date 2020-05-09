package com.qfq.tainzhi.videoplayer.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.qfq.tainzhi.videoplayer.R;
import com.qfq.tainzhi.videoplayer.R2;
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuChannelFragment;
import com.qfq.tainzhi.videoplayer.ui.fragment.DouyuFragment;
import com.qfq.tainzhi.videoplayer.ui.fragment.LikeFragment;
import com.qfq.tainzhi.videoplayer.ui.fragment.LocalVideoFragment;
import com.qfq.tainzhi.videoplayer.ui.fragment.TVFragment;
import com.qfq.tainzhi.videoplayer.ui.fragment.USTVFragment;
import com.qfq.tainzhi.videoplayer.util.SettingUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	
	public static final int FRAGMENT_LOCAL_VIDEO = 0;
	public static final int FRAGMENT_DOUYU = 1;
	public static final int FRAGMENT_TV = 2;
	public static final int FRAGMENT_USTV = 3;
	public static final int FRAGMENT_LIKE = 4;
	private static final String POSITION = "position";
	private static final String SELECTED_ITEM = "bottomNavigationSelectItem";
	
	
	@BindView(R2.id.toolbar)
	Toolbar toolbar;
	@BindView(R2.id.container)
	FrameLayout container;
	@BindView(R2.id.bottom_navigation)
	BottomNavigationView mBottomNavigationView;
	@BindView(R2.id.nav_view)
	NavigationView mNavigationView;
	@BindView(R2.id.drawer_layout)
	DrawerLayout mDrawerLayout;
	private int[] mTitles = {
			R.string.title_local_video,
			R.string.title_douyu,
			R.string.title_tv,
			R.string.title_ustv,
			R.string.title_like};
	
	private LocalVideoFragment mLocalVideoFragment;
	private DouyuFragment mDouyuFragment;
	private TVFragment mTVFragment;
	private USTVFragment mUSTVFragment;
	private LikeFragment mLikeFragment;
	private long mExitTime = 0;
	private long mFirstClickTime = 0;
	private int mPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		initView();
		
		if (savedInstanceState != null) {
			mLocalVideoFragment =
					(LocalVideoFragment) getSupportFragmentManager().findFragmentByTag(LocalVideoFragment.class.getName());
			mDouyuFragment =
					(DouyuFragment) getSupportFragmentManager().findFragmentByTag(DouyuFragment.class.getName());
			mTVFragment =
					(TVFragment) getSupportFragmentManager().findFragmentByTag(TVFragment.class.getName());
			mUSTVFragment =
					(USTVFragment) getSupportFragmentManager().findFragmentByTag(USTVFragment.class.getName());
			mLikeFragment =
					(LikeFragment) getSupportFragmentManager().findFragmentByTag(LikeFragment.class.getName());
			showFragment(savedInstanceState.getInt(POSITION));
			mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(SELECTED_ITEM));
		} else {
			showFragment(FRAGMENT_LOCAL_VIDEO);
		}
		
		if (SettingUtil.newInstance().getIsFirstTime()) {
			showTapTarget();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onConfigurationChanged(newConfig);
	}
	
	// TODO: 2019/6/10
	// @Override
	// protected void onSavedInstanceState(Bundle outState) {
	//     super.onSaveInstanceState(outState);
	//     outState.putInt(POSITION, mPosition);
	//     outState.putInt(SELECTED_ITEM, mBottomNavigationView.getSelectedItemId())
	// }
	
	private void showTapTarget() {
		// TODO: 2019/6/10 添加引导界面
	}
	
	private void initView() {
		setSupportActionBar(toolbar);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(toggle);
		toggle.syncState();
		
		mNavigationView.setNavigationItemSelectedListener(this);
		mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
			switch (item.getItemId()) {
				case R.id.action_local_video:
					showFragment(FRAGMENT_LOCAL_VIDEO);
					doubleClick(FRAGMENT_LOCAL_VIDEO);
					break;
				case R.id.action_douyu:
					showFragment(FRAGMENT_DOUYU);
					doubleClick(FRAGMENT_DOUYU);
					break;
				case R.id.action_tv:
					showFragment(FRAGMENT_TV);
					doubleClick(FRAGMENT_TV);
					break;
				case R.id.action_ustv:
					showFragment(FRAGMENT_USTV);
					doubleClick(FRAGMENT_USTV);
					break;
				case R.id.action_like:
					showFragment(FRAGMENT_LIKE);
					doubleClick(FRAGMENT_LIKE);
					break;
			}
			return true;
		});
	}
	
	public void doubleClick(int index) {
		long secondClickTime = System.currentTimeMillis();
		if ((secondClickTime - mFirstClickTime < 500)) {
			switch (index) {
				case FRAGMENT_LOCAL_VIDEO:
					mLocalVideoFragment.onDoubleClick();
					break;
				case FRAGMENT_DOUYU:
					mDouyuFragment.onDoubleClick();
					break;
				case FRAGMENT_TV:
					mTVFragment.onDoubleClick();
					break;
				case FRAGMENT_USTV:
					mUSTVFragment.onDoubleClick();
					break;
				case FRAGMENT_LIKE:
					mLikeFragment.onDoubleClick();
					break;
			}
		} else {
			mFirstClickTime = secondClickTime;
		}
	}
	
	private void showFragment(int index) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		hideFragment(ft);
		mPosition = index;
		switch (index) {
			case FRAGMENT_LOCAL_VIDEO:
				toolbar.setTitle(R.string.title_local_video);
				if (mLocalVideoFragment == null) {
					mLocalVideoFragment = LocalVideoFragment.newInstance();
					ft.add(R.id.container, mLocalVideoFragment,
							LocalVideoFragment.class.getName());
				} else {
					ft.show(mLocalVideoFragment);
				}
				break;
			case FRAGMENT_DOUYU:
				toolbar.setTitle(R.string.title_douyu);
				if (mDouyuFragment == null) {
					mDouyuFragment = DouyuFragment.newInstance();
					ft.add(R.id.container, mDouyuFragment,
							DouyuFragment.class.getName());
				} else {
					ft.show(mDouyuFragment);
				}
				break;
			case FRAGMENT_TV:
				toolbar.setTitle(R.string.title_tv);
				if (mTVFragment == null) {
					mTVFragment = TVFragment.newInstance();
					ft.add(R.id.container, mTVFragment,
							TVFragment.class.getName());
				} else {
					ft.show(mTVFragment);
				}
				break;
			case FRAGMENT_USTV:
				toolbar.setTitle(R.string.title_ustv);
				if (mUSTVFragment == null) {
					mUSTVFragment = USTVFragment.newInstance();
					ft.add(R.id.container, mUSTVFragment,
							USTVFragment.class.getName());
				} else {
					ft.show(mUSTVFragment);
				}
				break;
			case FRAGMENT_LIKE:
				toolbar.setTitle(R.string.title_like);
				if (mLikeFragment == null) {
					mLikeFragment = LikeFragment.newInstance();
					ft.add(R.id.container, mLikeFragment,
							LikeFragment.class.getName());
				} else {
					ft.show(mLikeFragment);
				}
		}
		ft.commit();
	}
	
	private void hideFragment(FragmentTransaction ft) {
		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
			String tag = fragment.getTag();
			if (tag != null) {
				if (tag.equals(DouyuChannelFragment.DOUYU_CHANNEL_TAG) ||
						    tag.equals(DouyuChannelFragment.DOUYU_CHANNEL_TO_LIVE)) {
					ft.remove(fragment);
				}
			}
		}
		if (mLocalVideoFragment != null) {
			ft.hide(mLocalVideoFragment);
		}
		if (mDouyuFragment != null) {
			ft.hide(mDouyuFragment);
		}
		if (mTVFragment != null) {
			ft.hide(mTVFragment);
		}
		if (mUSTVFragment != null) {
			ft.hide(mUSTVFragment);
		}
		if (mLikeFragment != null) {
			ft.hide(mLikeFragment);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		
		if (id == R.id.nav_home) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {
		
		} else if (id == R.id.nav_slideshow) {
		
		} else if (id == R.id.nav_tools) {
		
		} else if (id == R.id.nav_share) {
		
		} else if (id == R.id.nav_send) {
		
		}
		
		mDrawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}
}
