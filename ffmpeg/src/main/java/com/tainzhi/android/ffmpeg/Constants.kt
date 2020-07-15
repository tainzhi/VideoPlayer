package com.tainzhi.android.ffmpeg

/**
 * @author:      tainzhi
 * @mail:        qfq61@qq.com
 * @date:        2020/7/15 14:40
 * @description:
 **/

object Constants {

    object PlayerState {
        /**
         * 播放错误码
         */
        //打不开媒体数据源
        // #define FFMPEG_CAN_NOT_OPEN_URL (ERROR_CODE_FFMPEG_PREPARE - 1)
        const val FFMPEG_CAN_NOT_OPEN_URL = -1

        //找不到媒体流信息
        // #define FFMPEG_CAN_NOT_FIND_STREAMS (ERROR_CODE_FFMPEG_PREPARE - 2)
        const val FFMPEG_CAN_NOT_FIND_STREAMS = -2

        //找不到解码器
        // #define FFMPEG_FIND_DECODER_FAIL (ERROR_CODE_FFMPEG_PREPARE - 3)
        const val FFMPEG_FIND_DECODER_FAIL = -3

        //无法根据解码器创建上下文
        // #define FFMPEG_ALLOC_CODEC_CONTEXT_FAIL (ERROR_CODE_FFMPEG_PREPARE - 4)
        const val FFMPEG_ALLOC_CODEC_CONTEXT_FAIL = -4

        //根据流信息 配置上下文参数失败
        // #define FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL (ERROR_CODE_FFMPEG_PREPARE - 5)
        const val FFMPEG_CODEC_CONTEXT_PARAMETERS_FAIL = -5

        //打开解码器失败
        // #define FFMPEG_OPEN_DECODER_FAIL (ERROR_CODE_FFMPEG_PREPARE - 6)
        const val FFMPEG_OPEN_DECODER_FAIL = -6

        //没有音视频
        // #define FFMPEG_NOMEDIA (ERROR_CODE_FFMPEG_PREPARE - 7)
        const val FFMPEG_NOMEDIA = -7

        //读取媒体数据包失败
        // #define FFMPEG_READ_PACKETS_FAIL (ERROR_CODE_FFMPEG_PLAY - 8)
        const val FFMPEG_READ_PACKETS_FAIL = -8

        //rtmp 初始化失败
        const val RTMP_INIT_ERROR = -9

        //设置 rtmp url 失败
        const val RTMP_SET_URL_ERROR = -10

        //连接服务器失败
        const val RTMP_CONNECT_ERROR = -11
        const val FAAC_ENC_OPEN_ERROR = -12
        const val RTMP_PUSHER_ERROR = -13
    }
}
