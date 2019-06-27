# UpdateAppUtils

 [ ![](https://img.shields.io/badge/platform-android-green.svg) ](http://developer.android.com/index.html) 
### 一行代码，快速实现app在线下载更新  A simple library for Android update app

## 集成
compile引入
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

#### Kotlin代码调用完全一样
```
   private fun update() {
        val apkPath:String = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk"

        UpdateAppUtils.from(this)
                .serverVersionCode(2)
                .serverVersionName("2.0")
                .apkPath(apkPath)
                .update()
    }

```

#### 更多配置使用
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

#### 说明
```
    1、UpdateAppUtils提供两种更新判断方式

    CHECK_BY_VERSION_CODE:通过versionCode判断，服务器上versionCode > 本地versionCode则执行更新

    CHECK_BY_VERSION_NAME：通过versionName判断，服务器上versionName 与 本地versionName不同则更新

    2、UpdateAppUtils提供两种下载apk方式

    DOWNLOAD_BY_APP：通过App下载

    DOWNLOAD_BY_BROWSER：通过手机浏览器下载

```

#### UpdateConfig：更新配置

| 属性                  | 描述                               | 默认值 |
|:--------------------- |:------------------------------------ |:------ |
| isDebug               | 是否输出【UpdateAppUtils】为Tag的日志|  true |
| force                 | 是否强制更新                         | false  |
| apkSavePath           | apk下载存放位置               | 包名目录    |
| apkSaveName           | apk保存文件名                 | 项目名        |
| downloadBy            | 下载方式              | DownLoadBy.APP   |
| needCheckMd5          | 是否需要校验apk签名md5              | false   |
| checkWifi             | 检查是否wifi        | false   |
| isShowNotification    | 是否显示通知栏进度    | true   |
| notifyImgRes          | 通知栏图标              | 项目icon  |
| serverVersionName     | 服务器上apk版本名 | 无   |
| serverVersionCode     | 服务器上apk版本号 | 无   |

#### 更新日志
##### 2.0.0
* Kotlin重构
* 支持AndroidX
* 安装包签名文件md5校验
* 通知栏自定义图标
* 支持自定义UI
* 适配中英文
* 增加下载回调等api
* 修复部分bug
