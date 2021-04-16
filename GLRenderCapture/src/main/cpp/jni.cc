//
// Created by kutear.guo on 2021/4/16.
//

#ifndef GL_CAPTURE
#define GL_CAPTURE
#include <jni.h>
#include "log.h"
#ifdef XHOOK
#include "xhook_for_gl.h"
#endif

#define JNI_API(f) Java_com_kutear_gl_render_capture_JNI_00024Companion_##f
JavaVM *javaVM = nullptr;

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
  LOGD("===> JNI_OnLoad");
  javaVM = vm;
  JNIEnv *env = nullptr;
  if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
    return JNI_ERR;
  }
  vm->AttachCurrentThread(&env, nullptr);
  return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_API(attach)(JNIEnv *env, jobject thiz) {
  LOGD("===> attach");
  if (javaVM != nullptr) {
    javaVM->AttachCurrentThread(&env, nullptr);
  }
}

extern "C" JNIEXPORT void JNICALL
JNI_API(start)(JNIEnv *env, jobject thiz) {
  LOGD("===> start");
#if XHOOK
  xhook_enable_debug(true);
  hook_gl();
#endif
}


extern "C" JNIEXPORT void JNICALL
JNI_API(stop)(JNIEnv *env, jobject thiz) {
  LOGD("===> stop");
}
#endif