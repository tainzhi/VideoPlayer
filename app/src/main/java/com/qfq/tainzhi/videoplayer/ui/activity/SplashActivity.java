package com.qfq.tainzhi.videoplayer.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qfq.tainzhi.videoplayer.mvp.presenter.SplashPresenter;
import com.qfq.tainzhi.videoplayer.mvp.presenter.impl.ISplashPresenter;
import com.qfq.tainzhi.videoplayer.ui.activity.impl.ISplashActivityView;

/**
 * Created by muqing on 2019/6/9.
 * Email: qfq61@qq.com
 */
public class SplashActivity extends AppCompatActivity implements ISplashActivityView {
    private ISplashPresenter mSplashPresenter;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSplashPresenter = new SplashPresenter(this);
        mSplashPresenter.setDelay();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void enterApp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
