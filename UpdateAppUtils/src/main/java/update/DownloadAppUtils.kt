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
     * App 下载APK包 并
     */
    fun download(context: Context, url: String, serverVersionName: String) {

        val packageName = context.packageName
        var filePath: String? = null
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {//外部存储卡
            filePath = Environment.getExternalStorageDirectory().absolutePath
        } else {
            Log.i(TAG, "没有SD卡")
            return
        }

        val apkLocalPath = filePath + File.separator + packageName + "_" + serverVersionName + ".apk"

        downloadUpdateApkFilePath = apkLocalPath

        FileDownloader.setup(context)

        FileDownloader.getImpl().create(url)
            .setPath(apkLocalPath)
            .setListener(object : FileDownloadLargeFileListener() {
                override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {

                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                    send(context, (soFarBytes * 100.0 / totalBytes).toInt(), serverVersionName)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {}

                override fun completed(task: BaseDownloadTask) {
                    send(context, 100, serverVersionName)
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                    //Toast.makeText(context, "下载出错", Toast.LENGTH_SHORT).show();

                    // todo 将错误回调给调用着
                }

                override fun warn(task: BaseDownloadTask) {}
            }).start()
    }


    /**
     * 发送通知进度
     */
    private fun send(context: Context, progress: Int, serverVersionName: String) {
        val intent = Intent("teprinciple.update")
        intent.putExtra("progress", progress)
        intent.putExtra("title", serverVersionName)
        context.sendBroadcast(intent)
    }
}
