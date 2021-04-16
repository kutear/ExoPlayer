//
// Created by kutear.guo on 2021/4/25.
//

#ifndef GL_CAPTURE_XHOOK_FOR_GL_H
#define GL_CAPTURE_XHOOK_FOR_GL_H
#include <stdio.h>
#include "xhook.h"

static int my_system_log_print(int prio, const char *tag, const char *fmt, ...) {
  va_list ap;
  char buf[1024];
  int r;

  snprintf(buf, sizeof(buf), "[%s] %s", (NULL == tag ? "" : tag), (NULL == fmt ? "" : fmt));

  va_start(ap, fmt);
  r = __android_log_vprint(prio, "xhook_system", buf, ap);
  va_end(ap);
  return r;
}

void hook_gl() {
  xhook_register("libnative.so",
                 "__android_log_print",
                 reinterpret_cast<void *>(my_system_log_print),
                 nullptr);
  xhook_refresh(0);
  xhook_clear();
}


#endif
//GL_CAPTURE_XHOOK_FOR_GL_H
