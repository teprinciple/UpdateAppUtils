package com.example.teprinciple.updateappdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import model.DownLoadBy
import model.UpdateConfig
import update.UpdateAppUtils


class MainActivity : AppCompatActivity() {

    private val apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.63.apk"
    // private val apkPath = "http://118.24.148.250:8080/yk/app-release.apk";

    private val updateTitle = "发现新版本V2.2.3"

    private val updateContent = "1、快来升级最新版本\n2、这次更漂亮了\n3、快点来吧"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 基本使用
        btn_basic_use.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkPath)
                .updateTitle(updateTitle)
                .changeConfig(UpdateConfig(customLayoutId = R.layout.view_custom_update_dialog))
                .updateContent(updateContent)
                .update()
        }

        // 浏览器下载
        btn_download_by_browser.setOnClickListener {
            UpdateAppUtils
                .getInstance()
                .apkUrl(apkPath)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .changeConfig(UpdateConfig(downloadBy = DownLoadBy.BROWSER))
                .update()
        }


    }
}
