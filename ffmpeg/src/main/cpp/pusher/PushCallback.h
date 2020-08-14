//
// Created by muqing on 2020/8/13.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_PUSHCALLBACK_H
#define VIDEOPLAYER_PUSHCALLBACK_H

#include <jni.h>
#include "../../ffmpeg-player/util/Constants.h"


class PushCallback {
  public :
    PushCallback(JavaVM *javaVM, JNIEnv *env, jobject jobject);

    ~PushCallback();

    /**
     * 开始链接
     */
    void onRtmpConnect(int thread_mode);

    /**
     * 连接成功
     */
    void onRtmpSucceed(int thread_mode);

    /**
     * 连接失败
     * @param errCode  错误码
     */
    void onError(int thread_mode,int errCode);

private:
    JavaVM *mJavaVM = 0;
    JNIEnv *mJNIEnv = 0;
    jobject mJobject;

    //开始连接的回调
    jmethodID mConnectId;
    jmethodID mSucceedId;
    jmethodID mErrorId;
};


#endif //VIDEOPLAYER_PUSHCALLBACK_H
