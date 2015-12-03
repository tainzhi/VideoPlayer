#MyVideoPlayer ##Introduction has only two activity, one is to show the list, then ohter is show the playing video.
- MainActivity: 重写了adapter，并读取数据库，写入视频信息到列表
- PlayVideo: 简单的实现了播放界面

##参考学习
- [recyclereview-playround](https://github.com/devunwired/recyclerview-playground) StaggeredGridView用法实例的github项目
- [提供RecycleView封装的api](http://lucasr.org/2014/07/31/the-new-twowayview/)
- [android官方文档 StaggeredGridLayoutManager](http://developer.android.com/reference/android/support/v7/widget/StaggeredGridLayoutManager.html#)

##How to getThumbnail of a Video

#### MediaStore.Video.Thumbnails.getThhumbnail( , , MediaStore.Video.Thumbnails.MICRO_KIND,)
```
Bitmap thumbBitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(),
            item.videoId,
            MediaStore.Video.Thumbnails.MICRO_KIND,
            (BitmapFactory.Options)null);
```
Thumbnails widht and height is 96*96

#### MediaStore.Video.Thumbnails.getThhumbnail( , , MediaStore.Video.Thumbnails.MINI_KIND,)
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

####ThumbnailUtils.createVideoThumbnail####

[References](http://www.jianshu.com/p/4aa82a21b4b5)

####MediaMetadataRetriver####

[MediaMetadataRetriver.getFrameAt](http://developer.android.com/intl/zh-cn/reference/android/media/MediaMetadataRetriever.html#getFrameAtTime)

[MediaMetadataRetriver与ThumbnailtUtils.createVideoThumbnail的关系](http://m.blog.csdn.net/blog/Mr_dsw/48524977), 可以查看源码


##Notes
##Todo
- [+] StaggedGridView
- [+] thumbnail.scale
- [ ] Item display
- [ ] 删除动画
- [ ] How to scrool the list smoothly?
- [ ] ViewStub覆盖住主界面, 没有视频的时候
- [ ] ActionBar重写
- [ ] ActionBar图标
- [ ] App图标
- [ ] 播放界面
- [ ] 播放界面的操作: 快进, 快退, 播放
- [ ] 响应Touch事件
- [ ] 最后播放的视频特殊化
- [ ] 缩略图使用最后退出时一帧的图片, AsyncTask实现
- [ ] 悬浮播放窗
- [ ] 类似搜狐视频一样的截屏进度条
- [ ] 各种界面, 性能优化

##图片介绍
![MainActivity](./listview.png)

![PlayVideo](./play.png)