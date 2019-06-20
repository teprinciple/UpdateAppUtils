package model

/**
 * desc: UpdateInfo
 * time: 2019/6/18
 * @author yk
 */
internal data class UpdateInfo(
    // 更新标题
    var updateTitle: String = "版本更新啦",
    // 更新内容
    var updateContent: String? = "发现新版本，立即更新",
    // apk 下载地址
    var apkUrl: String = "",
    // 更新配置
    var config: UpdateConfig = UpdateConfig()
)