package ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import extension.no
import extension.yes
import model.DownLoadBy
import teprinciple.updateapputils.R
import update.DownloadAppUtils
import update.UpdateAppService
import update.UpdateAppUtils
import util.AlertDialogUtil

internal class UpdateAppActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvSureBtn: TextView
    private lateinit var tvCancelBtn: TextView

    private val updateConfig by lazy { UpdateAppUtils.updateConfig }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO 可以从这里设置不同的UI
        setContentView(R.layout.view_version_tips_dialog)
        initView()
    }

    private fun initView() {

        tvTitle = findViewById(R.id.tv_update_title) as TextView
        tvContent = findViewById(R.id.tv_update_content) as TextView
        tvCancelBtn = findViewById(R.id.tv_update_cancel) as TextView
        tvSureBtn = findViewById(R.id.tv_update_sure) as TextView

        updateConfig.updateTitle.isNotEmpty().yes {
            tvTitle.text = updateConfig.updateTitle
        }

        tvContent.text = updateConfig.updateInfo

        tvCancelBtn.setOnClickListener {
            updateConfig.force.yes {
                System.exit(0)
            }.no {
                finish()
            }
        }

        tvSureBtn.setOnClickListener {
            preDownLoad()
        }
    }


    override fun onBackPressed() {
        // do noting
    }


    /**
     * 预备下载 进行 6.0权限检查
     */
    private fun preDownLoad() {
        // 6.0 以下不用动态权限申请
        (Build.VERSION.SDK_INT < Build.VERSION_CODES.M).yes {
            download()
        }.no {
            val writePermission = ContextCompat.checkSelfPermission(this, permission)
            (writePermission == PackageManager.PERMISSION_GRANTED).yes {
                download()
            }.no {
                // 申请权限
                ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_CODE)
            }
        }
    }

    /**
     * 下载
     */
    private fun download() {
        // 开启服务 动态注册 receiver
        startService(Intent(this, UpdateAppService::class.java))
        when (updateConfig.downloadBy) {
            // App下载
            DownLoadBy.APP -> {
                // TODO 这里增加WIFI判断
                DownloadAppUtils.download(this, updateConfig.apkUrl, updateConfig.serverVersionName)
            }

            // 浏览器下载
            DownLoadBy.BROWSER -> {
                DownloadAppUtils.downloadForWebView(this, updateConfig.apkUrl)
            }
        }
//        finish()
    }

    /**
     * 权限请求结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> (grantResults[0] == PackageManager.PERMISSION_GRANTED).yes {
                download()
            }.no {
                ActivityCompat.shouldShowRequestPermissionRationale(this,permission).no {
                    // 显示无权限弹窗
                    AlertDialogUtil.show(this, "暂无储存权限，是否前往打开", onSureClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName") // 根据包名打开对应的设置界面
                        startActivity(intent)
                    })
                }
            }
        }
    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, UpdateAppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        private const val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        private const val PERMISSION_CODE = 1001
    }
}
