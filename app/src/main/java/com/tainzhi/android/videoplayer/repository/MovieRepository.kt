package com.tainzhi.android.videoplayer.repository

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

    // fun getHomeData() = flow<Result<List<Classify>>>{
    //     movieManager.curUseSourceConfig().requestHomeData { homeData ->
    //             emit(Result.success(homeData.classifyList))
    //         }
    //     }
    // }
}
