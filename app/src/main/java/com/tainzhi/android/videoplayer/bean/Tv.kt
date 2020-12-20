package com.tainzhi.android.videoplayer.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tainzhi.mediaspider.spider.TvProgramBean
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
        @PrimaryKey @ColumnInfo(name = "id") @Json(name = "tvId") var id: String = "",
        @ColumnInfo(name = "type") @Json(name = "tvType") var type: String?,
        @ColumnInfo(name = "name") @Json(name = "tvName") var name: String?,
        @ColumnInfo(name = "image") @Json(name = "tvImg") var image: String?,
        @ColumnInfo(name = "program_url") @Json(name = "programUrl") var programUrl: String? = "",
        @ColumnInfo(name = "introduce") @Json(name = "introduce") var introduce: String? = ""
) : Serializable {
    @Ignore
    @Json(name = "tvCircuit")
    var tvCircuit: List<String>? = null

    @Ignore
    var program: TvProgramBean? = null
    // constructor(): this("", "", "", "", "", "", null)
    // constructor(id: String, type: String, name: String, image: String, programUrl: String, introduce: String): this(id, type, name, image, programUrl, introduce, null)

}

/**
 * only used in read TV from tv_circuits.json
 */
@JsonClass(generateAdapter = true)
data class InputTv(
        val tvId: String,
        val tvType: String?,
        val tvName: String?,
        val tvImg: String?,
        val tvCircuit: List<String>?,
        val programUrl: String?,
        val introduce: String?
)
