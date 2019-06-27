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
##### 1.5.2
* 修复部分bug
##### 1.5.1
* 库内部适配至Android8.0
##### 1.4
* 使用[filedownloader](https://github.com/lingochamp/FileDownloader)替换DownloadManager，避免部分手机DownLoadManager无效，同时解决了重复下载的问题，且提高了下载速度
* 增加接口UpdateAppUtils.needFitAndroidN(false)，避免不需要适配7.0，也要设置FileProvider
##### 1.3.1
* 修复部分bug，在demo中加入kotlin调用代码
##### 1.3
* 增加接口方法 showNotification(false) //是否显示下载进度到通知栏；
* updateInfo(info) //更新日志信息；
* 下载前WiFi判断。
##### 1.2
* 适配Android7.0，并在demo中加入适配6.0和7.0的代码
##### 1.1
* 适配更多SdkVersion