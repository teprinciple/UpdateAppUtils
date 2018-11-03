package util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;


/**
 * Created by Teprinciple on 2017/11/3.
 */

public class UpdateAppReceiver extends BroadcastReceiver {

    private String notificationChannel = "1001";

    @Override
    public void onReceive(Context context, Intent intent) {

        int progress = intent.getIntExtra("progress", 0);
        String title = intent.getStringExtra("title");


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 显示通知栏
        int notifyId = 1;
        if (UpdateAppUtils.showNotification) {
            showNotification(context, notifyId, progress, title, notificationChannel, nm);
        }

        // 下载完成
        if (progress == 100) {
            handleDownloadComplete(context, notifyId, nm);
        }
    }


    /**
     * 下载完成后的逻辑
     */
    private void handleDownloadComplete(Context context, int notifyId, NotificationManager nm) {
        // 关闭通知栏
        if (nm != null) {
            nm.cancel(notifyId);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                nm.deleteNotificationChannel(notificationChannel);
            }
        }

        // 安装apk
        if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
            toInstall(context);
        }
    }


    /**
     * 通知栏显示
     */
    private void showNotification(Context context, int notifyId, int progress, String title, String notificationChannel, NotificationManager nm) {

        String notificationName = "notification";

        // 适配8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 通知渠道
            NotificationChannel channel = new NotificationChannel(notificationChannel, notificationName, NotificationManager.IMPORTANCE_HIGH);

            channel.enableLights(false); // 是否在桌面icon右上角展示小红点
            channel.setShowBadge(false); // 是否在久按桌面图标时显示此渠道的通知
            channel.enableVibration(false);

            // 最后在notificationmanager中创建该通知渠道
            nm.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(context);

        //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationChannel);
        }

        builder.setContentTitle("正在下载 " + title);
        builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        builder.setProgress(100, progress, false);

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }else {
            notification = builder.getNotification();
        }

        nm.notify(notifyId, notification);
    }

    /**
     * 跳转安装
     */
    private void toInstall(Context context) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context, context.getPackageName() + ".fileprovider", apkFile);
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}