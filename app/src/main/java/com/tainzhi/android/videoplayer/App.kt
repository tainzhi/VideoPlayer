package com.tainzhi.android.videoplayer

import android.app.Application
import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tainzhi.android.videoplayer.di.appModule
import glimpse.core.Glimpse
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
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

        // 是否是 灰色主题
        var isGrayTheme = false
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(appModule)
        }

        initLog()

        Glimpse.init(this)
    }

    private fun initLog() {
        Logger.addLogAdapter(object : AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder()
                        .showThreadInfo(false)
                        .methodCount(2)
                        .methodOffset(7)
                        .build()
        ) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}

