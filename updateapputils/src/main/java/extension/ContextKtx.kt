package extension

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * desc: context 相关扩展
 * author: teprinciple on 2020/3/27.
 */


/**
 * appName
 */
val Context.appName
    get() = packageManager.getPackageInfo(packageName, 0)?.applicationInfo?.loadLabel(packageManager).toString()

/**
 * 检测wifi是否连接
 */
fun Context.isWifiConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    cm ?: return false
    val networkInfo = cm.activeNetworkInfo
    return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
}


/**
 * 跳转安装
 */
fun Context.installApk(apkPath: String?) {

    if (apkPath.isNullOrEmpty())return

    val intent = Intent(Intent.ACTION_VIEW)
    val apkFile = File(apkPath)

    // android 7.0 fileprovider 适配
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val contentUri = FileProvider.getUriForFile(this, this.packageName + ".fileprovider", apkFile)
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
    } else {
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}


