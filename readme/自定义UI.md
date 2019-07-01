## 完全自定义UI

### 1、创建你的layout（必须）
你可以创建任意你想要的UI布局（[参考 view_update_dialog_custom.xml](https://github.com/teprinciple/UpdateAppUtils/blob/master/app/src/main/res/layout/view_update_dialog_custom.xml)）
，但是控件id需要保持如下：

| id                  | 说明                 |      控件类型        | 是否必须 |
|:--------------------- |:-------------------|:----------------- |:------ |
| btn_update_sure       | 立即更新按钮id| 任意View |true |
| btn_update_cancel     | 暂不更新按钮id| 任意View  |true  |
| tv_update_title     | 更新弹窗标题| TextView |false  |
| tv_update_content     | 更新内容| TextView  |false  |

btn_update_sure和btn_update_cancel是必须提供的，否则更新无法继续；

tv_update_title，tv_update_content提供，UpdateAppUtils内部会自动
设置值，如果你不想这样，也可以自行命名，稍后通过OnInitUiListener接口进行相关文案设置；

### 2、注入到UpdateAppUtils（必须）

通过设置uiConfig，将自定义布局注入到UpdateAppUtils；注意uiType必须为UiType.CUSTOM

```
UpdateAppUtils
    .getInstance()
    // ...
    .uiConfig(UiConfig(uiType = UiType.CUSTOM, customLayoutId = R.layout.view_update_dialog_custom))
    .update()
```

### 3、实现OnInitUiListener接口（非必须）

UpdateAppUtils 中只对上表中的4个控件进行了相关内容的填充，如果你自定义的布局中有其他控件需要进行内容填充
需要实现OnInitUiListener接口来进行操作：
```
UpdateAppUtils
    .getInstance()
    // ...
    .setOnInitUiListener(object : OnInitUiListener {
        override fun onInitUpdateUi(view: View?, updateConfig: UpdateConfig, uiConfig: UiConfig) {
            view?.findViewById<TextView>(R.id.tv_update_title)?.text = "版本更新啦"
            view?.findViewById<TextView>(R.id.tv_version_name)?.text = "V2.0.0"
            // do more...
        }
    })
```