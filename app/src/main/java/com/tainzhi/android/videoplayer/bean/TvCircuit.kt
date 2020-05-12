package com.tainzhi.android.videoplayer.bean

import androidx.room.*
import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 14:29
 * @description: 卫视频道源
 **/
@Entity(
        tableName = "tv_circuit",
        foreignKeys = [
            ForeignKey(entity = Tv::class, parentColumns = ["name"], childColumns = ["tv_name"])
        ],
        indices = [Index("tv_name")]
)
data class TvCircuit(
    @ColumnInfo(name = "tv_name") var tvName: String?,
    var circuit: String?
): Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var tvCircuitId: Long = 0
}