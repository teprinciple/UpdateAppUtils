package update

import listener.Md5CheckResultListener
import listener.UpdateDownloadListener
import model.UiConfig
import model.UpdateConfig
import model.UpdateInfo
import ui.UpdateAppActivity
import util.GlobalContextProvider


/**
 * Created by Teprinciple on 2016/11/15.
 */
object UpdateAppUtils {

    init {
        GlobalContextProvider.getGlobalContext()
    }

    // 更新信息对象
    internal val updateInfo = UpdateInfo()

    // 下载监听
    internal var listener: UpdateDownloadListener? = null

    // md5校验结果回调
    internal var md5CheckResultListener: Md5CheckResultListener? = null

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
     * 设置更新配置
     */
    fun updateConfig(config: UpdateConfig): UpdateAppUtils {
        updateInfo.config = config
        return this
    }

    /**
     * 设置UI配置
     */
    fun uiConfig(uiConfig: UiConfig): UpdateAppUtils{
        updateInfo.uiConfig = uiConfig
        return this
    }

    /**
     * 设置下载监听
     */
    fun setUpdateDownloadListener(listener: UpdateDownloadListener): UpdateAppUtils {
        this.listener = listener
        return this
    }

    /**
     * 设置md5校验结果回调
     */
    fun setMd5CheckResultListener(listener: Md5CheckResultListener): UpdateAppUtils {
        this.md5CheckResultListener = listener
        return this
    }

    /**
     * 检查更新
     */
    fun update() = UpdateAppActivity.launch()

    /**
     * 获取单例对象
     */
    @JvmStatic
    fun getInstance() = this
}