package listener

import android.view.View
import model.UiConfig
import model.UpdateConfig

/**
 * desc: 初始化UI 回调 用于进一步自定义UI
 * time: 2019/6/28
 * @author teprinciple
 */
interface OnInitUiListener {

    /**
     * 初始化更新弹窗回调
     * @param view 弹窗view
     * @param updateConfig 当前更新配置
     * @param uiConfig 当前ui配置
     */
    fun onInitUpdateUi(view: View?, updateConfig: UpdateConfig, uiConfig: UiConfig)
}