package com.tainzhi.android.videoplayer.network

import com.tainzhi.android.videoplayer.base.ResponseBody
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 16:36
 * @description:
 **/

interface VideoService {

    companion object {
        const val DOUYU_BASE_URL = "http://open.douyucdn.cn/api/RoomApi/"
    }

    /**
     * 推荐的游戏房间
     * http://open.douyucdn.cn/api/RoomApi/live?offset=20&limit=20
     */
    @GET("live")
    suspend fun getRecommendRooms(
            @Query("offset") offset: Int,
            @Query("limit") limit: Int
    ): ResponseBody<List<DouyuRoom>>

    /**
     * 获取游戏分类game_id下的直播房间
     * http://open.douyucdn.cn/api/RoomApi/live/3?offset=20&limit=20
     */
    @GET("live/{game_id}")
    suspend fun getGameRooms(
            @Path("game_id") gameId: String,
            @Query("offset") offset: Int,
            @Query("limit") limit: Int
    ): ResponseBody<List<DouyuRoom>>


    /**
     * 获取所有的游戏分类
     * http://open.douyucdn.cn/api/RoomApi/game
     */
    @GET("game")
    suspend fun getAllGames(): ResponseBody<List<DouyuGame>>

}