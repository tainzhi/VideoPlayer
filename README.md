#MyVideoPlayer
##Introduction has only two activity, one is to show the list, then ohter is show the playing video.
- MainActivity: 重写了adapter，并读取数据库，写入视频信息到列表
- PlayVideo: 简单的实现了播放界面

##Notes
###参考学习
- [recyclereview-playround](https://github.com/devunwired/recyclerview-playground) StaggeredGridView用法实例的github项目
- [提供RecycleView封装的api](http://lucasr.org/2014/07/31/the-new-twowayview/)
- [android官方文档 StaggeredGridLayoutManager](http://developer.android.com/reference/android/support/v7/widget/StaggeredGridLayoutManager.html#)

###How to getThumbnail of a Video

##### MediaStore.Video.Thumbnails.getThhumbnail( , , MediaStore.Video.Thumbnails.MICRO_KIND,)
```
Bitmap thumbBitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(),
            item.videoId,
            MediaStore.Video.Thumbnails.MICRO_KIND,
            (BitmapFactory.Options)null);
```
Thumbnails widht and height is 96*96

##### MediaStore.Video.Thumbnails.getThhumbnail( , , MediaStore.Video.Thumbnails.MINI_KIND,)
```
BitmapFactory.Options options = new BitmapFactory.Options();
        options.outHeight = mThumbnailParentWidth;
        options.outWidth = mThumbnailParentWidth;
Bitmap thumbBitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(),
            item.videoId,
            MediaStore.Video.Thumbnails.MINI_KIND,
            options);
```
Thumbnails widht and height is 288*512

options is for decoding MINI_KIND, so it will not be usefull if options.outWidth is not fit for MINI_KIND

#####ThumbnailUtils.createVideoThumbnail####

[References](http://www.jianshu.com/p/4aa82a21b4b5)

#####MediaMetadataRetriver####

[MediaMetadataRetriver.getFrameAt](http://developer.android.com/intl/zh-cn/reference/android/media/MediaMetadataRetriever.html#getFrameAtTime)

[MediaMetadataRetriver与ThumbnailtUtils.createVideoThumbnail的关系](http://m.blog.csdn.net/blog/Mr_dsw/48524977), 可以查看源码

###RecyclerView don't has onItemClickListener
RecyclerView don't has onItemClickLister&& onItemLongClickListern, so you have to implement them.
[参考](http://stackoverflow.com/questions/24471109/recyclerview-onclick/26826692#26826692)

###AsyncTask加载列表缩略图
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/process-bitmap.html
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/process-bitmap.html
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/display-bitmap.html

###获取更清晰额缩略图并缓存在cache中
- 使用MediaMetadataRetriever.getFrameAtTime()获取缩略图, 如果获取失败是MediaStore.Video.Thumbnails.getThumbnail()获取
- 获取的缩略图缓存在externalCacheDir()中, 通过id和progres命名

###DoubleMovieActivity
两个播放窗口, 但是没有播放声音
[参考](https://github.com/google/grafika/blob/master/src/com/android/grafika/DoubleDecodeActivity.java)

###MediaPlayer
http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
http://developer.android.com/intl/zh-cn/guide/topics/media/mediaplayer.html

###SurfaceView&&VideoView
VideoView extends SurfaceView

http://developer.android.com/reference/android/view/SurfaceView.html
http://developer.android.com/reference/android/widget/VideoView.html#getAudioSessionId()
http://stackoverflow.com/questions/16700587/how-to-attach-mediaplayer-with-surfaceview-in-android
http://stackoverflow.com/questions/16700587/how-to-attach-mediaplayer-with-surfaceview-in-android

###Thread&&Runnable实现SeekBar自动更新进度
http://stackoverflow.com/questions/17168215/seekbar-and-media-player-in-android
http://developer.android.com/reference/java/lang/Runnable.html
http://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread
http://stackoverflow.com/questions/3658779/threads-in-java


###使用Callback机制和ViewStub实现无视频时的提示界面


###FloatWindow
主要使用了`WindowManager`. 显示悬浮窗有两种方式
- 方式1: 需要权限`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>`
- 方式2: 不需要权限, 但是需要设置`WindownManager.LayoutParams.type`为`WindowManager.LayoutParams.TYPE_TOAST`

**Todo**: 移动悬浮窗的时候, 悬浮窗会一直抖动, 这个问题需要解决

[参考](http://www.liaohuqiu.net/cn/posts/android-windows-manager/)

###ActionBar显示title&&icon

###GalleryView弃用, 改用HorizontalGridView
- PopWindow必须设置width和height, 否则无法显示
[参考](https://android.googlesource.com/platform/development/+/master/samples/SupportLeanbackDemos/src/com/example/android/leanback/HorizontalGridTestActivity.java)
- WeakHashMap

###getFrameAt
http://stackoverflow.com/questions/12772547/mediametadataretriever-getframeattime-returns-only-first-frame
http://stackoverflow.com/questions/14460151/mediametadataretriever-getframeattimelong-option-crashing
http://stackoverflow.com/questions/20624996/android-get-all-the-frames-of-a-video-any-video-file
https://github.com/wseemann/FFmpegMediaMetadataRetriever

###DiskCache保存缩略图
https://github.com/JakeWharton/DiskLruCache
http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method

##Todo
- [+] StaggedGridView
- [+] thumbnail.scale
- [+] Item display
- [+] AsyncTask加载缩略图, scroom the list smoothly
- [+] 优化1: 把获取的缩略图存储起来
- [+] DoubleViewActivity
- [+] ViewStub覆盖住主界面, 没有视频的时候
- [+] ActionBar重写
- [+] ActionBar图标
- [+] App图标
- [+] 播放界面, with SurfaceView
- [+] 播放界面的操作: 快进, 快退, 播放
- [+] 响应Touch事件
- [ ] 最后播放的视频特殊化
- [+] 缩略图使用最后退出时一帧的图片, AsyncTask实现
- [+] 悬浮播放窗
- [ ] SearchView的实现
- [ ] CheckBox的实现
- [ ] DoubleViewActivity优化: 添加声音, 控制
- [+] 类似搜狐视频一样的截屏进度条
- [+] DiskCache进度条产生的缩略图文件
- [ ] Animation:显示和隐藏播放界面控制栏动画
- [ ] Animation:列表界面删除动画

##图片介绍
![MainActivity](./listview.png)

![PlayVideo](./play.png)