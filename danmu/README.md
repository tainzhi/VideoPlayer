## info

弹幕有简单弹幕和高级弹幕

## 简单弹幕 SimpleDanmu
只是一个简单的demo
- 核心思想:
>- 通过属性动画TranslateAnimation对添加到 SimpleDanmuView中的每一条弹幕 SimpleDanmuItemView移动
>- 通过handle和looper不断循环从SimpleDanmuBean的list中取出并创建SimpleDanmuItemView, 添加到SimpleDanmuView
- 用法
```kotlin
val  simpleDanmu = SimpleDanmuView()
simpleDanmu.mData = SimpleUtil.loadData()

// 清除弹幕
simpleDanmu.clear()
```

## 高级弹幕 AdvanceDanmu
- 两种: 基于View的DanmuContainerView和基于SurfaceView的DanmuContainerSurfaceView
- 如果要使单个Danmu能响应点击事件, 需要用DanmuParentView作为DanmuContainerView/DanmuContainerSurfaceView的根布
- DanmuContainerView依赖 Controller, Controller初始化后, 可以添加Danmu
- Controller依赖PoolManager和DanmuDispatcher
- PoolManager依赖Producer和ProducedPool, Consumer和ConsumedPool
- DanmuDispatcher和Channel根据屏幕的高度和默认Danmu的高度, 确定Danmu Channel, 即屏幕上同时可以滑过的Danmu频道
- Producer从ProducedPool中取出Danmu, dispatch到ConsumedPool中, 通过Consumer调用DanmuPainter绘制出来

### 特性
- 支持载入单条Danmu, 多条Danmu
- 响应Damu点击事件
- Danmu滑动速度控制
- Danmu缓冲池
- 从左到右滑动还是从右到左滑动

### 用法
xml中添加到
```xml
	<com.tainzhi.android.danmu.advancedanmu.view.DanmuContainerSurfaceView
		android:id="@+id/danmuContainerView"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		/>
```

添加danmu
```kotlin
// this is activity
val danmuHelper = DanMuHelper(this, danmuContainerView)
danmuHelper.addDanmu(DanmuEntity("https://avatar.jpg", "张三", "00001", 5, 1, "666666666"))

// 隐藏所有的弹幕
danmuHelper.hideAllDanmu()
```


