#include <jni.h>

#ifndef FFmpeg_Invoker
#define FFmpeg_Invoker
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exec(JNIEnv *, jobject , jint, jobjectArray);

JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exit(JNIEnv *, jobject );


JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getConfigInfo(JNIEnv *, jobject);

JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVCodecInfo(JNIEnv *env, jobject );

JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFormatInfo(JNIEnv *, jobject);

JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFilterInfo(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif

void ffmpeg_progress(float percent);

