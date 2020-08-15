#include <jni.h>

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativeInit(JNIEnv *env, jobject thiz,
                                                         jboolean is_media_codec) {
    // TODO: implement nati
    //  veInit()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativeStart(JNIEnv *env, jobject thiz, jstring path) {
    // TODO: implement nativeStart()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativeRelease(JNIEnv *env, jobject thiz) {
    // TODO: implement nativeRelease()
}
extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativeSetVideoEncoderInfo(JNIEnv *env, jobject thiz,
                                                                        jint width, jint height,
                                                                        jint fps, jint bit) {
    // TODO: implement nativeSetVideoEncoderInfo()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativeSetAudioEncoderInfo(JNIEnv *env, jobject thiz,
                                                                        jint sample_rate,
                                                                        jint channels) {
    // TODO: implement nativeSetAudioEncoderInfo()
}extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativePushAudio(JNIEnv *env, jobject thiz,
                                                              jbyteArray data) {
    // TODO: implement nativePushAudio()
}extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_pushH264(JNIEnv *env, jobject thiz,
                                                       jbyteArray h264_data, jint type,
                                                       jint time_stamp) {
    // TODO: implement pushH264()
}extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_pushAACData(JNIEnv *env, jobject thiz,
                                                          jbyteArray audio, jint length,
                                                          jint time_stamp) {
    // TODO: implement pushAACData()
}extern "C"
JNIEXPORT void JNICALL
Java_com_tainzhi_android_ffmpeg_PusherManager_nativePushVideo(JNIEnv *env, jobject thiz,
                                                              jbyteArray data) {
    // TODO: implement nativePushVideo()
}