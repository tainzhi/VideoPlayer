package com.qfq.tainzhi.videoplayer

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Created by muqing on 2019/6/10.
 * Email: qfq61@qq.com
 */
class App constructor() : MultiDexApplication() {
    public override fun onCreate() {
        super.onCreate()
        AppContext = getApplicationContext()
        initTheme()
        
        // TODO: 2019/6/10 add leakcnary
        // if (BuildConfig.DEBUG) {
        //     SdkManager.initStetho(this);
        //     SdkManager.initLeakCanary(this);
        // }
        Logger.addLogAdapter(AndroidLogAdapter())
    }
    
    private fun initTheme() {
        // TODO: 2019/6/10 add theme change
    }
    
    companion object {
        var AppContext: Context? = null
    }
}