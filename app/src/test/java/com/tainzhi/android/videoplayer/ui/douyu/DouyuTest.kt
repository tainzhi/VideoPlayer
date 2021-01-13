package com.tainzhi.android.videoplayer.ui.douyu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.tainzhi.android.videoplayer.MainCoroutineScopeRule
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.*
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.*

/**
 * File:     DouyuTest
 * Author:   tainzhi
 * Created:  2021/1/13 16:34
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DouyuTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScope = MainCoroutineScopeRule()

    @Test
    fun `test douyu game viewmodel`() {
        mainCoroutineScope.runBlockingTest {
            val douyuViewModel: DouyuGameViewModel = get()
            douyuViewModel.getGameRooms("-1")
            douyuViewModel.gameRooms
                    .test()
                    .assertHasValue()
                    .awaitValue()
                    .assertValue {
                        it is ResultOf.Loading
                    }
                    .awaitNextValue()
                    .assertValue {
                        it is ResultOf.Success
                    }
                    .assertValue {
                        (it as ResultOf.Success).data is List<DouyuRoom>
                    }

        }
    }

    @Test
    fun `test douyu category viewmodel`() {
        mainCoroutineScope.runBlockingTest {
            val categoryViewModel: DouyuCategoryViewModel = get()
            categoryViewModel.getDouyuRooms()
            categoryViewModel.games
                    .test()
                    .awaitNextValue()
                    .assertValue {
                        (it as List).isNotEmpty()
                    }
        }
    }

}
