package com.qfq.tainzhi.videoplayer;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
public class App extends MultiDexApplication {
    public static Context AppContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = getApplicationContext();
    
        initTheme();
    
        // TODO: 2019/6/10 add leakcnary
        // if (BuildConfig.DEBUG) {
        //     SdkManager.initStetho(this);
        //     SdkManager.initLeakCanary(this);
        // }
        Logger.addLogAdapter(new AndroidLogAdapter());
    }
    
    private void initTheme() {
        // TODO: 2019/6/10 add theme change
    }
}


