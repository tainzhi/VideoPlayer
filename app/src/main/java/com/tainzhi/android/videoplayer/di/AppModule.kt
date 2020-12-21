package com.tainzhi.android.videoplayer.di

import android.app.Application
import com.tainzhi.android.common.CoroutinesDispatcherProvider
import com.tainzhi.android.videoplayer.db.AppDataBase
import com.tainzhi.android.videoplayer.db.TvDao
import com.tainzhi.android.videoplayer.network.VideoClient
import com.tainzhi.android.videoplayer.network.VideoService
import com.tainzhi.android.videoplayer.repository.DouyuRepository
import com.tainzhi.android.videoplayer.repository.LocalVideoRepository
import com.tainzhi.android.videoplayer.repository.MovieRepository
import com.tainzhi.android.videoplayer.repository.PreferenceRepository
import com.tainzhi.android.videoplayer.repository.TVRepository
import com.tainzhi.android.videoplayer.ui.MainViewModel
import com.tainzhi.android.videoplayer.ui.douyu.DouyuCategoryViewModel
import com.tainzhi.android.videoplayer.ui.douyu.DouyuGameViewModel
import com.tainzhi.android.videoplayer.ui.like.LikeViewModel
import com.tainzhi.android.videoplayer.ui.local.LocalVideoViewModel
import com.tainzhi.android.videoplayer.ui.movie.MovieViewModel
import com.tainzhi.android.videoplayer.ui.play.PlayDouyuViewModel
import com.tainzhi.android.videoplayer.ui.tv.TVViewModel
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
    viewModel { TVViewModel(get(), get()) }
    viewModel { LocalVideoViewModel(get(), get()) }
    viewModel { DouyuGameViewModel(get(), get()) }
    viewModel { DouyuCategoryViewModel(get(), get()) }
    viewModel { MainViewModel() }
    viewModel { PlayDouyuViewModel(get(), get())}
    viewModel { LikeViewModel(get()) }
    viewModel { MovieViewModel(get(), get()) }
}

val repositoryModule = module {
    single { VideoClient.getService(VideoService::class.java, VideoService.DOUYU_BASE_URL) }
    single { TVRepository(get()) }
    single { LocalVideoRepository() }
    single { DouyuRepository() }
    single { PreferenceRepository(get()) }
    single { MovieRepository() }
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

    single { provideDatabase(androidApplication()) }
    single { provideTVDao(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule, databaseModule, coroutineModule)