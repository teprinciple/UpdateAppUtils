package update

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

import ui.UpdateAppActivity
import extension.TAG
import model.UpdateConfig


/**
 * Created by Teprinciple on 2016/11/15.
 */
object UpdateAppUtils {

    // 将所有的 属性 放入model
    private val updateConfig = UpdateConfig()

    /**
     * apk 下载路径
     */
    fun apkPath(apkPath: String): UpdateAppUtils {
        updateConfig.apkPath = apkPath
        return this
    }

    /**
     * @param downloadBy 下载方式
     */
    fun downloadBy(downloadBy: Int): UpdateAppUtils {
        updateConfig.downloadBy = downloadBy
        return this
    }

    /**
     * 是否将下载进度显示到通知栏
     */
    fun showNotification(showNotification: Boolean): UpdateAppUtils {
        updateConfig.isShowNotification = showNotification
        //this.showNotification = showNotification
        return this
    }

    /**
     * 是否强制更新
     */
    fun isForce(isForce: Boolean): UpdateAppUtils {
        updateConfig.force = isForce
        return this
    }


    /**
     *@param checkBy 检查更新方式
     *
     */
    fun checkBy(checkBy: Int): UpdateAppUtils {
        updateConfig.checkBy = checkBy
        return this
    }


    fun updateInfo(updateInfo: String): UpdateAppUtils {
        updateConfig.updateInfo = updateInfo
        return this
    }


    fun serverVersionCode(serverVersionCode: Int): UpdateAppUtils {
        updateConfig.serverVersionCode = serverVersionCode
        return this
    }

    fun serverVersionName(serverVersionName: String): UpdateAppUtils {
        updateConfig.serverVersionName = serverVersionName
        return this
    }




    /**
     * 检查更新
     */
    fun update(activity: Activity) {

        when (updateConfig.checkBy) {
            CHECK_BY_VERSION_CODE -> if (updateConfig.serverVersionCode > updateConfig.localVersionCode) {
                toUpdate(activity)
            } else {
                Log.i(TAG, "当前版本是最新版本" + updateConfig.serverVersionCode + "/" + updateConfig.serverVersionName)
            }

            CHECK_BY_VERSION_NAME -> if (updateConfig.serverVersionName != updateConfig.localVersionName) {
                toUpdate(activity)
            } else {
                Log.i(TAG, "当前版本是最新版本" + updateConfig.serverVersionCode + "/" + updateConfig.serverVersionName)
            }
        }
    }

    /**
     * 更新
     */
    private fun toUpdate(activity: Activity) {
        UpdateAppActivity.launch(activity, updateConfig)
    }


    val CHECK_BY_VERSION_NAME = 1001
    val CHECK_BY_VERSION_CODE = 1002
    val DOWNLOAD_BY_APP = 1003
    val DOWNLOAD_BY_BROWSER = 1004
    var showNotification = true
}