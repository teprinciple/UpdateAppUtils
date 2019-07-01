package update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadLargeFileListener
import com.liulishuo.filedownloader.FileDownloader
import extension.log
import extension.no
import extension.yes
import model.UpdateInfo
import util.GlobalContextProvider
import util.SPUtil
import util.SignMd5Util
import util.Utils
import java.io.File

/**
 * Created by Teprinciple on 2016/12/13.
 */
internal object DownloadAppUtils {

    const val KEY_OF_SP_APK_PATH = "KEY_OF_SP_APK_PATH"

    /**
     * apk 下载后本地文件路径
     */
    var downloadUpdateApkFilePath: String = ""

    /**
     * 更新信息
     */
    private val updateInfo by lazy { UpdateAppUtils.updateInfo }

    /**
     * context
     */
    private val context by lazy { GlobalContextProvider.getGlobalContext() }

    /**
     * 是否在下载中
     */
    var isDownloading = false

    /**
     *下载进度回调
     */
    var onProgress: (Int) -> Unit = {}

    /**
     * 下载出错回调
     */
    var onError: () -> Unit = {}

    /**
     * 出错，点击重试回调
     */
    var onReDownload: () -> Unit = {}

    /**
     * 通过浏览器下载APK包
     */
    fun downloadForWebView(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 出错后，点击重试
     */
    fun reDownload() {
        onReDownload.invoke()
        download()
    }

    /**
     * App下载APK包，下载完成后安装
     */
    fun download() {

        (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED).no {
            log("没有SD卡")
            onError.invoke()
            return
        }

        var filePath = ""
        (updateInfo.config.apkSavePath.isNotEmpty()).yes {
            filePath = updateInfo.config.apkSavePath
        }.no {
            val packageName = context.packageName
            filePath = Environment.getExternalStorageDirectory().absolutePath + "/" + packageName
        }

        // apk 保存名称
        val apkName = if (updateInfo.config.apkSaveName.isNotEmpty()) {
            updateInfo.config.apkSaveName
        } else {
            Utils.getAppName(context)
        }

        val apkLocalPath = "$filePath/$apkName.apk"

        downloadUpdateApkFilePath = apkLocalPath

        SPUtil.putBase(KEY_OF_SP_APK_PATH, downloadUpdateApkFilePath)

        FileDownloader.setup(context)

        FileDownloader.getImpl().create(updateInfo.apkUrl)
            .setPath(apkLocalPath)
            .setListener(object : FileDownloadLargeFileListener() {

                override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    log("pending:soFarBytes($soFarBytes),totalBytes($totalBytes)")
                    isDownloading = true
                    UpdateAppUtils.downloadListener?.onStart()
                    UpdateAppReceiver.send(context, 0)
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    isDownloading = true
                    val progress = (soFarBytes * 100.0 / totalBytes).toInt()
                    log("progress:$progress")
                    UpdateAppReceiver.send(context, progress)
                    this@DownloadAppUtils.onProgress.invoke(progress)
                    UpdateAppUtils.downloadListener?.onDownload(progress)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    isDownloading = false
                }

                override fun completed(task: BaseDownloadTask) {
                    isDownloading = false
                    log("completed")
                    this@DownloadAppUtils.onProgress.invoke(100)
                    UpdateAppUtils.downloadListener?.onFinish()
                    // 校验md5
                    (updateInfo.config.needCheckMd5).yes {
                        checkMd5(context)
                    }.no {
                        UpdateAppReceiver.send(context, 100)
                    }
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                    isDownloading = false
                    log("error:${e.message}")
                    Utils.deleteFile(downloadUpdateApkFilePath)
                    this@DownloadAppUtils.onError.invoke()
                    UpdateAppUtils.downloadListener?.onError(e)

                    UpdateAppReceiver.send(context, -1000)
                }

                override fun warn(task: BaseDownloadTask) {
                }
            }).start()
    }

    /**
     * 校验Md5
     *  先获取本应用的MD5值，获取未安装应用的MD5.进行对比
     */
    private fun checkMd5(context: Context) {
        // 当前应用md5
        val localMd5 = SignMd5Util.getAppSignatureMD5()

        // 下载的apk 签名md5
        val apkMd5 = SignMd5Util.getSignMD5FromApk(File(downloadUpdateApkFilePath))
        log("当前应用签名md5：$localMd5")
        log("下载apk签名md5：$apkMd5")

        // 校验结果回调
        UpdateAppUtils.md5CheckResultListener?.onResult(localMd5.equals(apkMd5, true))

        (localMd5.equals(apkMd5, true)).yes {
            log("md5校验成功")
            UpdateAppReceiver.send(context, 100)
        }.no {
            log("md5校验失败")
        }
    }
}