package util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager

/**
 * desc: Utils
 * author: teprinciple on 2019-06-03.
 */
internal object Utils {

    /**
     * 检测wifi是否连接
     */
    private fun isWifiConnected(context: Context): Boolean {
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
    private fun getAPPLocalVersion(ctx: Context): PackageInfo?{
        val manager = ctx.packageManager
        return try {
            manager.getPackageInfo(ctx.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}