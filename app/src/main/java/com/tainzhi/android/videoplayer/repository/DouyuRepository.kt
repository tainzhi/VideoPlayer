package com.tainzhi.android.videoplayer.repository

import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.common.base.Result
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.VideoClient

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 19:01
 * @description:
 **/

class DouyuRepository : BaseRepository(){

    suspend fun getGameRooms(gameId: String): Result<List<DouyuRoom>> {
        return safeApiCall(call = { requestRooms(gameId)})
    }

    private suspend fun requestRooms(gameId: String): Result<List<DouyuRoom>> =
        executeResponse(VideoClient.service.getGameRooms(gameId))

    suspend fun getAllGames(): Result<List<DouyuGame>> {
        return safeApiCall(call = { requestAllGames()})
    }

    private suspend fun requestAllGames(): Result<List<DouyuGame>> =
            executeResponse(VideoClient.service.getAllGames())
}