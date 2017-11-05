package util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

import teprinciple.updateapputils.R;

/**
 * Created by Teprinciple on 2017/11/3.
 */

 public class UpdateAppReceiver extends BroadcastReceiver {

    private Drawable mDrawable;

    @Override
    public void onReceive(Context context, Intent intent) {

        int notifyId = 1001;
        int progress = intent.getIntExtra("progress", 0);
        String title = intent.getStringExtra("title");


        PackageManager manager = context.getApplicationContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
            mDrawable = info.applicationInfo.loadIcon(manager);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        NotificationManager nm = null;
        if (UpdateAppUtils.showNotification){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle(title);

//            builder.setSmallIcon()

            builder.setSmallIcon(R.drawable.ic_launcher);
            builder.setProgress(100,progress,false);

            Notification notification = builder.build();
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(notifyId,notification);
        }


        if (progress == 100){
            if (nm!=null)nm.cancel(notifyId);

            if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
                if ( UpdateAppUtils.needFitAndroidN &&  Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
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
}
