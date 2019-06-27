# UpdateAppUtils

 [ ![](https://img.shields.io/badge/platform-android-green.svg) ](http://developer.android.com/index.html) 
### 一行代码，快速实现app在线下载更新  A simple library for Android update app

## 集成
```
repositories {
   jcenter()    
}

dependencies {
    implementation 'com.teprinciple:updateapputils:1.5.2'
}
```

## 使用
更新检测一般放在MainActivity或者启动页上，
在请求服务器版本检测接口获取到versionCode、versionName、最新apkPath后调用。

#### 快速使用
```
 UpdateAppUtils.from(this)
                .serverVersionCode(2)  //服务器versionCode
                .serverVersionName("2.0") //服务器versionName
                .apkPath(apkPath) //最新apk下载地址
                .update();
```

#### 多配置使用
```
UpdateAppUtils.from(this)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                .serverVersionCode(2)
                .serverVersionName("2.0")
                .apkPath(apkPath)
                .showNotification(false) //是否显示下载进度到通知栏，默认为true
                .updateInfo(info)  //更新日志信息 String
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_BROWSER) //下载方式：app下载、手机浏览器下载。默认app下载
                .isForce(true) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                .update();
```
#### md5校验说明

#### UI配置

#### 自定义UI

## Api说明
#### UpdateAppUtils全部Api

| api             | 说明                               | 默认值                   | 必须设置 |
|:-------------- |:------------------------------------ |:--------------------- |:------ |
| fun apkUrl(apkUrl: String)| 更新包服务器url         | null                  | true   |
| fun update() | UpdateAppUtils入口      | -     | true   |
| fun updateTitle(title: String)         | 更新标题     | 版本更新啦！     | false   |
| fun updateContent(content: String)        | 更新内容   | 发现新版本，立即更新  | false   |
| fun updateConfig(config: UpdateConfig)   | 更新配置  | 查看源码 | false  |
| fun uiConfig(uiConfig: UiConfig) | 更新弹窗UI配置  | 查看源码                 | false  |
| fun setUpdateDownloadListener(listener: UpdateDownloadListener) | 下载过程监听  | null | false   |
| fun setMd5CheckResultListener(listener: Md5CheckResultListener) | md5校验结果回调 | null  | false  |


#### UpdateConfig：更新配置说明

| 属性                  | 说明                               | 默认值 |
|:--------------------- |:------------------------------------ |:------ |
| isDebug               | 是否输出【UpdateAppUtils】为Tag的日志|  true |
| force                 | 是否强制更新，强制时无取消按钮       | false  |
| apkSavePath           | apk下载存放位置               | 包名目录    |
| apkSaveName           | apk保存文件名                 | 项目名        |
| downloadBy            | 下载方式              | DownLoadBy.APP   |
| needCheckMd5          | 是否需要校验apk签名md5              | false   |
| checkWifi             | 检查是否wifi        | false   |
| isShowNotification    | 是否显示通知栏进度    | true   |
| notifyImgRes          | 通知栏图标              | 项目icon  |
| serverVersionName     | 服务器上apk版本名 | 无   |
| serverVersionCode     | 服务器上apk版本号 | 无   |
| customLayoutId        | 自定义更新弹窗布局id | 无   |

#### UiConfig：更新弹窗Ui配置说明

| 属性                  | 说明                               | 默认值 |
|:--------------------- |:------------------------------------ |:------ |
| uiType                | ui模板                        | UiType.SIMPLE |
| customLayoutId        | 自定义布局id    | false  |
| updateLogoImgRes      | 更新弹窗logo图片资源id               | -    |
| titleTextSize         | 标题字体大小                 | 16sp        |
| titleTextColor        | 标题字体颜色              | -   |
| contentTextSize         | 内容字体大小                 | 14sp       |
| contentTextColor        | 内容字体颜色              | -  |
| updateBtnBgColor      | 更新按钮背景颜色              | -   |
| updateBtnBgRes        | 更新按钮背景资源id        | -   |
| updateBtnTextColor    | 更新按钮字体颜色    | -   |
| updateBtnTextSize     | 更新按钮文字大小 | 14sp   |
| updateBtnText         | 更新按钮文案              | 立即更新  |
| cancelBtnBgColor      | 取消按钮背景颜色 | -   |
| cancelBtnBgRes        | 取消按钮背景资源id | -   |
| cancelBtnTextColor    | 取消按钮文字颜色 | -   |
| cancelBtnTextSize     | 取消按钮文字大小 | 14sp   |
| cancelBtnText    | 取消按钮文案 | 暂不更新   |

## 更新日志
#### 2.0.0
* Kotlin重构
* 支持AndroidX
* 安装包签名文件md5校验
* 通知栏自定义图标
* 支持自定义UI
* 适配中英文
* 增加下载回调等api
* 修复部分bug