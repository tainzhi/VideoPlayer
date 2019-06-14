package com.qfq.tainzhi.videoplayer.mvp.model;

import com.google.gson.Gson;
import com.qfq.tainzhi.videoplayer.bean.GsonChannelRooms;

import io.reactivex.Flowable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuModel {
    private final static int DEFAULT_LIMIT = 20;
    private final static String DOUYU_API_BASE = "http://open.douyucdn" +
                                                         ".cn/api/RoomApi/";
    private DouyuService mDouyuService;
    
    public DouyuModel() {
        Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .baseUrl(DOUYU_API_BASE)
                            .build();/**/
        mDouyuService = retrofit.create(DouyuService.class);
    }
    
    public interface DouyuService {
        // http://open.douyucdn.cn/api/RoomApi/live?limit=20&offset=20
        @GET("live")
        Flowable<GsonChannelRooms> getRecommend(@Query("limit") int limit,
                                                @Query(
                "offset") int offset);
        
        // http://open.douyucdn.cn/api/RoomApi/live/3?limit=20&offset=20
        @GET("live/{channel_id}")
        Flowable<GsonChannelRooms> getChannelRoom(@Path("channel_id") int channel_id,
                                        @Query("limit") int limit,
                                        @Query("offset") int offset);
        
        
    }
    
    public Flowable<GsonChannelRooms> RoomListGet(int channelId, String title,
                                          int offset) {
        if (channelId == -1) {
            return mDouyuService.getRecommend(DEFAULT_LIMIT, offset);
        } else {
            return mDouyuService.getChannelRoom(channelId, DEFAULT_LIMIT,
                    offset);
        }
    }
}
