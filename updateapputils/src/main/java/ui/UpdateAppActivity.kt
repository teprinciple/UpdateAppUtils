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
import com.teprinciple.updateapputils.R
import extension.no
import extension.yes
import model.DownLoadBy
import update.DownloadAppUtils
import update.UpdateAppService
import update.UpdateAppUtils
import util.AlertDialogUtil
import util.Utils

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

        // 更新标题
        updateConfig.updateTitle.isNotEmpty().yes {
            tvTitle.text = updateConfig.updateTitle
        }

        // 更新内容
        tvContent.text = updateConfig.updateInfo

        // 取消
        tvCancelBtn.setOnClickListener {
            updateConfig.force.yes {
                System.exit(0)
            }.no {
                finish()
            }
        }

        // 确定
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
     * 下载判断
     */
    private fun download() {
        // 动态注册广播，8.0 静态注册收不到
        // 开启服务注册，避免直接在Activity中注册广播生命周期随Activity终止而终止
        startService(Intent(this, UpdateAppService::class.java))

        when (updateConfig.downloadBy) {
            // App下载
            DownLoadBy.APP -> {
                (updateConfig.checkWifi && !Utils.isWifiConnected(applicationContext)).yes {
                    // 需要进行WiFi判断
                    AlertDialogUtil.show(this, "当前没有连接Wifi，是否继续下载", onSureClick = {
                        realDownload()
                    })
                }.no {
                    // 不需要wifi判断，直接下载
                    realDownload()
                }
            }

            // 浏览器下载
            DownLoadBy.BROWSER -> {
                DownloadAppUtils.downloadForWebView(this, updateConfig.apkUrl)
            }
        }
    }

    /**
     * 实际下载
     */
    private fun realDownload() {
        DownloadAppUtils.download(this, updateConfig, onProgress = {
            // TODO 在这里设置进度
        }, onError = {

        })
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
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission).no {
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
