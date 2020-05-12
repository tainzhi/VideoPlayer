package com.tainzhi.android.videoplayer.bean

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/5/12 15:04
 * @description:
 **/

data class TvAndTvCircuit (
    @Embedded val tv: Tv,

    @Relation( parentColumn = "name", entityColumn = "tv_name", entity = TvCircuit::class )
    val circuits: List<TvCircuit> = emptyList()
)
