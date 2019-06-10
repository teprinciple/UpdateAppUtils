package model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import update.UpdateAppUtils

@SuppressLint("ParcelCreator")
@Parcelize
internal data class UpdateConfig(
    var downloadBy: Int = 0, // 下载方式：默认app下载
    var apkPath: String? = "", // apk 下载地址
    var updateInfo: String? = "", // 更新内容
    var updateTitle: String? = "", // 更新标题
    var force: Boolean? = false, // 是否强制更新
    var serverVersionName: String? = "", // 服务器上版本名
    var serverVersionCode: Int = 0, // 服务器上版本号
    var localVersionName: String? = "", // 当前本地版本名
    var localVersionCode: Int = 0,// 当前本地版本号
    var isShowNotification: Boolean = true // 是否在通知栏显示
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