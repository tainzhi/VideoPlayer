//
// Created by muqing on 2020/8/14.
// Email: qfq61@qq.com
//

#ifndef VIDEOPLAYER_LOG_H
#define VIDEOPLAYER_LOG_H

#include <android/log.h>

#define TAG "pusher"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)

#endif //VIDEOPLAYER_LOG_H
