package com.tainzhi.android.videoplayer.repository

import com.orhanobut.logger.Logger
import com.tainzhi.android.common.base.ResponseBody
import com.tainzhi.android.videoplayer.bean.DouyuGame
import com.tainzhi.android.videoplayer.bean.DouyuRoom
import com.tainzhi.android.videoplayer.network.Result
import com.tainzhi.android.videoplayer.network.VideoService
import com.tainzhi.mediaspider.spider.DouyuSpider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.get

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/6/15 19:01
 * @description:
 **/

class DouyuRepository {

    private var douyuCurrentOffset = 0
    private var douyuLimit = 20 // 因为第一页加载20,方便形成首行横跨两格; 从第二页开始加载20个

    /**
     * @param gameId 直播游戏id
     * @param isRefresh 是否重新加载, true 刷新, 只加载第一个page; false, 加载下一个page
     */
    fun getGameRooms(gameId: String, isRefresh: Boolean = false): Flow<Result<List<DouyuRoom>>> =
            flow {
                emit(Result.loading())

                if (isRefresh) {
                    douyuCurrentOffset = 0
                }

                lateinit var fetchedData: ResponseBody<List<DouyuRoom>>
                // 推荐房间列表
                if (gameId == "-1")
                    fetchedData = get(VideoService::class.java).getRecommendRooms(douyuCurrentOffset, douyuLimit)
                // 特定game房间列表
                else
                    fetchedData = get(VideoService::class.java).getGameRooms(gameId, douyuCurrentOffset, douyuLimit)

                val fetchedDataSize = fetchedData.data.size
                if (fetchedDataSize < douyuLimit) {
                    emit(Result.SuccessEndData(fetchedData.data))
                } else {
                    emit(Result.Success(fetchedData.data))
                }
                douyuCurrentOffset += fetchedDataSize
            }.catch { e ->
                Logger.e(e.toString())
                emit(Result.error(e.toString()))
            }

    fun getAllGames(): Flow<Result<List<DouyuGame>>> =
            flow {
                emit(Result.loading())

                val fetchedData = get(VideoService::class.java).getAllGames()

                emit(Result.Success(fetchedData.data))
            }.catch { e ->
                Logger.e(e.toString())
                emit(Result.error(e.toString()))
            }

    fun getRoomUrl(roomId: String): Flow<Result<String>> =
            flow {
                emit(Result.loading())
                val roomCircuitId = DouyuSpider.getInstance().getRoomCircuitId(roomId)
                val url = String.format(circuit1, roomCircuitId)
                emit(Result.Success(url))
            }.catch { e ->
                Logger.e(e.toString())
                emit(Result.error(e.toString()))
            }

    companion object {
        val circuit1 = "http://tx2play1.douyucdn.cn/live/%s_550.flv"
        val circuit2 = "http://hdls1a.douyucdn.cn/live/%s.flv"
        val circuit5 = "https://tc-tct.douyucdn2.cn/dyliveflv1a/%s_550.flv"

    }
}