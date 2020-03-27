package model

import com.teprinciple.updateapputils.R
import extension.string

/**
 * desc: UpdateInfo
 * time: 2019/6/18
 * @author teprinciple
 */
internal data class UpdateInfo(
    // 更新标题
    var updateTitle: CharSequence = string(R.string.update_title),
    // 更新内容
    var updateContent: CharSequence = string(R.string.update_content),
    // apk 下载地址
    var apkUrl: String = "",
    // 更新配置
    var config: UpdateConfig = UpdateConfig(),
    // ui配置
    var uiConfig: UiConfig = UiConfig()
)