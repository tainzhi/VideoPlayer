cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -UNDEBUG")

# 必须先添加源文件, 再添加.so文件, 否则报错  Cannot specify link libraries for target which is not built by the project
# 加载多个 c 源文件
file(GLOB native_srcs "src/main/jniLibs/*.c")
add_library(
        # Specifies the name of the library.
        ffmpeg-invoke
        # Sets the library as a shared library.
        # STATIC 静态库, 比如 livavcodec.a
        SHARED

        # Provides a relative path to your source file(s).
        ${native_srcs}
        src/main/cpp/ffmpeg-invoke.cpp
#        ffmpeg-invoke.cpp
#        cmdutils.c
#        ffmpeg.c
#        ffmpeg_filter.c
#        ffmpeg_hw.c
#        ffmpeg_opt.c
#        ffmpeg_qsv.c
#        ffmpeg_videotoolbox.c
#        ffplay.c
#        ffprobe.c
)

# 添加头文件
include_directories(src/main/cpp)
include_directories(src/main/jniLibs/include)

# set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
set(so_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs)

add_library( libavcodec SHARED IMPORTED )
set_target_properties( libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libavcodec.so)

add_library( libavdevice SHARED IMPORTED )
set_target_properties( libavdevice
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libavdevice.so )

add_library( libavfilter SHARED IMPORTED )
set_target_properties( libavfilter
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libavfilter.so )

add_library( libavformat SHARED IMPORTED )
set_target_properties( libavformat
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libavformat.so )

add_library( libavutil SHARED IMPORTED )
set_target_properties( libavutil
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libavutil.so )

add_library( libpostproc SHARED IMPORTED )
set_target_properties( libpostproc
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libpostproc.so )

add_library( libswresample SHARED IMPORTED )
set_target_properties(
        libswresample
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libswresample.so
)


add_library( libswscale SHARED IMPORTED )
set_target_properties( libswscale
        PROPERTIES IMPORTED_LOCATION
        ${so_DIR}/libswscale.so
)

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


# Include libraries needed for native-codec-jni lib
target_link_libraries(
#        链接ffmpeg
        ffmpeg-invoke
#        编码(最重要)
        libavcodec
#        设备信息
        libavdevice
#        滤镜特效处理
        libavfilter
#        封装格式处理
        libavformat
#        工具库(大部分库都需要该库支持)
        libavutil
#        后期处理
        libpostproc
#        音频采样数据格式转换库
        libswresample
#        视频像素数据格式转换
        libswscale


        -landroid

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
