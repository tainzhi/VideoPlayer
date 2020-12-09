package com.tainzhi.android.videoplayer.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 14:29
 * @description: 卫视频道直播源线路
 **/
@Entity(
        tableName = "tv_circuit",
        foreignKeys = [
            ForeignKey(entity = Tv::class, parentColumns = ["id"], childColumns = ["tv_id"])
        ],
        indices = [Index("tv_id")]
)
data class TvCircuit(
        @ColumnInfo(name = "tv_id") @Json(name = "tvId") var tvId: String?, // 卫视id
        var circuit: String? // 卫视直播线路
): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tvCircuitId: Long = 0
}