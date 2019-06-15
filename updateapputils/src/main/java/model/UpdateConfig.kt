package model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import update.UpdateAppUtils

@SuppressLint("ParcelCreator")
@Parcelize
internal data class UpdateConfig(

    var updateInfo: String? = "", // 更新内容
    var updateTitle: String = "", // 更新标题
    var force: Boolean? = false, // 是否强制更新

    var serverVersionName: String = "", // 服务器上版本名
    var serverVersionCode: Int = 0, // 服务器上版本号
    var localVersionName: String? = "", // 当前本地版本名
    var localVersionCode: Int = 0,// 当前本地版本号

    var md5: String = "", // 服务器apk md5
    var needMd5Check: Boolean = false, // 是否需要进行md5校验

    var downloadBy: Int = DownLoadBy.APP, // 下载方式：默认app下载
    var apkUrl: String = "", // apk 下载地址
    var apkSavePath: String = "", // apk下载存放位置
    var justDownload: Boolean = false, // 是否只下载apk，不进行安装

    var checkWifi: Boolean = true, // 是否检查是否wifi

    var isShowNotification: Boolean = true, // 是否在通知栏显示
    var notifyImgRes: Int = 0 // 通知栏图标

) : Parcelable

object DownLoadBy {
    /**
     * app下载
     */
    const val APP = 0x101

    /**
     * 浏览器下载
     */
    const val BROWSER = 0x102
}