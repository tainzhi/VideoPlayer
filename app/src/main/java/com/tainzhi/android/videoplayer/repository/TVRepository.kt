package com.tainzhi.android.videoplayer.repository

import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.videoplayer.bean.Tv
import com.tainzhi.android.videoplayer.db.TvDao

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/10 14:15
 * @description:
 **/

class TVRepository(private val tvDao: TvDao): BaseRepository() {

    fun loadTvs(): List<Tv> {
        return tvDao.getAllTv()
    }
}