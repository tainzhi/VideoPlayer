#include <jni.h>
extern "C"
JNIEXPORT jint JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exec(JNIEnv *, jobject, jint, jobjectArray);

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_exit(JNIEnv *, jobject);


extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getConfigInfo(JNIEnv *, jobject);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVCodecInfo(JNIEnv *env, jobject);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFormatInfo(JNIEnv *, jobject);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tainzhi_android_ffmpeg_FFmpegInvoker_getAVFilterInfo(JNIEnv *, jobject);

void ffmpeg_progress(float percent);

