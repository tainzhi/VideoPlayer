//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#include <jni.h>
#include <android/native_window_jni.h>
#include <android/log.h>
#include "JNICallback.h"
#include "Player.h"


extern "C" {
#include <libavutil/avutil.h>
}

JavaVM *javaVM = nullptr;
Player * player = nullptr;
ANativeWindow * nativeWindow = nullptr;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;

int JNI_OnLoad(JavaVM *javaVm, void *pVoid) {
    ::javaVM = javaVm;
    return JNI_VERSION_1_6;
}

const char *getFFmpegVersion() {
    return av_version_info();
}

void renderFrame(uint8_t *src_data, int width, int height, int src_size) {
    pthread_mutex_lock(&mutex);

    if (!nativeWindow) {
        pthread_mutex_unlock(&mutex);
        nativeWindow = nullptr;
        return;
    }

    ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888);

    ANativeWindow_Buffer window_buffer;

    if (ANativeWindow_lock(nativeWindow, &window_buffer, 0)) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = nullptr;
        pthread_mutex_unlock(&mutex);
        return;
    }

    auto *dst_data = static_cast<uint8_t *>(window_buffer.bits);
    int lineSize = window_buffer.stride * 4; //RGBA
    // 逐行 copy
    for (int i = 0; i < window_buffer.height; ++i) {
        memcpy(dst_data + i * lineSize, src_data + i * src_size, lineSize);
    }
    ANativeWindow_unlockAndPost(nativeWindow);
    pthread_mutex_unlock(&mutex);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_ffmpegVersion(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF(getFFmpegVersion());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_setSurfaceNative(JNIEnv *env, jobject thiz,
                                                                     jobject surface) {
    LOGD("Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_setSurfaceNative")
    pthread_mutex_lock(&mutex);
    if (nativeWindow) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = nullptr;
    }
    nativeWindow = ANativeWindow_fromSurface(env, surface);
    pthread_mutex_unlock(&mutex);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_prepareNative(JNIEnv *env, jobject thiz,
                                                                  jstring dataSource) {
    LOGD("Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_prepareNative")
    auto * jniCallback = new JNICallback(javaVM, env, thiz);
    const char *data_source = env->GetStringUTFChars(dataSource, nullptr);
    player = new Player(data_source, jniCallback);
    player->setRenderCallback(renderFrame);
    player->prepare();
    env->ReleaseStringUTFChars(dataSource, data_source);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_startNative(JNIEnv *env, jobject thiz) {
    if (player) {
        player->start();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_restartNative(JNIEnv *env, jobject thiz) {
    if (player) {
        player->restart();
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_stopNative(JNIEnv *env, jobject thiz) {
    if (player) {
        player->stop();
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_releaseNative(JNIEnv *env, jobject thiz) {
    if (player) {
        player->release();
    }
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_isPlayingNative(JNIEnv *env, jobject thiz) {
    jboolean isPlaying = false;
    if (player) {
        isPlaying = player->isPlaying;
    }
    return isPlaying;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_getDuration(JNIEnv *env, jobject thiz) {
    if (player) {
        return player->getDuration();
    }
    return 0;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegPlayerManager_seek(JNIEnv *env, jobject thiz, jint progress) {
    if (player) {
        player->seek(progress);
    }
}