package com.tainzhi.android.videoplayer.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.testing.TestListenableWorkerBuilder
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 14:07
 * @description:
 **/

@RunWith(JUnit4::class)
class TvDatabaseWorkerTest {
    private lateinit var context: Context
    private lateinit var workManager: WorkManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        workManager = WorkManager.getInstance(context)
    }

    @Test
    fun testTvDataWork() {
        // Get the ListenableWorker
        val worker = TestListenableWorkerBuilder<TvDatabaseWorker>(context).build()

        // Start the work synchronously
        val result = worker.startWork().get()

        assertThat(result, CoreMatchers.`is`(ListenableWorker.Result.success()))

    }
}