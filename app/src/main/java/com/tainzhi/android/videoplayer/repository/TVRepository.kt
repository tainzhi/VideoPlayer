package com.tainzhi.android.videoplayer.repository

import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.db.TvDao
import com.tainzhi.android.videoplayer.network.VideoService
import com.tainzhi.mediaspider.TVSpider
import com.tainzhi.mediaspider.bean.TvProgramBean

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 14:15
 * @description:
 **/

class TVRepository(
        private val tvDao: TvDao,
        private val apiService: VideoService
) {

    fun loadTVs(): List<Tv> {
        val tvs = tvDao.getAllTv()
        tvs.forEach { tv ->
            tv.tvCircuit = loadTVSource(tv.id)
        }
        return tvs
    }

    /**
     * 查询卫视tvId的所有直播源
     *
     * @program tvId 卫视tvId
     */
    private fun loadTVSource(tvId: String): List<String> {
        return tvDao.getTvCircuit(tvId)
    }

    fun loadTVProgram(): Map<String, TvProgramBean> {
        return TVSpider.getInstance().getTvProgram("cctv") +
                TVSpider.getInstance().getTvProgram("satellite")
    }
}