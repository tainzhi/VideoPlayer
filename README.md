VideoPlayer

## Info
This is a repository about my videoplayer

![demo](./demo.gif)

## 参考
- [影视天地（Android 客户端），涵盖电影、电视剧、综艺节目、动漫、游戏五大类别的资源。整合两大资源搜索引擎，连接互联网千万数量级的资源库。支持视频资源在线播放、边下边播，不限速下载，不等待播放](https://github.com/123lxw123/VideoWorld_Android) 
- [abigfishtv ios 大鱼电视直播 基于ijkplayer的播放器 700多个电视台 包括央视，各地方台，卫视，熊猫直播，社会化分享，登陆，仿微博等 (支持iphonex)](https://github.com/clyhs/ABigFishTV) 
- [各种视频播放所需要的功能集](https://github.com/yangchong211/YCVideoPlayer) 
- [后期项目怎么优化](https://github.com/yangchong211/LifeHelper)
- [QSVideoPlayer](https://github.com/tohodog/QSVideoPlayer) 

## Notes

### How to getThumbnail of a Video

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

##### ThumbnailUtils.createVideoThumbnail

[References](http://www.jianshu.com/p/4aa82a21b4b5)

##### MediaMetadataRetriver

[MediaMetadataRetriver.getFrameAt](http://developer.android.com/intl/zh-cn/reference/android/media/MediaMetadataRetriever.html#getFrameAtTime)

[MediaMetadataRetriver与ThumbnailtUtils.createVideoThumbnail的关系](http://m.blog.csdn.net/blog/Mr_dsw/48524977), 可以查看源码

### RecyclerView don't has onItemClickListener
RecyclerView don't has onItemClickLister&& onItemLongClickListern, so you have to implement them.
[参考](http://stackoverflow.com/questions/24471109/recyclerview-onclick/26826692#26826692)

### AsyncTask加载列表缩略图
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/process-bitmap.html
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/process-bitmap.html
http://developer.android.com/intl/zh-cn/training/displaying-bitmaps/display-bitmap.html

### 获取更清晰额缩略图并缓存在cache中
- 使用MediaMetadataRetriever.getFrameAtTime()获取缩略图, 如果获取失败是MediaStore.Video.Thumbnails.getThumbnail()获取
- 获取的缩略图缓存在externalCacheDir()中, 通过id和progres命名

### DoubleMovieActivity
两个播放窗口, 但是没有播放声音
[参考](https://github.com/google/grafika/blob/master/src/com/android/grafika/DoubleDecodeActivity.java)

### MediaPlayer
http://developer.android.com/intl/zh-cn/reference/android/media/MediaPlayer.html
http://developer.android.com/intl/zh-cn/guide/topics/media/mediaplayer.html

### SurfaceView&&VideoView
VideoView extends SurfaceView

http://developer.android.com/reference/android/view/SurfaceView.html
http://developer.android.com/reference/android/widget/VideoView.html#getAudioSessionId()
http://stackoverflow.com/questions/16700587/how-to-attach-mediaplayer-with-surfaceview-in-android
http://stackoverflow.com/questions/16700587/how-to-attach-mediaplayer-with-surfaceview-in-android

### Thread&&Runnable实现SeekBar自动更新进度
http://stackoverflow.com/questions/17168215/seekbar-and-media-player-in-android
http://developer.android.com/reference/java/lang/Runnable.html
http://stackoverflow.com/questions/541487/implements-runnable-vs-extends-thread
http://stackoverflow.com/questions/3658779/threads-in-java


### 使用Callback机制和ViewStub实现无视频时的提示界面


### FloatWindow
主要使用了`WindowManager`. 显示悬浮窗有两种方式
- 方式1: 需要权限`<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>`
- 方式2: 不需要权限, 但是需要设置`WindownManager.LayoutParams.type`为`WindowManager.LayoutParams.TYPE_TOAST`

**Todo**: 移动悬浮窗的时候, 悬浮窗会一直抖动, 这个问题需要解决

[参考](http://www.liaohuqiu.net/cn/posts/android-windows-manager/)

### ActionBar显示title&&icon

### GalleryView弃用, 改用HorizontalGridView
- PopWindow必须设置width和height, 否则无法显示
[参考](https://android.googlesource.com/platform/development/+/master/samples/SupportLeanbackDemos/src/com/example/android/leanback/HorizontalGridTestActivity.java)
- WeakHashMap

### getFrameAt
http://stackoverflow.com/questions/12772547/mediametadataretriever-getframeattime-returns-only-first-frame
http://stackoverflow.com/questions/14460151/mediametadataretriever-getframeattimelong-option-crashing
http://stackoverflow.com/questions/20624996/android-get-all-the-frames-of-a-video-any-video-file
https://github.com/wseemann/FFmpegMediaMetadataRetriever

### DiskCache保存缩略图
https://github.com/JakeWharton/DiskLruCache
http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
http://stackoverflow.com/questions/10185898/using-disklrucache-in-android-4-0-does-not-provide-for-opencache-method

### Animation

## 基于
MVP, retrofit2, rxjava2

## Todo
- [ ] todo: Fragment实现本地视频，和电视直播，斗鱼直播等功能
- [ ] [Recyclerview covered by BottomNavigationView](https://stackoverflow.com/questions/41199801/recyclerview-covered-by-bottomnavigationview)
- [ ] Douyu界面,无法通过`GridLayoutManager.setPanSize()`设置第一行只有1列显示
- [ ] 电视频道
> - [ ] [电视猫](https://www.tvmao.com/program/CCTV-CCTV3-w1.html) , 
> - [ ]可以显示当前正在播放的内容(这个需要爬虫爬取结果, 用个热点小图标显示)
> - [ ]爬取频道的标志图片, 用圆形图片显示; 三级缓存实现
> - [ ]该界面搜索: 是搜索的卫视频道 
> - [ ]列表滑动删除: [列表滑动删除](https://www.androidhive.info/2017/09/android-recyclerview-swipe-delete-undo-using-itemtouchhelper/?utm_source=recyclerview&utm_medium=site&utm_campaign=refer_article) 
- [ ] 本地视频
> - [ ] 该界面搜索: 搜索本地视频
> - [ ] 参考小米视频: 实现最近播放记录功能(或者用videopage实现)
- [ ]进度条+小窗显示
- [ ]悬浮窗播放
- [ ]记录视频播放记录
- [ ]混淆编译
- [ ]添加自动测试模块
- [ ]ExoPlayer, vitamio:[部署参考](https://github.com/tainzhi/QDouyu/tree/v0.1), ijkplayer提供切换接口
- [ ]搭建一个局域网服务器, 提供全国视频爬取的链接
- [ ]搭建一个局域网服务器, 提供美剧tab的搜索和展示
- [ ]升级RxJava1.x到RxJava2.x
- [ ]kotlin实现



