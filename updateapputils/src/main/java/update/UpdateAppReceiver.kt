package update

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import com.teprinciple.updateapputils.R
import extension.yes
import util.Utils

/**
 * desc: UpdateAppReceiver
 * author: teprinciple on 2019/06/3.
 */
internal class UpdateAppReceiver : BroadcastReceiver() {

    private val notificationChannel = "1001"

    private val updateConfig by lazy { UpdateAppUtils.updateInfo.config }

    override fun onReceive(context: Context, intent: Intent) {
        // 进度
        val progress = intent.getIntExtra(KEY_OF_INTENT_PROGRESS, 0)

        // 通知栏标题
        val title = intent.getStringExtra(KEY_OF_INTENT_TITLE)

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 显示通知栏
        val notifyId = 1
        updateConfig.isShowNotification.yes {
            showNotification(context, notifyId, progress, title, notificationChannel, nm)
        }

        // 下载完成
        if (progress == 100) {
            handleDownloadComplete(context, notifyId, nm)
        }
    }

    /**
     * 下载完成后的逻辑
     */
    private fun handleDownloadComplete(context: Context, notifyId: Int, nm: NotificationManager?) {
        // 关闭通知栏
        nm?.let {
            nm.cancel(notifyId)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                nm.deleteNotificationChannel(notificationChannel)
            }
        }

        // 安装apk
        DownloadAppUtils.downloadUpdateApkFilePath.isNotEmpty().yes {
            Utils.installApk(context, DownloadAppUtils.downloadUpdateApkFilePath)
        }
    }

    /**
     * 通知栏显示
     */
    private fun showNotification(context: Context, notifyId: Int, progress: Int, title: String, notificationChannel: String, nm: NotificationManager) {

        val notificationName = "notification"

        // 适配 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知渠道
            val channel = NotificationChannel(notificationChannel, notificationName, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(false)
            // 是否在桌面icon右上角展示小红点
            channel.setShowBadge(false)
            // 是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false)
            // 最后在notificationmanager中创建该通知渠道
            nm.createNotificationChannel(channel)
        }

        val builder = Notification.Builder(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationChannel)
        }

        // TODO 设置
        builder.setContentTitle("正在下载 $title")

        // 设置通知图标
        builder.setSmallIcon(R.drawable.ic_logo)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_logo))

        builder.setProgress(100, progress, false)
        // 设置只响一次
        builder.setOnlyAlertOnce(true)
        val notification = builder.build()
        nm.notify(notifyId, notification)
    }

    companion object {
        /**
         * 标题key
         */
        private const val KEY_OF_INTENT_TITLE = "KEY_OF_INTENT_TITLE"
        /**
         * 进度key
         */
        private const val KEY_OF_INTENT_PROGRESS = "KEY_OF_INTENT_PROGRESS"

        /**
         * ACTION_UPDATE
         */
        const val ACTION_UPDATE = "teprinciple.update" // TODO 研究一下这里

        /**
         * 发送进度通知
         */
        fun send(context: Context, progress: Int, serverVersionName: String = "") {
            val intent = Intent(ACTION_UPDATE)
            intent.putExtra(KEY_OF_INTENT_PROGRESS, progress)
            intent.putExtra(KEY_OF_INTENT_TITLE, serverVersionName)
            context.sendBroadcast(intent)
        }
    }
}