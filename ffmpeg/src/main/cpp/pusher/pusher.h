//
// Created by muqing on 2020/8/14.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_PUSHER_H
#define VIDEOPLAYER_PUSHER_H

#include <jni.h>
#include "PushCallback.h"
#include "RTMPModel.h"

class Pusher {
public:
    RTMPModel *rtmpModel = 0;
    
    int IsStart();
    
    int IsReadyPushing();
    
    void init(JavaVM *javaVm, JNIEnv *env, jobject instance) {
        PushCallback *pushCallback = new PushCallback(javaVm, env, instance);
        RTMPModel rtmpModel = new RTMPModel(pushCallback);
        
        this->rtmpModel = rtmpModel;
    }
    
    void release() {
        if (rtmpModel) {
            rtmpModel->release();
            delete  rtmpModel;
            rtmpModel = nullptr;
        }
    }
    
    void start(const char *path) {
        rtmpModel->_onConnect(path);
    }
};

#endif //VIDEOPLAYER_PUSHER_H
