package com.example.teprinciple.updateappdemo

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.check_md5_demo_activity.*
import listener.Md5CheckResultListener
import model.UpdateConfig
import update.UpdateAppUtils

/**
 * desc: md5校验示例
 * time: 2019/7/1
 * @author yk
 */
class CheckMd5DemoActivity : AppCompatActivity() {

    /**
     * 已签名的apk
     */
    private val signedApkUrl = "http://118.24.148.250:8080/yk/update_signed.apk"

    /**
     * 非正规签名的apk
     */
    private val notSignedApkUrl = "http://118.24.148.250:8080/yk/update_not_signed.apk"

    private val updateTitle = "发现新版本V2.0.0"
    private val updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.check_md5_demo_activity)

        // 更新配置
        val updateConfig = UpdateConfig().apply {
            force = true
            needCheckMd5 = true
        }

        // 正确签名
        btn_signed.setOnClickListener {
            updateConfig.apply { apkSaveName = "signed" }
            UpdateAppUtils
                .getInstance()
                .apkUrl(signedApkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(updateConfig)
                .setMd5CheckResultListener(object : Md5CheckResultListener {
                    override fun onResult(result: Boolean) {
                        Toast.makeText(this@CheckMd5DemoActivity, "Md5检验是否通过：$result", Toast.LENGTH_SHORT).show()
                    }
                })
                .update()
        }

        // 错误签名
        btn_not_signed.setOnClickListener {
            updateConfig.apply { apkSaveName = "not_signed" }
            UpdateAppUtils
                .getInstance()
                .apkUrl(notSignedApkUrl)
                .updateTitle(updateTitle)
                .updateContent(updateContent)
                .updateConfig(updateConfig)
                .setMd5CheckResultListener(object : Md5CheckResultListener {
                    override fun onResult(result: Boolean) {
                        Toast.makeText(this@CheckMd5DemoActivity, "Md5检验是否通过：$result", Toast.LENGTH_SHORT).show()
                    }
                })
                .update()
        }
    }
}