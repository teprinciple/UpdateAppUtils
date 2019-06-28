# UpdateAppUtils2.0

 [ ![](https://img.shields.io/badge/platform-android-green.svg) ](http://developer.android.com/index.html) 
### 一行代码，快速实现app在线下载更新  A simple library for Android app update

#### UpdateAppUtils2.0 特点
* Kotlin First，Kotlin开发
* 支持AndroidX
* 支持Md5签名验证
* 支持自定义UI
* 适配中英文
* 适配至Android9.0
* 通知栏图片自定义

#### 效果图

<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_simple.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_plentiful.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_force.png" width="285">
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_change.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_change.png" width="285">

### 集成
```
repositories {
   jcenter()    
}

// Support
implementation 'com.teprinciple:updateapputils:2.0.0'

// AndroidX
implementation 'com.teprinciple:updateapputilsX:2.0.0'
```

### 使用
下面为kotlin使用示例，Java示例请参考[JavaDemo](https://github.com/teprinciple/UpdateAppUtils/blob/master/app/src/main/java/com/example/teprinciple/updateappdemo/JavaDemoActivity.java)
#### 1、快速使用
```
 UpdateAppUtils
        .getInstance()
        .apkUrl(apkUrl)
        .updateTitle(updateTitle)
        .updateContent(updateContent)
        .update()
```

#### 2、多配置使用
```
    // ui配置
    val uiConfig = UiConfig().apply {
        uiType = UiType.PLENTIFUL
        cancelBtnText = "下次再说"
        updateLogoImgRes = R.drawable.ic_update
        updateBtnBgRes = R.drawable.bg_btn
        titleTextColor = Color.BLACK
        titleTextSize = 18f
        contentTextColor = Color.parseColor("#88e16531")
    }

    // 更新配置
    val updateConfig = UpdateConfig().apply {
        force = true
        checkWifi = true
        needCheckMd5 = true
        isShowNotification = true
        notifyImgRes = R.drawable.ic_logo
        apkSavePath = Environment.getExternalStorageDirectory().absolutePath +"/teprinciple"
        apkSaveName = "teprinciple"
    }

    UpdateAppUtils
        .getInstance()
        .apkUrl(apkUrl)
        .updateTitle(updateTitle)
        .updateContent(updateContent)
        .updateConfig(updateConfig)
        .uiConfig(uiConfig)
        .setMd5CheckResultListener(object : Md5CheckResultListener{
            override fun onResult(result: Boolean) {
                // do something
            }
        })
        .setUpdateDownloadListener(object : UpdateDownloadListener{
            override fun onStart() {
            }

            override fun onDownload(progress: Int) {
            }

            override fun onFinish() {
            }

            override fun onError(e: Throwable) {
            }
        })
        .update()
```
#### 3、md5校验说明

#### 4、自定义UI

##### 1、UpdateAppUtils内置了两套UI，你可以通过修改[UiConfig](#UiConfig)进行



### Api说明
#### 1、UpdateAppUtils Api

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

#### 2、UpdateConfig：更新配置说明

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

#### 3、UiConfig：更新弹窗Ui配置说明 <div id = "UiConfig"/>

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

### 更新日志
#### 2.0.0
* Kotlin重构
* 支持AndroidX
* 安装包签名文件md5校验
* 通知栏自定义图标
* 支持自定义UI
* 适配中英文
* 增加下载回调等api
* 修复部分bug