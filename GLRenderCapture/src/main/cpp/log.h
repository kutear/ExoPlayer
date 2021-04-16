//
// Created by kutear.guo on 2021/4/16.
//

#ifndef GL_CAPTURE_LOG_H
#define GL_CAPTURE_LOG_H
#define LOG_TAG    "gl-capture"

#include <android/log.h>

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

#endif //EXOPLAYER_LOG_H
