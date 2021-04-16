# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

set(TARGET xhook)
project(${TARGET})


# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
#add_compile_options(-Wall -funwind-tables -fPIC -O2)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


add_library( # Sets the name of the library.
        ${TARGET}

        # XHOOK
        queue.h
        tree.h
        xh_core.c
        xh_core.h
        xh_elf.h
        xh_elf.c
        xh_errno.h
        xh_jni.c
        xh_log.c
        xh_log.h
        xh_util.c
        xh_util.h
        xh_version.c
        xh_version.h
        xhook.c
        xhook.h)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)
