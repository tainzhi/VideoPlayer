package com.tainzhi.android.videoplayer.ui.tv

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.tainzhi.android.videoplayer.MainCoroutineScopeRule
import com.tainzhi.android.videoplayer.network.ResultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.*
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.junit.*


/**
 * File:     TVViewModelTest
 * Author:   tainzhi
 * Created:  2021/1/13 17:04
 * Mail:     QFQ61@qq.com
 * Description:
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class TVViewModelTest : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScope = MainCoroutineScopeRule()

    @Test
    fun `test get tv programs`() {
        mainCoroutineScope.runBlockingTest {
            val tvViewModel: TVViewModel = get()
            tvViewModel.getTVProgram()
            tvViewModel.tvPrograms
                    .test()
                    .awaitValue()
                    .assertValue {
                        it is ResultOf.Loading
                    }
                    .awaitNextValue()
                    .assertValue {
                        it is ResultOf.Success
                    }
                    .assertValue {
                        (it as ResultOf.Success).data is Map
                    }

        }
    }

}
