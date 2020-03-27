package update

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import extension.installApk
import extension.no
import extension.yes

/**
 * desc: UpdateAppReceiver
 * author: teprinciple on 2019/06/3.
 */
internal class UpdateAppReceiver : BroadcastReceiver() {

    private val notificationChannel = "1001"

    private val updateConfig by lazy { UpdateAppUtils.updateInfo.config }

    private val uiConfig by lazy { UpdateAppUtils.updateInfo.uiConfig }

    private var lastProgress = 0

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {

            // 下载中
            context.packageName + ACTION_UPDATE -> {
                // 进度
                val progress = intent.getIntExtra(KEY_OF_INTENT_PROGRESS, 0)

                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                (progress != -1000).yes {
                    lastProgress = progress
                }

                // 显示通知栏
                val notifyId = 1
                updateConfig.isShowNotification.yes {
                    showNotification(context, notifyId, progress, notificationChannel, nm)
                }

                // 下载完成
                if (progress == 100) {
                    handleDownloadComplete(context, notifyId, nm)
                }
            }

            // 重新下载
            context.packageName + ACTION_RE_DOWNLOAD -> {
                DownloadAppUtils.reDownload()
            }
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
        context.installApk(DownloadAppUtils.downloadUpdateApkFilePath)
    }

    /**
     * 通知栏显示
     */
    private fun showNotification(context: Context, notifyId: Int, progress: Int, notificationChannel: String, nm: NotificationManager) {

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


        // 设置通知图标
        (updateConfig.notifyImgRes > 0).yes {
            builder.setSmallIcon(updateConfig.notifyImgRes)
            builder.setLargeIcon(BitmapFactory.decodeResource(context.resources, updateConfig.notifyImgRes))
        }.no {
            builder.setSmallIcon(android.R.mipmap.sym_def_app_icon)
        }

        // 设置进度
        builder.setProgress(100, lastProgress, false)

        if (progress == -1000) {
            val intent = Intent(context.packageName + ACTION_RE_DOWNLOAD)
            intent.setPackage(context.packageName)
            val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT)
            builder.setContentIntent(pendingIntent)
            // 通知栏标题
            builder.setContentTitle(uiConfig.downloadFailText)
        } else {
            // 通知栏标题
            builder.setContentTitle("${uiConfig.downloadingBtnText}$progress%")
        }


        // 设置只响一次
        builder.setOnlyAlertOnce(true)
        val notification = builder.build()
        nm.notify(notifyId, notification)
    }

    companion object {
        /**
         * 进度key
         */
        private const val KEY_OF_INTENT_PROGRESS = "KEY_OF_INTENT_PROGRESS"

        /**
         * ACTION_UPDATE
         */
        const val ACTION_UPDATE = "teprinciple.update"

        /**
         * ACTION_RE_DOWNLOAD
         */
        const val ACTION_RE_DOWNLOAD = "action_re_download"


        const val REQUEST_CODE = 1001


        /**
         * 发送进度通知
         */
        fun send(context: Context, progress: Int) {
            val intent = Intent(context.packageName + ACTION_UPDATE)
            intent.putExtra(KEY_OF_INTENT_PROGRESS, progress)
            context.sendBroadcast(intent)
        }
    }
}