# UpdateAppUtils2.0

 [ ![](https://img.shields.io/badge/platform-android-green.svg) ](http://developer.android.com/index.html) 
### 一行代码，快速实现app在线下载更新  A simple library for Android update app

#### UpdateAppUtils2.0 特点
* Kotlin First，Kotlin开发
* 支持AndroidX
* 支持Md5签名验证
* 支持自定义任意UI
* 适配中英文
* 适配至Android9.0
* 通知栏图片自定义
* 支持修改是否每次显示弹窗（非强更）

#### 效果图
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_simple.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_plentiful.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_change.png" width="285">
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_custom.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_downloading.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_fail.png" width="285">

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
            // do something
        })
        .update()
```
#### 3、md5校验说明


#### 4、自定义UI
##### UpdateAppUtils内置了两套UI，你可以通过修改[UiConfig](#UiConfig)进行UI内容的自定义；当然当内部UI模板与你期望UI差别很大时，你可以
采用[完全自定义UI]()

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
| fun setUpdateDownloadListener() | 下载过程监听  | null | false   |
| fun setMd5CheckResultListener() | md5校验结果回调 | null  | false  |
| fun setOnInitUiListener() | 初始化更新弹窗UI回调 | null  | false  |

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
| alwaysShow            | 是否每次显示更新弹窗（非强更） | true   |
| thisTimeShow          | 本次是否显示更新弹窗（非强更） | false  |

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
| cancelBtnText         | 取消按钮文案 | 暂不更新   |

### Demo体验

![](data:image/png;base64,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)

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
##### [更多历史版本](https://github.com/teprinciple/UpdateAppUtils/blob/master/readme/version.md)