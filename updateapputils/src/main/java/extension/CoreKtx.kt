package extension

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import update.UpdateAppUtils
import util.GlobalContextProvider
import kotlin.system.exitProcess

/**
 * desc: 扩展
 * author: teprinc
 * iple on 2020/3/27.
 */

/**
 * 全局context
 */
fun globalContext() = GlobalContextProvider.mContext


/**
 * 打印日志
 */
fun log(content: String?) = UpdateAppUtils.updateInfo.config.isDebug.yes {
    Log.e("[UpdateAppUtils]", content ?: "")
}

/**
 * 获取color
 */
fun color(color: Int) = if (globalContext() == null) 0 else ContextCompat.getColor(globalContext()!!, color)

/**
 * 获取 String
 */
fun string(string: Int) = globalContext()?.getString(string) ?: ""

/**
 * view 显示隐藏
 */
fun View.visibleOrGone(show: Boolean) {
    if (show) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

/**
 * 退出app
 */
fun exitApp() {
    val manager = globalContext()!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        manager.appTasks.forEach { it.finishAndRemoveTask() }
    } else {
        exitProcess(0)
    }
}