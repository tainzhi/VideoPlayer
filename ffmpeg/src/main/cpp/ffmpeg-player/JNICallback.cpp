//
// Created by muqing on 2020/7/15.
// Email: qfq61@qq.com
//

#include "JNICallback.h"

JNICallback::JNICallback(JavaVM *javaVM, JNIEnv *env, jobject instance) {
    this->javaVM = javaVM;
    this->env = env;
    // jobject, 跨线程, 必须用全局引用
    this->instance = env->NewGlobalRef(instance);

    jclass playerClass = env->GetObjectClass(this->instance);
    const char * sigPrepred = "()V";
    const char *sigError = "(I)V";
    const char *sigProgress = "(I)V";


    this->jmd_prepared = env->GetMethodID(playerClass, "onPrepared", sigPrepred);
    this->jmd_error = env->GetMethodID(playerClass, "onError", sigError);
    this->jmd_progress = env->GetMethodID(playerClass, "onProgress", sigProgress);
}

void JNICallback::onPrepared(int thread_mode) {
    if (thread_mode == THREAD_MAIN) {
        env->CallVoidMethod(this->instance, jmd_prepared);
    } else {
        JNIEnv  *jniEnv = nullptr;
        jint ret = javaVM->AttachCurrentThread(&jniEnv, 0);
        if (ret != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(this->instance, jmd_prepared);
        javaVM->DetachCurrentThread();
    }
}

void JNICallback::onErrorAction(int thread_mode, int error_code) {
    if (thread_mode == THREAD_MAIN) {
        env->CallVoidMethod(this->instance, jmd_error,error_code);//主线程可以直接调用 Java 方法
    } else {
        //子线程，用附加 native 线程到 JVM 的方式，来获取到权限 env
        JNIEnv *jniEnv = nullptr;
        jint ret = javaVM->AttachCurrentThread(&jniEnv, 0);
        if (ret != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(this->instance, jmd_error,error_code);//开始调用 Java 方法
        javaVM->DetachCurrentThread();//解除附加
    }
}

void JNICallback::onProgress(int thread, int progress) {
    if (thread == THREAD_CHILD) {
        JNIEnv *jniEnv = nullptr;
        if (javaVM->AttachCurrentThread(&jniEnv, 0) != JNI_OK) {
            return;
        }
        jniEnv->CallVoidMethod(this->instance, jmd_progress, progress);
        javaVM->DetachCurrentThread();
    } else {
        env->CallVoidMethod(this->instance, jmd_progress, progress);
    }
}

JNICallback::~JNICallback() {
    LOGD("~JNICallback")
    this->javaVM = nullptr;
    env->DeleteGlobalRef(this->instance);
    this->instance = 0;
    env = nullptr;
}
