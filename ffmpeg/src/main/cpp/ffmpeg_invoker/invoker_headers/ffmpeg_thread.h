#include "ffmpeg.h"

int ffmpeg_thread_run_cmd(int cmdnum,char **argv);

void ffmpeg_thread_exit(int ret);

void ffmpeg_thread_callback(void (*cb)(int ret));

void ffmpeg_thread_cancel();