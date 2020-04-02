# UpdateAppUtils2.0

 [ ![](https://img.shields.io/badge/platform-android-green.svg) ](http://developer.android.com/index.html) 
 [ ![Download](https://api.bintray.com/packages/teprinciple/maven/updateapputils/images/download.svg) ](https://bintray.com/teprinciple/maven/updateapputils/_latestVersion)

### 一行代码，快速实现app在线下载更新  A simple library for Android update app

#### UpdateAppUtils2.0 特点
* Kotlin First，Kotlin开发
* 支持AndroidX
* 支持Md5签名验证
* 支持自定义任意UI
* 适配中英文
* 适配至Android 10
* 通知栏图片自定义
* 支持修改是否每次显示弹窗（非强更）
* 支持安装完成后自动删除安装包

UpdateAppUtils2.0功能结构变化巨大，建议使用2.0以上版本；[2.0以前版本文档](https://github.com/teprinciple/UpdateAppUtils/blob/master/readme/README_1.5.2.md)

#### 效果图
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_simple.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_plentiful.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_change.png" width="285">
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_custom.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_downloading.png" width="285"> <img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/update_ui_fail.png" width="285">

### 集成

```
repositories {
   jcenter()
}
```

Support
```
implementation 'com.teprinciple:updateapputils:2.3.0'
```

AndroidX项目
```
注意，由于操作失误bintray 中updateapputilsX被我删掉，
所以2.3.0以后使用updateapputilsx。之前的仍使用updateapputilsX
//implementation 'com.teprinciple:updateapputilsX:2.2.1'
implementation 'com.teprinciple:updateapputilsx:2.3.0'

```

### 使用
下面为kotlin使用示例，Java示例请参考[JavaDemo](https://github.com/teprinciple/UpdateAppUtils/blob/master/app/src/main/java/com/example/teprinciple/updateappdemo/JavaDemoActivity.java)
#### 1、快速使用

##### 注意：部分手机SDK内部初始化不了context，造成context空指针，建议在application或者使用SDK前先初始化
```
 UpdateAppUtils.init(context)
```

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
        .setUpdateDownloadListener(object : UpdateDownloadListener{
            // do something
        })
        .update()
```
#### 3、md5校验说明
 为了保证app的安全性，避免apk被二次打包的风险。UpdateAppUtils内部提供了对签名文件md5值校验的接口；
 首先你需要保证当前应用和服务器apk使用同一个签名文件进行了签名（一定要保管好自己的签名文件，否则检验就失去了意义），
 然后你需要将UpdateConfig 的 needCheckMd5 设置为true，并在Md5CheckResultListener监听中，监听校验返回结果。具体使用可参考
 [CheckMd5DemoActivity](https://github.com/teprinciple/UpdateAppUtils/blob/master/app/src/main/java/com/example/teprinciple/updateappdemo/CheckMd5DemoActivity.kt)
 ```
 UpdateAppUtils
        .getInstance()
        .apkUrl(apkUrl)
        .updateTitle(updateTitle)
        .updateContent(updateContent)
        .updateConfig(updateConfig) // needCheckMd5设置为true
        .setMd5CheckResultListener(object : Md5CheckResultListener{
            override fun onResult(result: Boolean) {
                // true：检验通过，false：检验失败
            }
        })
 ```

#### 4、自定义UI
 UpdateAppUtils内置了两套UI，你可以通过修改[UiConfig](#UiConfig)进行UI内容的自定义；
 当然当内部UI模板与你期望UI差别很大时，你可以采用[完全自定义UI](https://github.com/teprinciple/UpdateAppUtils/blob/master/readme/%E8%87%AA%E5%AE%9A%E4%B9%89UI.md)

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
| fun deleteInstalledApk() | 删除已安装的apk | -  | false  |
| fun setCancelBtnClickListener() | 暂不更新按钮点击监听 | -  | false  |
| fun setUpdateBtnClickListener() | 立即更新按钮点击监听 | -  | false  |

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
| alwaysShowDownLoadDialog| 是否需要显示更新下载进度弹窗（非强更） | false  |
| showDownloadingToast  | 开始下载时是否显示Toast | true  |

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
| downloadingToastText  | 开始下载时的Toast提示文字 | 更新下载中...   |
| downloadingBtnText    | 下载中 下载按钮以及通知栏标题前缀，进度自动拼接在后面 | 下载中   |
| downloadFailText      | 下载出错时，下载按钮及通知栏标题 | 下载出错，点击重试   |

### Demo体验
<img src="https://github.com/teprinciple/UpdateAppUtils/blob/master/img/demo.png" width="220">

### 更新日志

#### 2.3.0
* 修复部分手机context空指针异常
##### [更多历史版本](https://github.com/teprinciple/UpdateAppUtils/blob/master/readme/version.md)