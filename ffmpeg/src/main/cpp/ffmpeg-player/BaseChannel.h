//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_BASECHANNEL_H
#define VIDEOPLAYER_BASECHANNEL_H


extern "C" {
#include "libavcodec/avcodec.h"
#include "libavutil/time.h"
};

#include "util/safe_queue.h"
#include "util/Constants.h"
#include "JNICallback.h"
#include "../../jniLibs/include/libavcodec/xvmc.h"
#include "../../jniLibs/include/libavutil/rational.h"
#include "../../jniLibs/include/libavutil/frame.h"

class BaseChannel {
public:
    int stream_index;

    bool isPlaying = 1;
    bool isStop = false;

    AVCodecContext *pContext;
    JNICallback *javaCallback;

    // 音视频同步所需要
    AVRational base_time;
    double audio_time;
    double video_time;

    // AVPacket Audio: aac Video: h264
    SafeQueue<AVPacket *> packages;
    // AVFrame Audio: PCM Video: YUV
    SafeQueue<AVFrame *> frames;

    BaseChannel(int stream_index, AVCodecContext * pContext, AVRational av_base_time, JNICallback
    * jniCallback) {
        this->stream_index = stream_index;
        this->pContext = pContext;
        this->base_time = av_base_time;
        this->javaCallback = javaCallback;
        packages.setReleaseCallback(releaseAVPacket);
        frames.setReleaseCallback(releaseAVFrame);
    }

    // 父类, 析构函数必须是虚函数
    virtual ~BaseChannel() {
        packages.clearQueue();
        frames.clearQueue();
    }

    static void releaseAVPacket(AVPacket **avPacket) {
        if (avPacket) {
            av_packet_free(avPacket);
            *avPacket = 0;
        }
    }

    static void releaseAVFrame(AVFrame **avFrame) {
        if (avFrame) {
            av_frame_free(avFrame);
            *avFrame = 0;
        }
    }

    void clear() {
        packages.clearQueue();
        frames.clearQueue();
    }

};
#endif //VIDEOPLAYER_BASECHANNEL_H
