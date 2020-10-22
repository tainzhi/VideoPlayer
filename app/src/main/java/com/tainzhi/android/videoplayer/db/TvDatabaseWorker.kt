package com.tainzhi.android.videoplayer.db

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.tainzhi.android.videoplayer.bean.InputCategory
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 11:27
 * @description:
 **/

class TvDatabaseWorker(
        context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(TV_CIRCUIT_JSON_FILE).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val tvCategories = object : TypeToken<List<InputCategory>>() {}.type
                    val tvList = ArrayList<Tv>()
                    val tvCircuitList = ArrayList<TvCircuit>()
                    val inputTvCategories: List<InputCategory> = Gson().fromJson(jsonReader, tvCategories)
                    inputTvCategories.forEach { category ->
                        category.tvLists.forEach { inputTv ->
                            tvList.add(Tv(inputTv.tvId, category.type, inputTv.tvName, inputTv.tvImg, inputTv.programUrl, inputTv.introduce))
                            inputTv.tvCircuit?.forEach { circuit ->
                                tvCircuitList.add(TvCircuit(inputTv.tvId, circuit))
                            }
                        }
                    }

                    val database = AppDataBase.getInstance(applicationContext)
                    database.getTvDao().run {
                        insertAllTv(tvList)
                        insertAllTvCircuit(tvCircuitList)
                    }

                    Result.success()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending database", e)
            Result.failure()
        }
    }

    companion object {
        private val TAG = TvDatabaseWorker::class.java.simpleName
    }
}