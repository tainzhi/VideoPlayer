<!-- vim-markdown-toc GFM -->
* [Info](#info)
* [测试驱动开发](#测试驱动开发)
* [module模块介绍](#module模块介绍)
    - [QMediaSpider](#qmediaspider)
* [实现的功能](#实现的功能)
* [依赖框架和技术](#依赖框架和技术)
* [资源获取方法](#资源获取方法)
    - [各大卫视网络源和图标](#各大卫视网络源和图标)
    - [音乐资源获取](#音乐资源获取)
    - [斗鱼直播](#斗鱼直播)
        + [抓取到房间号后,通过real-url获取直播链接](#抓取到房间号后通过real-url获取直播链接)
        + [手动获取](#手动获取)
        + [python破解方式参考(失效)](#python破解方式参考失效)
* [Todo](#todo)
* [参考](#参考)

<!-- vim-markdown-toc -->
## Info
<img src="./art/demo.gif" alt="项目demo" height="300">

这是一个视频播放器，又不仅仅是一个播放器。它不仅能观看本地视频，还能看斗鱼直播， 能看央视和各大地方卫士，能看美剧和电影。许多功能正在紧张的开发中。。。敬请期待


## 测试驱动开发
- 先完成功能模块: 读取卫视列表到database, test成功
- SurfaceView
- TextureView
- GLSurfaceView: effect, 截屏

## module模块介绍
### QMediaPlayer
- Surface/TextureView/GLSurface
- Android MediaPlayer/IjkPlayer/exoplayer, 特效, 水印, 缓存视频

### common
- BaseFragment支持懒加载

### QMediaSpider
- 爬取网络视频资源, 使用到技术有Jsoup, Volley, android test等等.
- 破解算法参考[real-url](https://github.com/wbt5/real-url)
- DouyuSpider

### ffmpeg
- 此module封装了[ffmpeg-4.3](http://ffmpeg.org/download.html#release_4.3), 用ndk20b编译
- 该module可以执行ffmpeg命令, 在cpp线程中执行, 提供了回调,命令执行进度,命令停止功能
- 编译过程和使用参考[ffmpeg/README.md](./ffmpeg/README.md)
- ffmpeg还支持了rtmp和hls推送

### danmmu
- 弹幕库, 分为简单版和复杂版
- 复杂版弹幕库有缓冲池功能, 性能更好
[详情参见](./danmu/README.md)

## 实现的功能
- PlayVideoViewActivity: VideoView+MediaController简单播放功能

## 依赖框架和技术
- javaMVP(第一版): MVP, retrofit2, rxjava2等等
- master(当前开发版): kotlin, MVVM(google jetpack), jsoup, ijkplayer,
  mediaplayer, exoplayer, koin, retrofit, okhttp

| 库 | 功能 |
| :----- | :--------- |
| [lottie](https://github.com/airbnb/lottie-android) | 动画 |
| [Jsoup](https://jsoup.org/) | 网页解析(爬虫) |
| [moshi](https://github.com/square/moshi/) | 替换Gson为moshi |
| [NotchScreenTool](https://github.com/smarxpan/NotchScreenTool) | 刘海屏全面屏工具,全屏播放时使用刘海 |
| [弃用]Volley,使用简单封装的HttpUrlConnection | 网络请求 |

## 资源获取方法

### 各大卫视网络源和图标
- [更多卫视频道, 也要全球iptv频道](https://github.com/iptv-org/iptv)
- [gitee: tv-source](https://gitee.com/forterli/project/tree/master/mini_program/tv_video/data): 从tv-source获取的tv卫视图片资源链接是http的, 而且有些图片不存在
- 卫视名称,图片,网络原地址从tv-source拷贝json文件, 并做了部分修改放置在assets/tv_circuits.json中,app启动创建数据后加载进数据库
- 各大卫视当前播放节目从[电视猫](https://www.tvmao.com/program/playing/cctv/)爬虫所得
- [电视之家:分享高清电视直播源](https://dtvzj.com/274.html)
- [电视节目单](http://epg.tv.cn/cctv1): 脚本生成的当前网页, 不太好抓取当前播放内容
- [北邮: 卫视高清资源](http://ivi.bupt.edu.cn/)
- [多条卫视线路, 其他电影线路多已失效](http://www.dszbdq.cn/)

### 音乐资源获取
- 参考[多媒体app](https://github.com/guofudong/KotlinAndroid),使用了获取[QQ和网易音乐的接口](https://www.bzqll.com/)
- [调调](https://www.diodio.cc/)

### 斗鱼直播
[douyu开发者平台:3月公测](https://open.douyu.com/), 不开放给个人开发者

一些斗鱼api
- [获取游戏分类](http://m.douyu.com/api/cate/list), [获取游戏分类2](http://open.douyucdn.cn/api/RoomApi/game)
- [获取某个游戏的直播间: http://open.douyucdn.cn/api/RoomApi/live/3?limit=20&offset=20](http://open.douyucdn.cn/api/RoomApi/live/3?limit=20&offset=20), 3为游戏id, limit每次返回房间数, offset为偏移量
- [推荐的游戏:http://open.douyucdn.cn/api/RoomApi/live?limit=20&offset=20](http://open.douyucdn.cn/api/RoomApi/live?limit=20&offset=20)
- [http://m.douyu.com/api/home/mix](http://m.douyu.com/api/home/mix)

#### 抓取到房间号后,通过[real-url](https://github.com/wbt5/real-url)获取直播链接
- 怎么通过chrome的js调试器, 下断点, 获取直播地址:
- 具体方法: 先找到返回源地址的POST或GET操作，观察其传入的参数，再到JS中搜索并找到相关代码块，随后通过下断点，一步步调试即可

#### 手动获取
斗鱼原先提供的v1可以直接获取直播地址的api已经失效，暂时无法获取地址。Demo中使用了一个24h直播间https://www.douyu.com/3346305，手动获取地址，来测试。
 [斗鱼直播源破解参考](https://www.52pojie.cn/thread-957638-1-1.html)
 
具体破解步骤：
- 假设原直播源地址为`http://hlsa.douyucdn.cn/live/431935rYIJ0kKhQ4_550/playlist.m3u8?wsSecret=924a83c6700d9d802a7717f1068811f6&wsTime=1558565155&token=h5-douyu-0-431935-373ef2a3e162f552b55145ccdd4571a3&did=h5_did`
- 直播源地址格式固定为`http://hlsa.douyucdn.cn/live/*_550/playlist/*`. 
- 替换hlsa为tx2play1, _550为普通清晰度，去除改为默认最高清晰度
  - 最终结果`http://tx2play1.douyucdn.cn/live/431935rYIJ0kKhQ4.flv`
          主线路: http://hdls1a.douyucdn.cn/live/9999rhwySJw8y5P6_1200p.flv
          线路5: http://tc-tct.douyucdn2.cn/dyliveflv1a/431935rgDRxvP0IV.flv
          // val roomCircuitId = mViewModel.getRoomCircuit(room.room_id.toString())
          // val circuit = "http://tx2play1.douyucdn.cn/live/${roomCircuitId}_550.flv"
- https://tc-tct.douyucdn2.cn/dyliveflv1a/431935rgDRxvP0IV.flv
- http://hdls1a.douyucdn.cn/live/431935rgDRxvP0IV_550.flv
https和http的区别
- tx2play1和hlds1a的区别

#### python破解方式参考(失效)
- [youtube-dl](https://github.com/ytdl-org/youtube-dl/blob/c3bcd206eb031de30179c88ac7acd806a477ceae/youtube_dl/extractor/douyutv.py)
- [you-get](https://github.com/soimort/you-get/blob/0811aed29c528e1f5994c81f126b05afbf146dd2/src/you_get/extractors/douyutv.py)
- [streamlink](https://github.com/streamlink/streamlink/blob/fb6a00c86bbf752b578ea317cdede242a2b4e836/src/streamlink/plugins/douyutv.py)

#### 电影资源获取
- [参考zy-player](https://github.com/vicedev/ZY-Player-Android)


## Todo
- [ ] 参考GSYVideoPlayer的GSYVideoGLViewCustomRender实现水印效果
- [] [Easy Permissions](https://github.com/googlesamples/easypermissions)
- [] [PermissionX](https://github.com/guolindev/PermissionX)
- [] [使用composing build, 而不是buildSrc](https://juejin.im/post/5ed3ef906fb9a047bf7070b6#heading-6)
- [] [manage network usage](https://developer.android.google.cn/training/basics/network-ops/managing))
- [] 使用Paging
- [] [detekt](https://github.com/detekt/detekt): 代码检测和kotlin编码规范
- [] [detek添加hook](https://blog.csdn.net/qq_23191031/article/details/107287168)
- [ ] motionlayout实现酷炫的动画
- [ ] 电影界面通过[flexbox-layout](https://github.com/google/flexbox-layout)实现酷炫的效果
- [x] Fab播放按钮, 播放最近观看记录; 使用Snackbar替换Toast,[参考1](https://stackoverflow.com/questions/36332487/move-snackbar-above-the-bottom-bar), [2](https://stackoverflow.com/questions/56040954/how-to-display-snackbar-above-bottomnavigationview-move-fab)
- [ ] 添加广告,gif截图
- [ ] 本地视频列表缩略实现, 参考我的印象笔记相同缩略图笔记内容
- [ ] 竖屏视频播放(通过CotentProvider查询到的orientation方向null, 但是resolution=1080*1920, 添加手动判断横屏或者竖屏)]
- [ ] 本地列表第一个是最近的播放记录, 记录播放位置, 并显示上次播放到的时间点的缩略图
- [ ] 实现沉浸式状态栏: [参考1](https://www.jianshu.com/p/dc20e98b9a90) [参考2](https://blog.csdn.net/u013647382/article/details/51603141) 
- [ ] 申请权限管理
- [ ] 参考[WanAndroid todo](https://github.com/tainzhi/WanAndroid)
- [ ] 进度条+小窗显示
- [ ] 悬浮窗播放: 赋予权限 [参考](https://github.com/duqian291902259/Android-FloatWindow) 
- [ ] 记录视频播放记录
- [ ] 混淆编译
- [ ] 添加自动测试模块
- [ ] 搭建一个局域网服务器, 类似小米阅读通过wifi从电脑传书到手机, [NanoHttp](https://blog.csdn.net/small_and_smallworld/article/details/103393070?utm_medium=distribute.pc_relevant.none-task-blog-baidujs-1), [AndServer](https://blog.csdn.net/yanzhenjie1003/article/details/51661599)
- [ ] 学习竞品: MX播放器, XPlayer, VLC, VideoPlayer等播放列表, 播放界面(全屏透明状态栏)等功能, 实现它们
- [ ] 学习Douyu新版, 看有什么想实现的功能; 如果能逆向出直播源就更好了
- [ ] 学习竞品: 小米视频

## buildSrc.AutoUpload插件的使用
- `./gradlew autoupload`自动编译上传到蒲公英
- 但是需要添加pgy渠道

## github action的使用
- `git tag -a v0.1.0`新建tag后, `git push origin v0.1.0`推送tag到远程后触发

## 参考
- [iToutiao相关设计也实现](https://github.com/iMeiji/Toutiao)
- [影视天地（Android 客户端），涵盖电影、电视剧、综艺节目、动漫、游戏五大类别的资源。整合两大资源搜索引擎，连接互联网千万数量级的资源库。支持视频资源在线播放、边下边播，不限速下载，不等待播放](https://github.com/123lxw123/VideoWorld_Android)
- [abigfishtv ios 大鱼电视直播 基于ijkplayer的播放器 700多个电视台 包括央视，各地方台，卫视，熊猫直播，社会化分享，登陆，仿微博等 (支持iphonex)](https://github.com/clyhs/ABigFishTV)
- [各种视频播放所需要的功能集](https://github.com/yangchong211/YCVideoPlayer)
- [弹弹play 概念版，弹弹play系列应用安卓平台上的实现，是一个提供了视频播放（本地+局域网）和弹幕加载（在线+本地）功能的本地播放器](https://github.com/xyoye/DanDanPlayForAndroid)
- [后期项目怎么优化](https://github.com/yangchong211/LifeHelper)
- [QSVideoPlayer](https://github.com/tohodog/QSVideoPlayer)
- [支持软硬解码](https://www.jianshu.com/p/93b7f7ec2d04)
- 其他的videoplayer:
  [1](https://github.com/danylovolokh/VideoPlayerManager),
  [2](https://github.com/blizzard-op/VideoPlayerKit),
  [3](https://github.com/macdonst/VideoPlayer),
  [4](https://github.com/ihmpavel/expo-video-player),
  [5](https://github.com/boredream/VideoPlayer),
  [6](https://github.com/Zhaoss/VideoPlayerDemo),
  [7](https://github.com/googlesamples/android-VideoPlayer)
- 怎么升级
>- [bugly](https://bugly.qq.com/docs/user-guide/instruction-manual-android-upgrade/?v=20200622202242): 没有下载次数限制; 继承方便, 提供升级页面; 不用验证身份证; 唯一缺点, 没有提供api, 无法自动上传apk
>- [蒲公英分发平台](https://www.pgyer.com/doc/view/api): 提供了api, 可以编写gradle插件自动上传; 缺点是需要身份证验证, 有下载次数限制

[ffmpeg]: #ffmpeg

