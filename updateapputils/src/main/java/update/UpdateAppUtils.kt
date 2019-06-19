package update

import android.content.Context
import listener.UpdateDownloadListener
import model.UpdateConfig
import model.UpdateInfo
import ui.UpdateAppActivity


/**
 * Created by Teprinciple on 2016/11/15.
 */
object UpdateAppUtils {

    // 更新信息对象
    internal val updateInfo = UpdateInfo()

    // 下载监听
    internal var listener: UpdateDownloadListener? = null

    /**
     * 设置apk下载地址
     */
    fun apkUrl(apkUrl: String): UpdateAppUtils {
        updateInfo.apkUrl = apkUrl
        return this
    }

    /**
     * 设置更新标题
     */
    fun updateTitle(title: String): UpdateAppUtils {
        updateInfo.updateTitle = title
        return this
    }

    /**
     * 设置更新内容
     */
    fun updateContent(content: String): UpdateAppUtils {
        updateInfo.updateContent = content
        return this
    }

    /**
     * 更改更新配置
     */
    fun changeConfig(config: UpdateConfig): UpdateAppUtils {
        updateInfo.config = config
        return this
    }

    /**
     * 设置下载监听
     */
    fun setUpdateDownloadListener(listener: UpdateDownloadListener) {
        this.listener = listener
    }

    /**
     * 检查更新
     */
    fun update(context: Context) = UpdateAppActivity.launch(context.applicationContext)

    /**
     * 获取单例对象
     */
    @JvmStatic
    fun getInstance() = this
}