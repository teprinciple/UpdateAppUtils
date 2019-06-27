package com.example.teprinciple.updateappdemo

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

    private val updateTitle = "发现新版本V2.0.0"

    private val updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索"

    private val uiConfig by lazy {
        UiConfig().apply {
            uiType = UiType.PLENTIFUL
//            cancelBtnText = "下次再说"
//            updateLogoImgRes = R.drawable.ic_update
//            updateBtnBgRes = R.drawable.bg_btn
//            titleTextColor = Color.BLACK
//            titleTextSize = 18f
//            contentTextColor = Color.parseColor("#88e16531")
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
                .uiConfig(UiConfig(uiType = UiType.SIMPLE))
                .update()
        }
    }
}
