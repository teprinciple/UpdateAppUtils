package update

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder

/**
 * desc: UpdateAppService
 * author: teprinciple on 2018/11/3.
 */
internal class UpdateAppService : Service() {

    private val updateAppReceiver = UpdateAppReceiver()

    override fun onCreate() {
        super.onCreate()
        // 动态注册receiver 适配8.0 updateAppReceiver 静态注册没收不到广播
        registerReceiver(updateAppReceiver, IntentFilter(packageName + UpdateAppReceiver.ACTION_UPDATE))
        registerReceiver(updateAppReceiver, IntentFilter(packageName + UpdateAppReceiver.ACTION_RE_DOWNLOAD))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(updateAppReceiver) // 注销广播
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
