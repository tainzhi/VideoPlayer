//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_AUDIOCHANNEL_H
#define VIDEOPLAYER_AUDIOCHANNEL_H


#include "../../jniLibs/include/libavcodec/vdpau.h"
#include "../../jniLibs/include/libavutil/rational.h"
#include "JNICallback.h"

#include "BaseChannel.h"
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_android.h>
#include <libswresample/swresample.h>

#define AUDIO_SAMPLE_RATE 44100

class AudioChannel: public BaseChannel {
public:
    AudioChannel(int stream_index,
            AVCodecContext *pContext, AVRational, JNICallback *jniCallback);
    ~AudioChannel();

    void stop();
    void start();
    void audio_decode();
    void audio_player();
    int getPCM();

    uint8_t *out_buffers = 0;

    int out_channels;
    int out_sample_size;
    int out_sample_rate;
    int out_buffers_size;

    void release();
    void restart();

private:
    pthread_t pid_audio_decode;
    pthread_t pid_audio_player;

    // 引擎
    SLObjectItf engineObject;
    // 引擎接口
    SLEngineItf engineInterface;
    // 混音器
    SLObjectItf outputMixObject;
    // 播放器
    SLObjectItf bqPlayerObject;
    // 播放器接口
    SLPlayItf bqPlayerPlay;
    // 获取播放器队列接口
    SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue;

    SwrContext *swr_ctx;

};


#endif //VIDEOPLAYER_AUDIOCHANNEL_H
