package com.tainzhi.android.videoplayer.repository

import com.orhanobut.logger.Logger
import com.tainzhi.android.videoplayer.App
import com.tainzhi.mediaspider.film.bean.MovieManager

/**
 * File:     MovieRepository
 * Author:   tainzhi
 * Created:  2020/12/21 07:55
 * Mail:     QFQ61@qq.com
 * Description:
 */
class MovieRepository {
    val movieManager = MovieManager.getInstance(App.CONTEXT)

    fun getMovieSourceList(): List<Pair<String, String>> {
        return if (movieManager.sourceConfigs != null) {
            return movieManager.sourceConfigs.keys.map {
                it to movieManager.sourceConfigs[it]!!.name
            }
        } else {
            val error = "source configs is null"
            Logger.e(error)
            throw Exception(error)
        }
    }
}
