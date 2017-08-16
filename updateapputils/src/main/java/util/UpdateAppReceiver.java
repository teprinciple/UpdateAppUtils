package util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;


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
        Cursor c = null;

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
                            File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(
                                        context, context.getPackageName() + ".fileprovider", apkFile);
                                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                i.setDataAndType(Uri.fromFile(apkFile),
                                        "application/vnd.android.package-archive");
                            }
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                }
                c.close();
            }
        }


    }
}
