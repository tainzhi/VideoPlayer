package com.tainzhi.android.videoplayer.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.bean.TvCircuit
import com.tainzhi.android.videoplayer.bean.TvProgram
import com.tainzhi.android.videoplayer.util.Start_UP_Create_Database

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/5/12 10:33
 * @description:
 **/

const val DATABASE_NAME = "videoplayer.db"
const val TV_CIRCUIT_JSON_FILE = "tv_circuits.json"

@Database(entities = [Tv::class, TvCircuit::class, TvProgram::class], version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getTvDao(): TvDao

    companion object {

        // TODO: 2020/12/9 把标记数据库创建成功的标记持久化
        var firstCreateDb = false

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
                        val request = OneTimeWorkRequestBuilder<TvDatabaseWorker>()
                            .addTag(
                                Start_UP_Create_Database
                            ).build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}