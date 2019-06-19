VideoPlayer

## Info
![demo](./demo.gif | width=600)
这是一个视频播放器，又不仅仅是一个播放器。它不仅能观看本地视频，还能看斗鱼直播， 能看央视和各大地方卫士，能看美剧和电影。许多功能正在紧张的开发中。。。敬请期待
## 参考
- [影视天地（Android 客户端），涵盖电影、电视剧、综艺节目、动漫、游戏五大类别的资源。整合两大资源搜索引擎，连接互联网千万数量级的资源库。支持视频资源在线播放、边下边播，不限速下载，不等待播放](https://github.com/123lxw123/VideoWorld_Android) 
- [abigfishtv ios 大鱼电视直播 基于ijkplayer的播放器 700多个电视台 包括央视，各地方台，卫视，熊猫直播，社会化分享，登陆，仿微博等 (支持iphonex)](https://github.com/clyhs/ABigFishTV) 
- [各种视频播放所需要的功能集](https://github.com/yangchong211/YCVideoPlayer) 
- [后期项目怎么优化](https://github.com/yangchong211/LifeHelper)
- [QSVideoPlayer](https://github.com/tohodog/QSVideoPlayer) 

## 功能简介
- 切换播放器: 默认播放器(基于android自带的MediaPlayer), 基于ijkplayer的播放器
- ~~[Bug]进度条可以预览视频(该功能有bug): 实现方法比较另类, 用Glide加载, 某些格式的视频需要能解码的库; 窗口暂时不能移动~~
- ~~[Bug]斗鱼直播源暂时不会逆向获得, 故使用了一个官方赛事MSI 4000分辨率的一个源替代来演示~~
- ~~[Bug]斗鱼更多游戏频道界面点击没有效果, 因为暂时无法解决fragment覆盖及销毁和逻辑的问题~~

## 依赖框架和技术
MVP, retrofit2, rxjava2

## 实现简介
- MainActivity: 
- DefaultPlayActivity: 使用handler作为异步机制, 使用android自带的视频库播放视频; 
- 


## Todo
- [ ] todo: Fragment实现本地视频，和电视直播，斗鱼直播等功能
- [ ] navitationdrawler添加切换player的菜单
- [ ] 实现沉浸式状态栏: [参考1](https://www.jianshu.com/p/dc20e98b9a90) [参考2](https://blog.csdn.net/u013647382/article/details/51603141) 
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
- [ ]悬浮窗播放: 赋予权限 [参考](https://github.com/duqian291902259/Android-FloatWindow) 
- [ ]记录视频播放记录
- [ ]混淆编译
- [ ]添加自动测试模块
- [ ]ExoPlayer, vitamio:[部署参考](https://github.com/tainzhi/QDouyu/tree/v0.1), ijkplayer提供切换接口
- [ ]搭建一个局域网服务器, 提供全国视频爬取的链接
- [ ]搭建一个局域网服务器, 提供美剧tab的搜索和展示
- [ ]升级RxJava1.x到RxJava2.x
- [ ]kotlin实现
- [ ]学习竞品: MX播放器, XPlayer, VLC, VideoPlayer等播放列表, 播放界面(全屏透明状态栏)等功能, 实现它们
- [ ]学习Douyu新版, 看有什么想实现的功能; 如果能逆向出直播源就更好了




