package com.tainzhi.android.videoplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.tainzhi.android.videoplayer.App
import com.tainzhi.android.videoplayer.bean.InputCategory
import com.tainzhi.android.videoplayer.bean.InputTv
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/5/12 10:33
 * @description:
 **/

const val DATABASE_NAME = "videoplayer.db"
const val TV_CIRCUIT_JSON_FILE = "tv_circuits.json"

@Database(entities = [Tv::class, TvCircuit::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getTvDao(): TvDao


    companion object {

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // FIXME: 2020/6/11 WorkManager为啥不能加载, 待解决后, 不使用Thread 
                            // val request = OneTimeWorkRequestBuilder<TvDatabaseWorker>().build()
                            // WorkManager.getInstance(context).enqueue(request)
                            Thread {
                                App.CONTEXT.assets.open(TV_CIRCUIT_JSON_FILE).use { inputStream ->
                                    JsonReader(inputStream.reader()).use { jsonReader ->
                                        val tvCategories = object : TypeToken<List<InputCategory>>() {}.type
                                        val tvList = ArrayList<Tv>()
                                        val tvCircuitList = ArrayList<TvCircuit>()
                                        val inputTvCategories: List<InputCategory> = Gson().fromJson(jsonReader, tvCategories)
                                        inputTvCategories.forEach { category ->
                                            category.tvLists.forEach { inputTv ->
                                                    // tvList.add(Tv(tv.id, tv.type, tv.name, tv.image, tv.programUrl))
                                                    tvList.add(Tv(inputTv.tvId, category.type, inputTv.tvName, inputTv.tvImg, inputTv.programUrl, inputTv.introduce))
                                                    inputTv.tvCircuit?.forEach { circuit ->
                                                        tvCircuitList.add(TvCircuit(inputTv.tvId, circuit))
                                                }
                                            }
                                        }

                                        instance!!.getTvDao().run {
                                            insertAllTv(tvList)
                                            insertAllTvCircuit(tvCircuitList)
                                        }
                                    }

                                }
                            }.start()
                        }
                    })
                    .build()

        }

    }

}