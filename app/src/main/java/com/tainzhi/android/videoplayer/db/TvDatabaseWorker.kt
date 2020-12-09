package com.tainzhi.android.videoplayer.db

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.tainzhi.android.videoplayer.bean.InputTv
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import kotlinx.coroutines.coroutineScope

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 11:27
 * @description:
 **/

class TvDatabaseWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            // 先从assets/tv_circuits.json中读取List<InputTV>
            // 再生成Tv和TvCircuit写到数据库, 每个Tv记录对应多个TvCircuit, 即一个频道有多个源
            val tvMoshiType = Types.newParameterizedType(List::class.java, InputTv::class.java)
            val moshiAdapter = Moshi.Builder().build().adapter<List<InputTv>>(tvMoshiType)
            applicationContext.assets.open(TV_CIRCUIT_JSON_FILE).use { inputStream ->
                val tvList = ArrayList<Tv>()
                val tvCircuitList = ArrayList<TvCircuit>()
                val readString = inputStream.bufferedReader().use { it.readText() }
                moshiAdapter.fromJson(readString)?.forEach { inputTv ->
                    tvList.add(Tv(inputTv.tvId, inputTv.tvType, inputTv.tvName, inputTv.tvImg, inputTv.programUrl, inputTv.introduce))
                    inputTv.tvCircuit?.forEach { circuit ->
                        tvCircuitList.add(TvCircuit(inputTv.tvId, circuit))
                    }
                }
                val database = AppDataBase.getInstance(applicationContext)
                database.getTvDao().run {
                    insertAllTv(tvList)
                    insertAllTvCircuit(tvCircuitList)
                }

                Result.success()
            }
        } catch (e: Exception) {
            val msg = "create database and load tv list from assets/circuits.json into db failed"
            Log.e(TAG, msg, e)
            Result.failure(Data.Builder().putString("create db error", msg).build())
        }
    }

    companion object {
        private val TAG = TvDatabaseWorker::class.java.simpleName
    }
}