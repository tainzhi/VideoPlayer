package com.qfq.tainzhi.videoplayer.mvp.model

import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannelRooms
import com.qfq.tainzhi.videoplayer.bean.GsonDouyuChannels
import io.reactivex.Flowable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
class DouyuModel constructor() {
    private val mDouyuService: DouyuService?
    
    open interface DouyuService {
        // http://open.douyucdn.cn/api/RoomApi/live?limit=20&offset=20
        @GET("live")
        open fun getRecommend(
                @Query("limit") limit: Int,
                @Query("offset") offset: Int): Flowable<GsonDouyuChannelRooms?>?
        
        // http://open.douyucdn.cn/api/RoomApi/live/3?limit=20&offset=20
        @GET("live/{channel_id}")
        open fun getChannelRoom(
                @Path("channel_id") channel_id: Int,
                @Query("limit") limit: Int,
                @Query("offset") offset: Int): Flowable<GsonDouyuChannelRooms?>?
        
        // http://open.douyucdn.cn/api/RoomApi/game
        @GET("game")
        open fun getChannelList(): Flowable<GsonDouyuChannels?>?
    }
    
    fun RoomListGet(
            channelId: Int, title: String?,
            offset: Int): Flowable<GsonDouyuChannelRooms?>? {
        if (channelId == -1) {
            return mDouyuService.getRecommend(DEFAULT_LIMIT, offset)
        } else {
            return mDouyuService.getChannelRoom(channelId, DEFAULT_LIMIT,
                                                offset)
        }
    }
    
    fun ChannelListGet(): Flowable<GsonDouyuChannels?>? {
        return mDouyuService.getChannelList()
    }
    
    companion object {
        private val DEFAULT_LIMIT: Int = 20
        private val DOUYU_API_BASE: String? = "http://open.douyucdn" +
                ".cn/api/RoomApi/"
    }
    
    init {
        val retrofit: Retrofit? = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DOUYU_API_BASE)
                .build() /**/
        mDouyuService = retrofit.create(DouyuService::class.java)
    }
}