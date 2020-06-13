package com.tainzhi.android.videoplayer.repository

import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.db.TvDao
import com.tanzhi.mediaspider.TVSpider

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 14:15
 * @description:
 **/

class TVRepository(private val tvDao: TvDao): BaseRepository() {

    fun loadTVs(): List<Tv> {
        return tvDao.getAllTv()
    }

    /**
     * 查询卫视tvId的所有直播源
     *
     * @program tvId 卫视tvId
     */
    fun loadTVSource(tvId: String): List<String> {
        return tvDao.getTvCircuit(tvId)
    }

    fun loadTVProgram(): Map<String, String> {
        return TVSpider.getInstance().getTvProgram("cctv") +
                TVSpider.getInstance().getTvProgram("satellite")
    }
}