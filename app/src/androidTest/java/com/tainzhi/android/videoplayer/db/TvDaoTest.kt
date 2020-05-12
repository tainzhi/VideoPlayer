package com.tainzhi.android.videoplayer.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import com.tainzhi.android.videoplayer.utilities.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 18:00
 * @description:
 **/

@RunWith(AndroidJUnit4::class)
class TvDaoTest {
    private lateinit var database: AppDataBase
    private lateinit var tvDao: TvDao
    private val tvList = arrayListOf<Tv>(
            Tv("CCTV1", "center", "CCTV1", "", "", ""),
            Tv("CCTV2", "center", "CCTV2", "", "", ""),
            Tv("CCTV3", "center", "CCTV3", "", "", ""))
    private val tvCircuitList = arrayListOf<TvCircuit>(
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV2", "https://www.baidu.com")
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
        tvDao = database.getTvDao()

        tvDao.insertAllTv(tvList)
        tvDao.insertAllTvCircuit(tvCircuitList)
    }


    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetTv() {
        val tvList = getValue(tvDao.getAllTv())
        assertThat(tvList.size, equalTo(3))

        val tvCircuitList = getValue(tvDao.getTvCircuit("CCTV1"))
        assertThat(tvCircuitList.size, equalTo(3))
    }
}