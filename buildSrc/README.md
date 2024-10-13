# info
gradle.kts管理的android依赖库

## 必须
- gradle version >= 6
- andorid build gradle version >= 4.0

# 使用方法
- 在项目根目录下
```
git submodule add git@github.com:tainzhi/android_buildSrc.git buildSrc
```
- 直接添加使用

# 删除该submodule
- 1. Run `git rm --cached buildSrc`
- 2. Delete the relevant lines from the `.gitmodules` file.
- 4. Delete the now untracked submodule files.`rm  -rf buildSrc`
- 3. Delete the relevant section from `.git/config`.
- 4. Commit
- 5. Remove directory `.git/modules/buildSrc`

# 引入第三方库需要注意的点
### BaseRecyclerViewAdapterHelper
[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki) 需要添加
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
