# android_common
用于android开发的公共库, 包含BasicFragment, BasicActivity, Utils,
Extensions

# how to use
在项目根目录
```
git submodule add git@github.com:tainzhi/android_common.git common
```
当然最好,
也在根目录下载好[buildSrc](https://github.com/tainzhi/android_buildSrc)

# 添加该common module
- 方法一: 通过Android Studio: New > Import Module, 选择该common
- 方法二: `settings.gradle.kts`添加`include(":common")`
