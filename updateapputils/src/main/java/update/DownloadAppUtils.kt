package update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.common.RequestEnum
import com.arialyy.aria.core.download.DownloadTaskListener
import com.arialyy.aria.core.task.DownloadTask
import extension.*
import util.FileDownloadUtil
import util.SPUtil
import util.SignMd5Util
import java.io.File

/**
 * Created by Teprinciple on 2016/12/13.
 */
internal object DownloadAppUtils : DownloadTaskListener {
    const val KEY_OF_SP_APK_PATH = "KEY_OF_SP_APK_PATH"

    /**
     * apk 下载后本地文件路径
     */
    var downloadUpdateApkFilePath: String = ""

    /**
     * task id
     */
    var taskId: Long = 0

    /**
     * 更新信息
     */
    private val updateInfo by lazy { UpdateAppUtils.updateInfo }

    /**
     * context
     */
    private val context by lazy { globalContext()!! }

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
        Aria.download(this).register()

        (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED).no {
            log("没有SD卡")
            onError.invoke()
            return
        }

        var filePath = ""
        (updateInfo.config.apkSavePath.isNotEmpty()).yes {
            filePath = updateInfo.config.apkSavePath
        }.no {
            // 适配Android10
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Environment.isExternalStorageLegacy()){
                filePath = (context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath ?: "") + "/apk"
            }else{
                val packageName = context.packageName
                filePath = Environment.getExternalStorageDirectory().absolutePath + "/" + packageName
            }
        }

        // apk 保存名称
        val apkName = if (updateInfo.config.apkSaveName.isNotEmpty()) {
            updateInfo.config.apkSaveName
        } else {
            context.appName
        }

        if (!File(filePath).exists()) {
            File(filePath).mkdirs()
        }

        val apkLocalPath = "$filePath/$apkName.apk"

        downloadUpdateApkFilePath = apkLocalPath

        SPUtil.putBase(KEY_OF_SP_APK_PATH, downloadUpdateApkFilePath)

        taskId = Aria.download(this)
            .load(updateInfo.apkUrl)
            .option(HttpOption().apply {
                addHeader("Accept-Encoding", "identity")
                addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36"
                )
                setRequestType(RequestEnum.GET)
            })
            .setFilePath(apkLocalPath)
            .ignoreFilePathOccupy()
            .resetState()
            .create()
    }

    override fun onWait(task: DownloadTask?) {
    }

    override fun onPre(task: DownloadTask?) {
        if (task != null) {
            log("onPre:soFarBytes(${task.currentProgress}),totalBytes(${task.fileSize})")
            downloadStart()
            if (task.fileSize < 0) {
                task.stop()
            }
        }
    }

    override fun onTaskPre(task: DownloadTask?) {
    }

    override fun onTaskResume(task: DownloadTask?) {
    }

    override fun onTaskStart(task: DownloadTask?) {
    }

    override fun onTaskStop(task: DownloadTask?) {
        log("获取文件总长度失败出错，尝试HTTPURLConnection下载")
        downloadUpdateApkFilePath.deleteFile()
        "$downloadUpdateApkFilePath.temp".deleteFile()
        downloadByHttpUrlConnection(updateInfo.config.apkSavePath, updateInfo.config.apkSaveName)
    }

    override fun onTaskCancel(task: DownloadTask?) {
    }

    override fun onTaskFail(task: DownloadTask?, e: Exception?) {
        log("下载出错，尝试HTTPURLConnection下载$e")
        downloadUpdateApkFilePath.deleteFile()
        "$downloadUpdateApkFilePath.temp".deleteFile()
        downloadByHttpUrlConnection(updateInfo.config.apkSavePath, updateInfo.config.apkSaveName)
    }

    override fun onTaskComplete(task: DownloadTask?) {
        downloadComplete()
    }

    override fun onTaskRunning(task: DownloadTask?) {
        if (task != null) {
            downloading(task.currentProgress, task.fileSize)
            if (task.fileSize < 0) {
                task.stop()
            }
        }
    }

    override fun onNoSupportBreakPoint(task: DownloadTask?) {
    }

    /**
     * 使用 HttpUrlConnection 下载
     */
    private fun downloadByHttpUrlConnection(filePath: String, apkName: String?) {
        FileDownloadUtil.download(
            updateInfo.apkUrl,
            filePath,
            "$apkName.apk",
            onStart = { downloadStart() },
            onProgress = { current, total -> downloading(current, total) },
            onComplete = { downloadComplete() },
            onError = { downloadError(it) }
        )
    }

    /**
     * 开始下载逻辑
     */
    private fun downloadStart() {
        isDownloading = true
        UpdateAppUtils.downloadListener?.onStart()
        UpdateAppReceiver.send(context, 0)
    }

    /**
     * 下载中逻辑
     */
    private fun downloading(soFarBytes: Long, totalBytes: Long) {
//        log("soFarBytes:$soFarBytes--totalBytes:$totalBytes")
        isDownloading = true
        var progress = (soFarBytes * 100.0 / totalBytes).toInt()
        if (progress < 0) progress = 0
        log("progress:$progress")
        UpdateAppReceiver.send(context, progress)
        this@DownloadAppUtils.onProgress.invoke(progress)
        UpdateAppUtils.downloadListener?.onDownload(progress)
    }

    /**
     * 下载完成处理逻辑
     */
    private fun downloadComplete() {
        isDownloading = false
        log("completed")
        this@DownloadAppUtils.onProgress.invoke(100)
        UpdateAppUtils.downloadListener?.onFinish()
        Aria.download(this).unRegister()
        // 校验md5
        (updateInfo.config.needCheckMd5).yes {
            checkMd5(context)
        }.no {
            UpdateAppReceiver.send(context, 100)
        }
    }

    /**
     * 下载失败处理逻辑
     */
    private fun downloadError(e: Throwable) {
        isDownloading = false
        log("error:${e.message}")
        downloadUpdateApkFilePath.deleteFile()
        this@DownloadAppUtils.onError.invoke()
        UpdateAppUtils.downloadListener?.onError(e)
        UpdateAppReceiver.send(context, -1000)
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