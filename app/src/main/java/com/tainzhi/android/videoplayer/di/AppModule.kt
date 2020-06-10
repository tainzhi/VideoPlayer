package com.tainzhi.android.videoplayer.di

import android.app.Application
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.db.AppDataBase
import com.tainzhi.android.videoplayer.db.TvDao
import com.tainzhi.android.videoplayer.repository.TVRepository
import com.tainzhi.android.videoplayer.ui.TVViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 14:16
 * @description: koin所需的module
 **/
 
val viewModelModule = module {
    viewModel { TVViewModel(get(), get())}
}

val repositoryModule = module {
    single { TVRepository(get()) }
}

// 单独抽出CoroutineModule, 方便test
val coroutineModule = module {
    single { CoroutinesDispatcherProvider() }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDataBase {
        return AppDataBase.getInstance(application)
    }

    fun provideTVDao(database: AppDataBase): TvDao {
        return database.getTvDao()
    }

    single { provideDatabase(androidApplication())}
    single { provideTVDao( get() )}
}

val appModule = listOf(viewModelModule, repositoryModule, databaseModule, coroutineModule)