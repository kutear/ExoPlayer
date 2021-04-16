//
// Created by kutear.guo on 2021/4/25.
//

#include <jni.h>
#include "android/log.h"

JNIEXPORT void JNICALL
Java_com_google_android_exoplayer2_gldemo_JNI_sayHello(JNIEnv *env, jobject thiz) {
  __android_log_print(ANDROID_LOG_ERROR, "JNI_sayHello", "hello");
}