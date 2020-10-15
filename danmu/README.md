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
