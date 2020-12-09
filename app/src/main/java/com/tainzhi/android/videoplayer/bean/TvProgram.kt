package com.tainzhi.android.videoplayer.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * File:     TvProgram
 * Author:   tainzhi
 * Created:  2020/12/9 17:32
 * Mail:     QFQ61@qq.com
 * Description:
 */
@Entity(
        tableName = "tv_program",
        // foreignKeys = [
        //     ForeignKey(entity = Tv::class, parentColumns = ["id"], childColumns = ["tv_id"])
        // ],
        indices = [Index("tv_id")]
)
data class TvProgram(
        @ColumnInfo(name = "tv_id") @Json(name = "tvId") val tvId: String, // 卫视id
        var liveProgram: String,
        var liveProgramTime: String,
        var nextProgram: String,
        var nextProgramTime: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tvProgramId: Long = 0
}
