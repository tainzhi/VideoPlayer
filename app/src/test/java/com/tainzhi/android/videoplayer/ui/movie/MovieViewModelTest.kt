package com.tainzhi.android.videoplayer.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.tainzhi.android.videoplayer.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.*
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.*


/**
 * File:     MovieViewModelTest
 * Author:   tainzhi
 * Created:  2021/1/13 17:11
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MovieViewModelTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScope = MainCoroutineScopeRule()

    @Test
    fun getHomeData() {
        mainCoroutineScope.runBlockingTest {
            val movieViewModel: MovieViewModel = get()
            movieViewModel.getHomeData()
            movieViewModel.classifyListLiveData
                    .test()
                    .assertHasValue()
        }
    }

    @Test
    fun getChannelData() {
    }

    @Test
    fun getMovieDetail() {
    }

    @Test
    fun changeMovieSource() {
    }

    @Test
    fun searchMovie() {
    }
}