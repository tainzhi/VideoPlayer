//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_PLAYER_H
#define VIDEOPLAYER_PLAYER_H


#include "JNICallback.h"
#include "AudioChannel.h"
#include "VideoChannel.h"
#include "util/macro.h"
#include <unistd.h>

extern "C" {
#include "libavformat/avformat.h"
#include "libavutil/time.h"
};

class Player {
public:
    Player();

    Player(const char *data_source, JNICallback *pCallback);

    ~Player();

    void prepare();

    void prepare_();

    void start();
    void start_();
    void restart();

    void stop();
    void release();
    void setRenderCallback(RenderCallback renderCallback);

    bool isPlaying;

    void seek(int i);

    int getDuration() {
        return duration;
    }

private:
    char *data_source = nullptr;
    pthread_t pid_prepare;
    pthread_t pid_start;

    AVFormatContext *formatContext = nullptr;
    AudioChannel *audioChannel = nullptr;
    VideoChannel *videoChannel = nullptr;
    JNICallback *pCallback = nullptr;

    bool isStop = false;
    RenderCallback renderCallback;
    AVCodecContext *codecContext = nullptr;

    pthread_mutex_t seekMutex;

    int duration = 0;

    bool isSeek = 0;


};


#endif //VIDEOPLAYER_PLAYER_H
