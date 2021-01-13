package com.tainzhi.android.videoplayer.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import com.tainzhi.android.videoplayer.bean.TvProgram

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/5/12 10:52
 * @description:
 **/
 
@Dao
abstract class TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllTv(tvs: List<Tv>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllTvCircuit(tvs: List<TvCircuit>)

    @Query("SELECT * FROM tv")
    abstract suspend fun getAllTv(): List<Tv>

    // 提供tvId, 查询该频道的所有网络源
    @Query("SELECT circuit FROM tv_circuit WHERE tv_id = :tvId")
    abstract suspend fun getTvCircuit(tvId: String): List<String>

    // @Transaction
    // @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings)")
    // fun getPlantedGardens(): LiveData<List<PlantAndGardenPlantings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllTvPrograms(programs: List<TvProgram>)

    @Query("SELECT * FROM tv_program")
    abstract suspend fun getAllTvPrograms(): List<TvProgram>

}