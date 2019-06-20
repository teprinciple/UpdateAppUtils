package model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class UpdateConfig(

    var isDebug: Boolean = false, // 是否是调试模式，调试模式会输出日志

    var alwaysShowTips: Boolean = false, // 非强制更新时，是否每次都显示弹窗，用VersionName来判断？
    var thisTimeShowTips: Boolean = false, // 非强制更新时，指定本次显示弹窗

    var force: Boolean? = false, // 是否强制更新
    var apkSavePath: String = "", // apk下载存放位置
    var apkSaveName: String = "", // apk 保存名（默认是app的名字）
    var downloadBy: Int = DownLoadBy.APP, // 下载方式：默认app下载
    var justDownload: Boolean = false, // 是否只下载apk，不进行安装

    var checkWifi: Boolean = true, // 是否检查是否wifi
    var isShowNotification: Boolean = true, // 是否在通知栏显示
    var notifyImgRes: Int = 0, // 通知栏图标

    var serverVersionName: String = "", // 服务器上版本名
    var serverVersionCode: Int = 0, // 服务器上版本号
    var localVersionName: String? = "", // 当前本地版本名
    var localVersionCode: Int = 0,// 当前本地版本号

    var md5: String = "", // 服务器apk md5
    var needMd5Check: Boolean = false // 是否需要进行md5校验
) : Parcelable