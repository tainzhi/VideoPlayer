package com.qfq.tainzhi.videoplayer.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qfq.tainzhi.videoplayer.mvp.presenter.SplashPresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView;
import com.tainzhi.android.videoplayer.ui.MainActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
@RuntimePermissions
public class SplashActivity extends AppCompatActivity implements ISplashActivityView {
	private ISplashPresenter mSplashPresenter;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSplashPresenter = new SplashPresenter(this);
		mSplashPresenter.setDelay();
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// NOTE: delegate the permission handling to generated method
		SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void enterApp() {
		SplashActivityPermissionsDispatcher.startActivityWithPermissionCheck(this);
	}
	
	@NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE})
	public void startActivity() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}
