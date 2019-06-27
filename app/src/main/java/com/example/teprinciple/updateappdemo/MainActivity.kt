package com.example.teprinciple.updateappdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import constacne.DownLoadBy
import constacne.UiType
import kotlinx.android.synthetic.main.activity_main.*
import listener.Md5CheckResultListener
import listener.UpdateDownloadListener
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

    private val apkUrl = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.63.apk"
    // private val apkUrl = "http://118.24.148.250:8080/yk/app-release.apk";
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
                .updateConfig(UpdateConfig(downloadBy = DownLoadBy.BROWSER))
                .uiConfig(UiConfig(uiType = UiType.PLENTIFUL))
                .update()
        }

        // 自定义UI
        btn_custom_ui.setOnClickListener {

        }

        // java使用示例
        btn_java_sample.setOnClickListener {
            startActivity(Intent(this, JavaDemoActivity::class.java))
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
                needCheckMd5 = true
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
                .setMd5CheckResultListener(object : Md5CheckResultListener {
                    override fun onResult(result: Boolean) {
                        // do something
                    }
                })
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
