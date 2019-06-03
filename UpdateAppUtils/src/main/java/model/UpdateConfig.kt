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
    var updateInfo: String? = "", // 更新说明
    var force: Boolean? = false, // 是否强制更新
    var serverVersionName: String? = "", // 服务器上版本名
    var serverVersionCode: Int = 0, // 服务器上版本号
    var localVersionName: String? = "", // 当前本地版本名
    var localVersionCode: Int = 0,// 当前本地版本号
    var checkBy: Int = UpdateAppUtils.CHECK_BY_VERSION_CODE,// 检查方式 按版本名或版本号
    var isShowNotification: Boolean = true // 是否在通知栏显示
): Parcelable