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
import util.SignMd5Util
import util.Utils
import java.io.File

/**
 * Created by Teprinciple on 2016/12/13.
 */
internal object DownloadAppUtils {

    /**
     * apk 下载后本地文件路径
     */
    var downloadUpdateApkFilePath: String = ""

    /**
     * 通过浏览器下载APK包
     */
    fun downloadForWebView(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * App下载APK包，下载完成后安装
     */
    fun download(context: Context, updateInfo: UpdateInfo, onProgress: (Int) -> Unit = {}, onError: () -> Unit = {}) {

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

        FileDownloader.setup(context)

        FileDownloader.getImpl().create(updateInfo.apkUrl)
            .setPath(apkLocalPath)
            .setListener(object : FileDownloadLargeFileListener() {

                override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    log("pending:soFarBytes($soFarBytes),totalBytes($totalBytes)")
                    UpdateAppUtils.downloadListener?.onStart()
                    UpdateAppReceiver.send(context, 0)
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    val progress = (soFarBytes * 100.0 / totalBytes).toInt()
                    log("progress:$progress")
                    UpdateAppReceiver.send(context, progress)
                    onProgress.invoke(progress)
                    UpdateAppUtils.downloadListener?.onDownload(progress)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {}

                override fun completed(task: BaseDownloadTask) {
                    log("completed")
                    UpdateAppUtils.downloadListener?.onFinish()
                    // 校验md5
                    (updateInfo.config.needCheckMd5).yes {
                        checkMd5(context)
                    }.no {
                        UpdateAppReceiver.send(context, 100)
                    }
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                    log("error:${e.message}")
                    onError.invoke()
                    UpdateAppUtils.downloadListener?.onError(e)
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