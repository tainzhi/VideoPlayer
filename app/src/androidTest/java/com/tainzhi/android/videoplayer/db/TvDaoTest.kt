package com.tainzhi.android.videoplayer.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import com.tainzhi.android.videoplayer.bean.TvProgram
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*

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
    private val tvList = listOf<Tv>(
            Tv("CCTV1", "center", "CCTV1", "", "", ""),
            Tv("CCTV2", "center", "CCTV2", "", "", ""),
            Tv("CCTV3", "center", "CCTV3", "", "", ""))
    private val tvCircuitList = listOf<TvCircuit>(
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV1", "https://www.baidu.com"),
            TvCircuit("CCTV2", "https://www.baidu.com")
    )
    private val tvProgramList = listOf(
            TvProgram("cctv", "program1", "12:01", "program2", "13:00"),
            TvProgram("cctv2", "program1", "12:01", "program2", "13:00"),
            TvProgram("cctv3", "program1", "12:01", "program2", "13:00"),
    )

    @Before
    fun createDb() = runBlocking {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDataBase::class.java).build()
        tvDao = database.getTvDao()
    }


    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun tvTableTest() {
        runBlocking {
            tvDao.insertAllTv(tvList)
            val tvs = tvDao.getAllTv()
            assertThat(tvs.size, `is`(3))
        }
    }

    @Test
    fun tvCircuitTableTest() = runBlocking {
        // tv_circuit用外键映射到tv,所以要要插入tv到tv_table
        tvDao.insertAllTv(tvList)
        tvDao.insertAllTvCircuit(tvCircuitList)
        val tvCircuits = tvDao.getTvCircuit("CCTV1")
        assertThat(tvCircuits.size, `is`(3))
    }

    @Test
    fun tvProgramTableTest() = runBlocking {
        tvDao.insertAllTvPrograms(tvProgramList)

        val tvPrograms = tvDao.getAllTvPrograms()
        assertThat(tvPrograms.size, `is`(3))
    }
}