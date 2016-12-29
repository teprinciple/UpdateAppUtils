package com.example.teprinciple.updateappdemo.updateapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**
 * 注册
 * <action android:name="android.intent.action.DOWNLOAD_COMPLETE" />
 * <action android:name="android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED"/>
 */
public class UpdateAppReceiver extends BroadcastReceiver {
    public UpdateAppReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // 处理下载完成
        Cursor c=null;
        try {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                if (DownloadAppUtils.downloadUpdateApkId >= 0) {
                    long downloadId = DownloadAppUtils.downloadUpdateApkId;
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    DownloadManager downloadManager = (DownloadManager) context
                            .getSystemService(Context.DOWNLOAD_SERVICE);
                    c = downloadManager.query(query);
                    if (c.moveToFirst()) {
                        int status = c.getInt(c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_FAILED) {
                            downloadManager.remove(downloadId);

                        } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setDataAndType(
                                        Uri.parse("file://"
                                                + DownloadAppUtils.downloadUpdateApkFilePath),
                                        "application/vnd.android.package-archive");
                                //todo 针对不同的手机 以及sdk版本  这里的uri地址可能有所不同
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(i);
                            }
                        }
                    }
                }
            }/* else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {//点击通知取消下载
                DownloadManager downloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                //点击通知栏取消下载
                downloadManager.remove(ids);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (c != null) {
                c.close();
            }
        }
    }
}
