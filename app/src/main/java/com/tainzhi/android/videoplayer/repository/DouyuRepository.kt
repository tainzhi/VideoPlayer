package com.tainzhi.android.videoplayer.repository

import com.tainzhi.android.common.base.BaseRepository
import com.tainzhi.android.common.base.Result
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.VideoService
import com.tainzhi.mediaspider.DouyuSpider
import org.koin.java.KoinJavaComponent.get

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 19:01
 * @description:
 **/

// TODO: 2020/12/9 用NetworkBoundRepository替换 
class DouyuRepository : BaseRepository(){

    /**
     * @param offset 已经加载offset个房间信息, 从offset加载之后的房间
     * @param limit 加载数量限制, 默认为20
     */
    suspend fun getGameRooms(gameId: String, offset: Int, limit: Int = 20): Result<List<DouyuRoom>> {
        return safeApiCall(call = { requestRooms(gameId, offset, limit)})
    }

    private suspend fun requestRooms(gameId: String, offset: Int, limit: Int): Result<List<DouyuRoom>> =
        executeResponse(
                // 推荐房间列表
                if (gameId == "-1")
                    get(VideoService::class.java).getRecommendRooms(offset, limit)
                // 特定game房间列表
                else
                    get(VideoService::class.java).getGameRooms(gameId, offset, limit)
        )

    suspend fun getAllGames(): Result<List<DouyuGame>> {
        return safeApiCall(call = { requestAllGames()})
    }

    private suspend fun requestAllGames(): Result<List<DouyuGame>> =
            executeResponse(get(VideoService::class.java).getAllGames())

    suspend fun getRoomCircuitId(roomId: String): Result<String> {
        return try {
            val result = DouyuSpider.getInstance().getRoomCircuitId(roomId)
            Result.Success<String>(result)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}