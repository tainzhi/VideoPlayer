package com.tainzhi.android.videoplayer.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tainzhi.mediaspider.bean.TvProgramBean
import java.io.Serializable

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 10:34
 * @description: 卫视频道
 **/
@Entity(
        tableName = "tv",
        indices = [Index("id")]
)
data class Tv(
        @PrimaryKey @ColumnInfo(name = "id") @field:SerializedName("tvId") var id: String = "",
        @ColumnInfo(name = "type") @field:SerializedName("tvType") var type: String?,
        @ColumnInfo(name = "name") @field: SerializedName("tvName") var name: String?,
        @ColumnInfo(name = "image") @field: SerializedName("tvImg") var image: String?,
        @ColumnInfo(name = "program_url") @field: SerializedName("programUrl") var programUrl : String? = "",
        @ColumnInfo(name = "introduce") @field: SerializedName("introduce") var introduce: String? = ""
): Serializable {
    @Ignore
    @field: SerializedName("tvCircuit")
    var tvCircuit: List<String>? = null
    @Ignore
    var program: TvProgramBean? = null
    // constructor(): this("", "", "", "", "", "", null)
    // constructor(id: String, type: String, name: String, image: String, programUrl: String, introduce: String): this(id, type, name, image, programUrl, introduce, null)

}