### 更新日志
#### 2.2.1
* 优化代码
* 修复部分bug
#### 2.2.0
* 适配Android 10
* 修复部分bug
#### 2.1.0
* 增加'暂不更新'按钮点击监听 setCancelBtnClickListener()
* 增加'立即更新'按钮点击监听 setUpdateBtnClickListener()
* 修复部分bug
#### 2.0.4
* 修复阿里云，码云平台上的apk FileDownloader下载失败
* 增加UpdateConfig alwaysShowDownLoadDialog字段，让非强更也能显示下载进度弹窗
#### 2.0.3
* 更新弹窗内容支持SpannableString
#### 2.0.2
* 9.0Http适配
#### 2.0.1
* 自定义FileProvide，防止provider冲突
#### 2.0.0
* Kotlin重构
* 支持AndroidX
* 安装包签名文件md5校验
* 通知栏自定义图标
* 支持自定义UI
* 适配中英文
* 增加下载回调等api
* 修复部分bug
#### 1.5.2
* 修复部分bug
#### 1.5.1
* 库内部适配至Android8.0
#### 1.4
* 使用[filedownloader](https://github.com/lingochamp/FileDownloader)替换DownloadManager，避免部分手机DownLoadManager无效，同时解决了重复下载的问题，且提高了下载速度
* 增加接口UpdateAppUtils.needFitAndroidN(false)，避免不需要适配7.0，也要设置FileProvider
#### 1.3.1
* 修复部分bug，在demo中加入kotlin调用代码
#### 1.3
* 增加接口方法 showNotification(false) //是否显示下载进度到通知栏；
* updateInfo(info) //更新日志信息；
* 下载前WiFi判断。
#### 1.2
* 适配Android7.0，并在demo中加入适配6.0和7.0的代码
#### 1.1
* 适配更多SdkVersion