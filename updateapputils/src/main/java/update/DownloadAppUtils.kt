package update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadLargeFileListener
import com.liulishuo.filedownloader.FileDownloader
import extension.TAG
import extension.log
import model.UpdateInfo
import java.io.File


/**
 * Created by Teprinciple on 2016/12/13.
 */
internal object DownloadAppUtils {

    var downloadUpdateApkFilePath: String = "" //下载更新Apk 文件路径

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

        val packageName = context.packageName
        log("packageName:$packageName")

        // TODO 这里设置 下载存储位置
        var filePath: String? = null
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) { //外部存储卡
            filePath = Environment.getExternalStorageDirectory().absolutePath
        } else {
            Log.i(TAG, "没有SD卡")
            return
        }
        val apkLocalPath = filePath + File.separator + packageName + "_" + System.currentTimeMillis() + ".apk"
        downloadUpdateApkFilePath = apkLocalPath

        FileDownloader.setup(context)

        FileDownloader.getImpl().create(updateInfo.apkUrl)
            .setPath(apkLocalPath)
            .setListener(object : FileDownloadLargeFileListener() {

                override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    log("pending:soFarBytes($soFarBytes),totalBytes($totalBytes)")
                    UpdateAppUtils.listener?.onStart()
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