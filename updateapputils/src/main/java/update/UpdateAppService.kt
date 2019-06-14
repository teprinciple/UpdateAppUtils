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

    private val receiver = UpdateAppReceiver()

    override fun onCreate() {
        super.onCreate()
        // 动态注册receiver 适配8.0 receiver 静态注册没收不到广播
        val intentFilter = IntentFilter("teprinciple.update")
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver) // 注销广播
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
