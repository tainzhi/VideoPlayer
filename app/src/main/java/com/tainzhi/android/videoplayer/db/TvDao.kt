package com.tainzhi.android.videoplayer.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/5/12 10:52
 * @description:
 **/
 
@Dao
abstract class TvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllTv(tvs: List<Tv>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllTvCircuit(tvs: List<TvCircuit>)

    @Query("SELECT id,type,name,introduce,image,program_url FROM tv")
    abstract fun getAllTv(): List<Tv>

    // 提供tvId, 查询该频道的所有网络源
    @Query("SELECT circuit FROM tv_circuit WHERE tv_id = :tvId")
    abstract fun getTvCircuit(tvId: String): List<String>

    // @Transaction
    // @Query("SELECT * FROM plants WHERE id IN (SELECT DISTINCT(plant_id) FROM garden_plantings)")
    // fun getPlantedGardens(): LiveData<List<PlantAndGardenPlantings>>

}