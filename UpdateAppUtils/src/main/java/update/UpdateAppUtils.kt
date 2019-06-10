package update

import android.content.Context
import model.UpdateConfig
import ui.UpdateAppActivity


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
        return this
    }

    /**
     * 是否强制更新
     */
    fun isForce(isForce: Boolean): UpdateAppUtils {
        updateConfig.force = isForce
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
    fun update(context: Context) {
        toUpdate(context)
    }


    /**
     * 更新
     */
    private fun toUpdate(context: Context) {
        UpdateAppActivity.launch(context, updateConfig)
    }


    @JvmStatic
    fun getInstance(): UpdateAppUtils {
        return this
    }
}