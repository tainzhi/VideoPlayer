package com.qfq.tainzhi.videoplayer.util;

/**
 * Created by muqing on 2019/6/13.
 * Email: qfq61@qq.com
 */
public class DouyuApi {
    public static final String DOUYU_API = "http://open.douyucdn.cn/api/RoomApi/";
    
    public static String getRecommendUrl(int offset) {
        return DOUYU_API + "live?&limit=20&offset=" + offset;
    }
    
    public static String getChannelUrl(int channelId, int offset) {
        return DOUYU_API + "live/" + channelId + "?&limit=20&offset=" + offset;
    }
    
    public static String getAllChannelsUrl() {
        return DOUYU_API + "game";
    }
    
    public static String getRoom(int roomId) {
        return DOUYU_API + "room/" + roomId;
    }
    
    public static String getRoomUrl(int roomId) {
        return "https://m.douyu.com/html5/live?roomId=" + roomId;
    }
    
    // 类似的api示例：
    // 搜索wings
    // http://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=wings&offset=0&limit=0&client_sys=android
    public static String getSearchUrl(String keyWord) {
        return "http://capi.douyucdn.cn/api/v1/mobileSearch/1/1?sk=" + keyWord +
                       "&limit=10&client_sys=android";
    }
    
    public static String extractZhiboUrlPath(String originalPath) {
        // 参考：斗鱼直播源破解参考网址：https://www.52pojie.cn/thread-957638-1-1.html
        //
        // 假设原直播源地址为：http://hlsa.douyucdn.cn/live/431935rYIJ0kKhQ4_550/playlist.m3u8?wsSecret=924a83c6700d9d802a7717f1068811f6&wsTime=1558565155&token=h5-douyu-0-431935-373ef2a3e162f552b55145ccdd4571a3&did=h5_did
        // 直播源地址格式固定为 http://hlsa.douyucdn.cn/live/*_550/playlist/*
        // 替换hlsa为tx2play1, _550为普通清晰度，去除改为默认最高清晰度
        // 替换为: http://tx2play1.douyucdn.cn/live/431935rYIJ0kKhQ4.flv
        int first = originalPath.indexOf('.');
        int second = originalPath.indexOf('_');
        return "http://tx2play1" + originalPath.substring(first, second) + ".flv";
    }
}
