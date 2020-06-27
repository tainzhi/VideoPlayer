# ffmpeg模块介绍

- 此module封装了[ffmpeg-4.3](http://ffmpeg.org/download.html#release_4.3), 用ndk20b编译
- 该module可以执行ffmpeg命令, 在cpp线程中执行, 提供了回调,命令执行进度,命令停止功能

# 使用方法

```kotlin
```kotlin
import com.tainzhi.android.ffmpeg.FFmpegInvokerimport 

// 获取编译ffmpeg的配置信息
val configInfo = FFmpegInvoker.getConfigInfo()

val callback = object: FFmpegInvoker.Callback {
    override fun onSuccess() {

    }

    override fun onFailure() {
    }

    override fun onProgress(percent: Float) {
    }
}

// 无需回调
FFmpegInvoker.exec("ffmpeg --version")

// 回调
FFmpegInvoker.exec("ffmpeg ffmpeg -i zhihu.mp4 -r 1 out%4d.png", callback)
// 退出耗时执行的命令任务
FFmpegInvoker.exit()
```

# 该项目介绍
真正执行ffmpeg命令的是`ffmpeg.c`中的`int ffmpeg_exec(int argc, char **argv)`
