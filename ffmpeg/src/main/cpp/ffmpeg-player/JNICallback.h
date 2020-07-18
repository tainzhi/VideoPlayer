//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_JNICALLBACK_H
#define VIDEOPLAYER_JNICALLBACK_H

#include "util/Constants.h"
#include "util/macro.h"
#include <jni.h>

class JNICallback {
public:
    JNICallback(JavaVM *javaVm, JNIEnv *env, jobject instance);

    void onPrepared(int thread_mode);
    void onErrorAction(int thread_mode, int error_code);
    void onProgress(int thread, int progress);

    ~JNICallback();

private:
    JavaVM  *javaVM = nullptr;
    JNIEnv  *env = nullptr;
    jobject instance;

    jmethodID jmd_prepared;
    jmethodID  jmd_error;
    jmethodID  jmid_progress;

};


#endif //VIDEOPLAYER_JNICALLBACK_H
