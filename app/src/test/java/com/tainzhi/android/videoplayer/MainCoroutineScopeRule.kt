package com.tainzhi.android.videoplayer

import android.app.Application
import android.content.Context
import com.tainzhi.android.videoplayer.util.CoroutineDispatcherProvider
import com.tainzhi.android.videoplayer.di.databaseModule
import com.tainzhi.android.videoplayer.di.repositoryModule
import com.tainzhi.android.videoplayer.di.viewModelModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.*
import org.junit.runner.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.*

/**
 * File:     MainCoroutineScopeRule
 * Author:   tainzhi
 * Created:  2021/1/12 20:07
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MainCoroutineScopeRule constructor(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
        TestWatcher(),
        TestCoroutineScope by TestCoroutineScope(dispatcher) {

    @Mock
    private lateinit var mockContext: Context

    override fun starting(description: Description?) {
        super.starting(description)
        // If your codebase allows the injection of other dispatchers like
        // Dispatchers.Default and Dispatchers.IO, consider injecting all of them here
        // and renaming this class to `CoroutineScopeRule`
        //
        // All injected dispatchers in a test should point to a single instance of
        // TestCoroutineDispatcher.
        Dispatchers.setMain(dispatcher)

        mockContext = Application()

        // 替换掉默认的 Dispatchers.default, 它不能用于测试
        val testCoroutineModule = module {
            single {
                CoroutineDispatcherProvider(
                        dispatcher, dispatcher, dispatcher
                )
            }
        }
        startKoin {
            androidContext(mockContext)
            modules(listOf(
                    viewModelModule, repositoryModule, databaseModule,
                    testCoroutineModule
            ))
        }
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()

        stopKoin()
    }
}