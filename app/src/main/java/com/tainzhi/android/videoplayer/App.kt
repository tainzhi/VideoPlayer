package com.tainzhi.android.videoplayer

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tainzhi.android.videoplayer.db.AppDataBase
import com.tainzhi.android.videoplayer.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 10:09
 * @description:
 **/

class App : Application() {


    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModule)
        }
        
        AppDataBase.getInstance(App.CONTEXT)

        Logger.addLogAdapter(AndroidLogAdapter())
    }

}