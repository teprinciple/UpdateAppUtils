package ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.teprinciple.updateapputils.R
import constant.DownLoadBy
import constant.UiType
import extension.*
import update.DownloadAppUtils
import update.UpdateAppService
import update.UpdateAppUtils
import util.AlertDialogUtil
import util.SPUtil

/**
 * desc: 更新弹窗
 * author: teprinciple on 2019/06/3.
 */
internal class UpdateAppActivity : AppCompatActivity() {

    private var tvTitle: TextView? = null
    private var tvContent: TextView? = null
    private var sureBtn: View? = null
    private var cancelBtn: View? = null
    private var ivLogo: ImageView? = null

    /**
     * 更新信息
     */
    private val updateInfo by lazy { UpdateAppUtils.updateInfo }

    /**
     * 更新配置
     */
    private val updateConfig by lazy { updateInfo.config }

    /**
     * ui 配置
     */
    private val uiConfig by lazy { updateInfo.uiConfig }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            when (uiConfig.uiType) {
                UiType.SIMPLE -> R.layout.view_update_dialog_simple
                UiType.PLENTIFUL -> R.layout.view_update_dialog_plentiful
                UiType.CUSTOM -> uiConfig.customLayoutId ?: R.layout.view_update_dialog_simple
                else -> R.layout.view_update_dialog_simple
            }
        )
        initView()
        initUi()

        // 初始化UI回调，用于进一步自定义UI
        UpdateAppUtils.onInitUiListener?.onInitUpdateUi(
            window.decorView.findViewById(android.R.id.content),
            updateConfig,
            uiConfig)

        // 每次弹窗后，下载前均把本地之前缓存的apk删除，避免缓存老版本apk或者问题apk，并不重新下载新的apk
        SPUtil.getString(DownloadAppUtils.KEY_OF_SP_APK_PATH, "").deleteFile()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        tvTitle = findViewById(R.id.tv_update_title)
        tvContent = findViewById(R.id.tv_update_content)
        cancelBtn = findViewById(R.id.btn_update_cancel)
        sureBtn = findViewById(R.id.btn_update_sure)
        ivLogo = findViewById(R.id.iv_update_logo)

        // 更新标题
        tvTitle?.text = updateInfo.updateTitle

        // 更新内容
        tvContent?.text = updateInfo.updateContent

        // 取消
        cancelBtn?.setOnClickListener {
            updateConfig.force.yes {
                exitApp()
            }.no {
                finish()
            }
        }

        // 确定
        sureBtn?.setOnClickListener {

            DownloadAppUtils.isDownloading.no {
                if (sureBtn is TextView) {
                    (sureBtn as? TextView)?.text = uiConfig.updateBtnText
                }
                preDownLoad()
            }
        }

        // 显示或隐藏取消按钮, 强更时默认不显示取消按钮
        hideShowCancelBtn(!updateConfig.force)

        // 外部额外设置 取消 按钮点击事件
        cancelBtn?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    UpdateAppUtils.onCancelBtnClickListener?.onClick() ?: false
                }
                else -> false
            }
        }

        // 外部额外设置 立即更新 按钮点击事件
        sureBtn?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    UpdateAppUtils.onUpdateBtnClickListener?.onClick() ?: false
                }
                else -> false
            }
        }
    }

    /**
     * 取消按钮处理
     */
    private fun hideShowCancelBtn(show: Boolean) {
        // 强制更新 不显示取消按钮
        cancelBtn?.visibleOrGone(show)
        // 取消按钮与确定按钮中的间隔线
        findViewById<View>(R.id.view_line)?.visibleOrGone(show)
    }

    /**
     * 初始化UI
     */
    private fun initUi() {

        uiConfig.apply {
            // 设置更新logo
            updateLogoImgRes?.let { ivLogo?.setImageResource(it) }
            // 设置标题字体颜色、大小
            titleTextColor?.let { tvTitle?.setTextColor(it) }
            titleTextSize?.let { tvTitle?.setTextSize(it) }
            // 设置标题字体颜色、大小
            contentTextColor?.let { tvContent?.setTextColor(it) }
            contentTextSize?.let { tvContent?.setTextSize(it) }
            // 更新按钮相关设置
            updateBtnBgColor?.let { sureBtn?.setBackgroundColor(it) }
            updateBtnBgRes?.let { sureBtn?.setBackgroundResource(it) }
            if (sureBtn is TextView) {
                updateBtnTextColor?.let { (sureBtn as? TextView)?.setTextColor(it) }
                updateBtnTextSize?.let { (sureBtn as? TextView)?.setTextSize(it) }
                (sureBtn as? TextView)?.text = updateBtnText
            }

            // 取消按钮相关设置
            cancelBtnBgColor?.let { cancelBtn?.setBackgroundColor(it) }
            cancelBtnBgRes?.let { cancelBtn?.setBackgroundResource(it) }
            if (cancelBtn is TextView) {
                cancelBtnTextColor?.let { (cancelBtn as? TextView)?.setTextColor(it) }
                cancelBtnTextSize?.let { (cancelBtn as? TextView)?.setTextSize(it) }
                (cancelBtn as? TextView)?.text = cancelBtnText
            }
        }
    }

    override fun onBackPressed() {
        // do noting 禁用返回键
    }

    /**
     * 预备下载 进行 6.0权限检查
     */
    private fun preDownLoad() {
        // 6.0 以下不用动态权限申请
        (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ).yes {
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
                (updateConfig.checkWifi && !isWifiConnected()).yes {
                    // 需要进行WiFi判断
                    AlertDialogUtil.show(this, getString(R.string.check_wifi_notice), onSureClick = {
                        realDownload()
                    })
                }.no {
                    // 不需要wifi判断，直接下载
                    realDownload()
                }
            }

            // 浏览器下载
            DownLoadBy.BROWSER -> {
                DownloadAppUtils.downloadForWebView(updateInfo.apkUrl)
            }
        }
    }

    /**
     * 实际下载
     */
    @SuppressLint("SetTextI18n")
    private fun realDownload() {

        if ((updateConfig.force || updateConfig.alwaysShowDownLoadDialog) && sureBtn is TextView) {
            DownloadAppUtils.onError = {
                (sureBtn as? TextView)?.text = uiConfig.downloadFailText
                (updateConfig.alwaysShowDownLoadDialog).yes {
                    hideShowCancelBtn(true)
                }
            }

            DownloadAppUtils.onReDownload = {
                (sureBtn as? TextView)?.text = uiConfig.updateBtnText
            }

            DownloadAppUtils.onProgress = {
                (it == 100).yes {
                    (sureBtn as? TextView)?.text = getString(R.string.install)
                    (updateConfig.alwaysShowDownLoadDialog).yes {
                        hideShowCancelBtn(true)
                    }
                }.no {
                    (sureBtn as? TextView)?.text = "${uiConfig.downloadingBtnText}$it%"
                    (updateConfig.alwaysShowDownLoadDialog).yes {
                        hideShowCancelBtn(false)
                    }
                }
            }
        }

        DownloadAppUtils.download()

        (updateConfig.showDownloadingToast).yes {
            Toast.makeText(this, uiConfig.downloadingToastText, Toast.LENGTH_SHORT).show()
        }

        // 非强制安装且alwaysShowDownLoadDialog为false时，开始下载后取消弹窗
        (!updateConfig.force && !updateConfig.alwaysShowDownLoadDialog).yes {
            finish()
        }
    }

    /**
     * 权限请求结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED).yes {
                download()
            }.no {
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission).no {
                    // 显示无权限弹窗
                    AlertDialogUtil.show(this, getString(R.string.no_storage_permission), onSureClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:$packageName") // 根据包名打开对应的设置界面
                        startActivity(intent)
                    })
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {

        fun launch() = globalContext()?.let {
            val intent = Intent(it, UpdateAppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            it.startActivity(intent)
        }

        private const val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        private const val PERMISSION_CODE = 1001
    }
}
