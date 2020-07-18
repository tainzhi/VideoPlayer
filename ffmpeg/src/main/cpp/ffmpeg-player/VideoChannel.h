#ifndef NDK_SAMPLE_VIDEOCHANNEL_H
#define NDK_SAMPLE_VIDEOCHANNEL_H


#include "BaseChannel.h"
#include "AudioChannel.h"
#include <unistd.h>


extern "C" {
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
}

typedef void (*RenderCallback)(uint8_t *, int, int, int);

class VideoChannel : public BaseChannel {

public:
    VideoChannel(int stream_index, AVCodecContext *pContext, AVRational, int,JNICallback* jniCallback);

    ~VideoChannel();

    void start();

    void stop();

    void video_decode();

    void video_player();

    void setRenderCallback(RenderCallback renderCallback);

    void setAudioChannel(AudioChannel* audioChannel);

    void release();

    void restart();



private:
    pthread_t pid_video_decode;
    pthread_t pid_video_player;
    RenderCallback renderCallback;

    int fpsValue;//视频 fps

    AudioChannel* audioChannel = 0;



};


#endif //NDK_SAMPLE_VIDEOCHANNEL_H
