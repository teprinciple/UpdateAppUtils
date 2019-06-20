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
import util.Utils


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
                    UpdateAppUtils.listener?.onStart()
                    UpdateAppReceiver.send(context, 0)
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    val progress = (soFarBytes * 100.0 / totalBytes).toInt()
                    log("progress:$progress")
                    UpdateAppReceiver.send(context, progress)
                    onProgress.invoke(progress)
                    UpdateAppUtils.listener?.onDownload(progress)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {}

                override fun completed(task: BaseDownloadTask) {
                    log("completed")
                    UpdateAppReceiver.send(context, 100)
                    UpdateAppUtils.listener?.onFinish()
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                    log("error:${e.message}")
                    onError.invoke()
                    UpdateAppUtils.listener?.onError(e)
                }

                override fun warn(task: BaseDownloadTask) {
                }
            }).start()
    }
}