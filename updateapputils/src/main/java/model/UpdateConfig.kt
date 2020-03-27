package model

import constant.DownLoadBy

data class UpdateConfig(
    var isDebug: Boolean = true, // 是否是调试模式，调试模式会输出日志

    var alwaysShow: Boolean = true, // 非强制更新时，是否每次都显示弹窗，用VersionName来判断
    var thisTimeShow: Boolean = false, // 非强制更新时，指定本次显示弹窗
    var alwaysShowDownLoadDialog: Boolean = false, // 非强制更新时，也显示下载进度dialog
    var force: Boolean = false, // 是否强制更新
    var apkSavePath: String = "", // apk下载存放位置
    var apkSaveName: String = "", // apk 保存名（默认是app的名字）
    var downloadBy: Int = DownLoadBy.APP, // 下载方式：默认app下载
    //var downloadDirect: Boolean = false, // 不需要弹窗，直接开始下载安装
    var checkWifi: Boolean = false, // 是否检查是否wifi
    var isShowNotification: Boolean = true, // 是否在通知栏显示
    var notifyImgRes: Int = 0, // 通知栏图标
    var needCheckMd5: Boolean = false, // 是否需要进行md5校验，仅app下载方式有效
    var showDownloadingToast: Boolean = true, // 是否需要显示 【更新下载中】文案
    var serverVersionName: String = "", // 服务器上版本名
    var serverVersionCode: Int = 0 // 服务器上版本号
)