cmake_minimum_required(VERSION 3.10.2)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall -UNDEBUG")
#set(CMAKE_C_STANDARD 99)

# 添加头文件/lib
include_directories(
        src/main/cpp/ffmpeg-invoker/include
        # player headers
        src/main/cpp/ffmpeg-player
        # pusher headers
        src/main/cpp/pusher
        # ffmpeg headers
        src/main/jniLibs/include
        # rtmp headers
        src/main/jniLibs/librtmp
)

# 定义源码所在目录
aux_source_directory(src/main/cpp/ffmpeg-invoker SRC_FFMPEG_INVOKER)

# 将 SRC_FFMPEG 添加到 SRC 中
aux_source_directory(src/main/cpp/ffmpeg-player SRC_FFMPEG_PLAYER)
#list(APPEND SRC ${SRC_FFMPEG_INVOKER})

aux_source_directory(src/main/cpp/pusher SRC_PUSHER)

# 必须先添加源文件, 再添加.so文件, 否则报错  Cannot specify link libraries for target which is not built by the project
# 加载多个 c 源文件
# file(GLOB native_srcs "src/main/cpp/*.c")
# 使用 ${native_srcs}
add_library(
        # Specifies the name of the library.
        ffmpeg-invoker
        # Sets the library as a shared library.
        # STATIC 静态库, 比如 livavcodec.a
        SHARED
        ${SRC_FFMPEG_INVOKER}
)

# ffmpeg player添加
add_library(
        ffmpeg-player SHARED ${SRC_FFMPEG_PLAYER}
)

add_library(
        pusher SHARED ${SRC_PUSHER}
)

# 设置ffmpeg库所在路径的目录
set(distribution_ffmpeg_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
# 设置rtmp静态库目录
set(distribution_rtmp ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/librtmp.a)

# 添加rtmp静态库
add_library( librtmp STATIC IMPORTED )
set_target_properties( librtmp
        PROPERTIES IMPORTED_LOCATION
        ${distribution_rtmp})

add_library( libavcodec SHARED IMPORTED )
set_target_properties( libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libavcodec.so)

add_library( libavdevice SHARED IMPORTED )
set_target_properties( libavdevice
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libavdevice.so )

add_library( libavfilter SHARED IMPORTED )
set_target_properties( libavfilter
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libavfilter.so )

add_library( libavformat SHARED IMPORTED )
set_target_properties( libavformat
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libavformat.so )

add_library( libavutil SHARED IMPORTED )
set_target_properties( libavutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libavutil.so )

add_library( libpostproc SHARED IMPORTED )
set_target_properties( libpostproc
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libpostproc.so )

add_library( libswresample SHARED IMPORTED )
set_target_properties( libswresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libswresample.so
)

add_library( libswscale SHARED IMPORTED )
set_target_properties( libswscale
        PROPERTIES IMPORTED_LOCATION
        ${distribution_ffmpeg_DIR}/libswscale.so
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

#播放 so
target_link_libraries(
        #        链接ffmpeg invoker
        ffmpeg-player
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
        #        native window
        android
        #        音频播放
        OpenSLES

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries(
        ffmpeg-invoker

        libavcodec
        libavdevice
        libavfilter
        libavformat
        libavutil
        libpostproc
        libswresample
        libswscale
        ${log-lib})

target_link_libraries(
        pusher

        librtmp

        libavcodec
        libavdevice
        libavfilter
        libavformat
        libavutil
        libpostproc
        libswresample
        libswscale
        ${log-lib})
