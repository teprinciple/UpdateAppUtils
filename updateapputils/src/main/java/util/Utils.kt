package util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import extension.log
import extension.yes
import java.io.File


/**
 * desc: Utils
 * author: teprinciple on 2019-06-03.
 */
internal object Utils {

    /**
     * 跳转安装
     */
    fun installApk(context: Context, apkPath: String) {

        val i = Intent(Intent.ACTION_VIEW)
        val apkFile = File(apkPath)

        // android 7.0 fileprovider 适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(
                context, context.packageName + ".fileprovider", apkFile)
            i.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(i)
    }

    /**
     * 退出app
     */
    fun exitApp() {
        val manager = getApp().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.appTasks.forEach {
                it.finishAndRemoveTask()
            }
        } else {
            System.exit(0)
        }
    }

    /**
     * 检测wifi是否连接
     */
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        if (cm != null) {
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }
        }
        return false
    }

    /**
     * 获取apk的版本号 currentVersionCode
     */
    fun getAPPVersionCode(): Int {
        val manager = getApp().applicationContext.packageManager
        return try {
            return manager.getPackageInfo(getApp().applicationContext.packageName, 0).versionCode
        } catch (e: Exception) {
            -1
        }
    }

    /**
     * 获取apk versioncode
     */
    fun getApkVersionCode(apkPath: String): Int {
        val pm = getApp().applicationContext.packageManager
        return try {
            pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES).versionCode
        } catch (e: java.lang.Exception) {
            -1
        }
    }

    /**
     * 获取应用程序名称
     */
    fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager;
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 删除文件
     */
    fun deleteFile(filePath: String?) {
        if (filePath == null) {
            return
        }
        val file = File(filePath)
        try {
            (file.isFile).yes {
                file.delete()
                log("删除成功")
            }
        } catch (e: Exception) {
            log(e.message)
        }
    }

    @JvmStatic
    fun getApp(): Context {
        return GlobalContextProvider.getGlobalContext()
    }
}