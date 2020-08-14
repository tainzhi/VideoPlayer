#include <jni.h>
#include <string.h>
#include <android/log.h>


extern "C" {
#include "ffmpeg.h"
#include "ffmpeg_thread.h"
//    #include "android_log.h"
#include "cmdutils.h"
#include "ffmpeg-invoker.h"
#include "include/log.h"
}

static JavaVM * jvm = nullptr; //java虚拟机
static jobject object = nullptr;

/**
 * 回调执行Java方法
 * 参看 Jni反射+Java反射
 */
void callJavaMethod(JNIEnv *env, jclass clazz,int ret) {
    if (clazz == nullptr) {
        LOGE("---------------clazz isnullptr---------------");
        return;
    }
    //todo remove log
    LOGD("callJavaMethod()");
    if (clazz == nullptr) {
        LOGE("class is null");
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成
    jmethodID methodID = env->GetStaticMethodID(clazz, "onExecuted", "(I)V");
    if (methodID == nullptr) {
        LOGE("---------------methodID isnullptr---------------");
        return;
    }
    //调用该java方法
    env->CallStaticVoidMethod(clazz, methodID,ret);
}

void callJavaMethodProgress(JNIEnv *env, jclass clazz,float ret) {
    if (clazz == nullptr) {
        LOGE("---------------clazz isnullptr---------------");
        return;
    }
    //获取方法ID (I)V指的是方法签名 通过javap -s -public FFmpegCmd 命令生成
    jmethodID methodID = env->GetStaticMethodID(clazz, "onProgress", "(F)V");
    if (methodID == nullptr) {
        LOGE("---------------methodID isnullptr---------------");
        return;
    }
    //调用该java方法
    env->CallStaticVoidMethod( clazz, methodID,ret);
}

/**
 * c语言-线程回调
 */
static void ffmpeg_callback(int ret) {
    JNIEnv *env;
    //附加到当前线程从JVM中取出JNIEnv, C/C++从子线程中直接回到Java里的方法时  必须经过这个步骤
    jvm->AttachCurrentThread(&env, nullptr);
    jclass clazz = env->GetObjectClass(object);
    callJavaMethod(env, clazz,ret);

    //完毕-脱离当前线程
    jvm->DetachCurrentThread();
}

void ffmpeg_progress(float percent) {
    JNIEnv *env;
    jvm->AttachCurrentThread(&env, nullptr);
    jclass clazz = env->GetObjectClass(object);
    callJavaMethodProgress(env, clazz,percent);
    jvm->DetachCurrentThread();
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exec(JNIEnv *env, jobject thiz, jint cmdnum,
                                                   jobjectArray cmdline) {
    //set java vm
    env->GetJavaVM(&jvm);
    // todo delete GlobalRef
    object = env->NewGlobalRef(thiz);

    //---------------------------------C语言 反射Java 相关----------------------------------------
    //---------------------------------java 数组转C语言数组----------------------------------------
    int i = 0;//满足NDK所需的C99标准
    char **argv = nullptr;//命令集 二维指针
    jstring *strr = nullptr;

    if (cmdline != nullptr) {
        argv = (char **) malloc(sizeof(char *) * cmdnum);
        strr = (jstring *) malloc(sizeof(jstring) * cmdnum);

        for (i = 0; i < cmdnum; ++i) {//转换
            strr[i] = (jstring)env->GetObjectArrayElement(cmdline, i);
            argv[i] = (char *) env->GetStringUTFChars(strr[i], 0);
        }

    }
    //---------------------------------java 数组转C语言数组----------------------------------------
    //---------------------------------执行FFmpeg命令相关----------------------------------------
    //新建线程 执行ffmpeg 命令
    ffmpeg_thread_run_cmd(cmdnum, argv);
    //注册ffmpeg命令执行完毕时的回调
    ffmpeg_thread_callback(ffmpeg_callback);

    free(strr);
    return 0;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exit(JNIEnv *env, jobject thiz) {
    env->GetJavaVM(&jvm);
    ffmpeg_thread_cancel();
    env->DeleteGlobalRef(object);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getConfigInfo(JNIEnv *env, jobject thiz) {
    char info[10000] = {0};
    sprintf(info, "%s\n", avcodec_configuration());
    return env->NewStringUTF(info);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFormatInfo(JNIEnv *env, jobject thiz) {
    char info[40000] = {0};

    AVCodec *c_temp = av_codec_next(nullptr);
    while (c_temp != nullptr) {
        if (c_temp->decode != nullptr) {
            sprintf(info, "%s[dec]", info);
        }
        else {
            sprintf(info, "%s[enc]", info);
        }
        switch (c_temp->type) {
            case AVMEDIA_TYPE_VIDEO:
                sprintf(info, "%s[Video]", info);
                break;
            case AVMEDIA_TYPE_AUDIO:
                sprintf(info, "%s[Audio]", info);
                break;
            default:
                sprintf(info, "%s[Other]", info);
                break;
        }
        sprintf(info, "%s[%10s]\n", info, c_temp->name);


        c_temp = c_temp->next;
    }

    return env->NewStringUTF(info);
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFilterInfo(JNIEnv *env, jobject thiz) {
    char info[40000] = { 0 };

    AVInputFormat *if_temp = av_iformat_next(nullptr);
    AVOutputFormat *of_temp = av_oformat_next(nullptr);
    //Input
    while (if_temp != nullptr) {
        sprintf(info, "%s[In ][%10s]\n", info, if_temp->name);
        if_temp = if_temp->next;
    }
    //Output
    while (of_temp != nullptr) {
        sprintf(info, "%s[Out][%10s]\n", info, of_temp->name);
        of_temp = of_temp->next;
    }

    return env->NewStringUTF(info);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVCodecInfo(JNIEnv *env, jobject thiz) {
    char info[40000] = { 0 };
    AVFilter *f_temp = (AVFilter *)avfilter_next(nullptr);
    while (f_temp != nullptr) {
        sprintf(info, "%s[%10s]\n", info, f_temp->name);
        f_temp = f_temp->next;
    }

    return env->NewStringUTF(info);
}