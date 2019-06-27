package com.example.teprinciple.updateappdemo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import constacne.DownLoadBy
import constacne.UiType
import kotlinx.android.synthetic.main.activity_main.*
import model.UiConfig
import model.UpdateConfig
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

    private val apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.63.apk"
    // private val apkPath = "http://118.24.148.250:8080/yk/app-release.apk";

    private val updateTitle = "发现新版本V2.2.3"

    private val updateContent = "1、快来升级最新版本\n2、这次更漂亮了\n3、快点来吧"

    private val uiConfig by lazy {
        UiConfig().apply {
            uiType = UiType.PLENTIFUL
            cancelBtnText = "下次再说"
            cancelBtnTextColor = Color.RED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 基本使用
        btn_basic_use.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkPath)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .uiConfig(uiConfig)
                .update()
        }

        // 浏览器下载
        btn_download_by_browser.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkPath)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(UpdateConfig(downloadBy = DownLoadBy.BROWSER))
                .uiConfig(UiConfig(uiType = UiType.SIMPLE,cancelBtnTextColor = Color.GREEN))
                .update()
        }
    }
}
