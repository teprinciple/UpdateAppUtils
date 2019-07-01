package com.example.teprinciple.updateappdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import constacne.DownLoadBy
import constacne.UiType
import kotlinx.android.synthetic.main.activity_main.*
import listener.OnInitUiListener
import listener.UpdateDownloadListener
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {
    private val apkUrl = "http://118.24.148.250:8080/yk/app-release.apk"
    private val updateTitle = "发现新版本V2.0.0"
    private val updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 基本使用
        btn_basic_use.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .uiConfig(UiConfig(uiType = UiType.SIMPLE))
                .updateContent(updateContent)
                .update()
        }

        // 浏览器下载
        btn_download_by_browser.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(UpdateConfig().apply {
                    downloadBy = DownLoadBy.APP
                    // alwaysShow = false
                    serverVersionName = "2.0.0"
                })
                .uiConfig(UiConfig(uiType = UiType.PLENTIFUL))
                .update()
        }

        // 自定义UI
        btn_custom_ui.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .uiConfig(UiConfig(uiType = UiType.CUSTOM, customLayoutId = R.layout.view_update_dialog_custom))
                .setOnInitUiListener(object : OnInitUiListener {
                    override fun onInitUpdateUi(view: View?, updateConfig: UpdateConfig, uiConfig: UiConfig) {
                        view?.findViewById<TextView>(R.id.tv_update_title)?.text = "版本更新啦"
                        view?.findViewById<TextView>(R.id.tv_version_name)?.text = "V2.0.0"
                        // do more...
                    }
                })
                .update()
        }

        // java使用示例
        btn_java_sample.setOnClickListener {
            startActivity(Intent(this, JavaDemoActivity::class.java))
        }

         // md5校验
        btn_java_sample.setOnClickListener {
            startActivity(Intent(this, CheckMd5DemoActivity::class.java))
        }

        // 高级使用
        btn_higher_level_use.setOnClickListener {
            // ui配置
            val uiConfig = UiConfig().apply {
                uiType = UiType.PLENTIFUL
                cancelBtnText = "下次再说"
                updateLogoImgRes = R.drawable.ic_update
                updateBtnBgRes = R.drawable.bg_btn
                titleTextColor = Color.BLACK
                titleTextSize = 18f
                contentTextColor = Color.parseColor("#88e16531")
            }

            // 更新配置
            val updateConfig = UpdateConfig().apply {
                force = true
                checkWifi = true
                isShowNotification = true
                notifyImgRes = R.drawable.ic_logo
                apkSavePath = Environment.getExternalStorageDirectory().absolutePath + "/teprinciple"
                apkSaveName = "teprinciple"
            }

            UpdateAppUtils
                .getInstance()
                .apkUrl(apkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(updateConfig)
                .uiConfig(uiConfig)
                .setUpdateDownloadListener(object : UpdateDownloadListener {
                    override fun onStart() {
                    }

                    override fun onDownload(progress: Int) {
                    }

                    override fun onFinish() {
                    }

                    override fun onError(e: Throwable) {
                    }
                })
                .update()
        }
    }
}