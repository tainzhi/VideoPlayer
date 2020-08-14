//
// Created by muqing on 2020/8/14.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_RTMPMODEL_H
#define VIDEOPLAYER_RTMPMODEL_H

#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <librtmp/rtmp.h>
#include "PushCallback.h"
#include "../../ffmpeg-player/util/safe_queue.h"
#include "AudioEncoderChannel.h"
#include "VideoEncoderChannel.h"
#include "log.h"


class RTMPModel {

public:
    /**
    * 定义 rtmp pact 队列
    */
    SafeQueue<RTMPPacket *> mPackets;
    /**
      * 定义推流标志位
    */
    int isStart = false;
    
    RTMPModel(PushCallback *pCallback, AudioEncoderChannel *audioEncoderChannel,
              VideoEncoderChannel *videoEncoderChannel, int mediacodec);
    
    ~RTMPModel();
    
    void _onConnect(const char *url);
    
    void onConnect();
    
    
    void onPush();
    
    
    void release();
    
    
    /**
     * 定义一个 RTMP 开始链接的时间
     */
    uint32_t mStartTime;
    /**
    * 定义准备推流的标志
    */
    int readyPushing = false;
    
    void restart();
    
    void stop();
    
    RTMP *rtmp = 0;
    
    
    void setMediaCodec(int mediacodec);

private:
    char *url = 0;
    PushCallback *pushCallback;
    pthread_mutex_t *mMutex = 0;
    pthread_t mPid;
    
    
    /**
      * 头包 Audio 信息
    */
    RTMPPacket *mAudioPacketTag = 0;
    
    /**
     * 定义视频处理通道
     */
    VideoEncoderChannel *mVideoChannel = 0;
    
    /**
     * 定义音频处理通道
     */
    AudioEncoderChannel *mAudioChannel = 0;
    
    void setPacketReleaseCallback();
    
    
    int isMediacodec = false;
    
};


#endif //VIDEOPLAYER_RTMPMODEL_H
