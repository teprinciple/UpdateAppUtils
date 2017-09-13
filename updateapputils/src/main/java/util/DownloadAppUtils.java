package util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;


/**
 *Created by Teprinciple on 2016/12/13.
 */
public class DownloadAppUtils {
    private static final String TAG = DownloadAppUtils.class.getSimpleName();
    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径

    /**
     * 通过浏览器下载APK包
     * @param context
     * @param url
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 下载更新apk包
     * 权限:1,<uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" />
     * @param context
     * @param url
     */
    public static void downloadForAutoInstall(final Context context, String url, String fileName, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        final String packageName = "com.android.providers.downloads";
        int state = context.getPackageManager().getApplicationEnabledSetting(packageName);
        //检测下载管理器是否被禁用
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("温馨提示").setMessage
                    ("系统下载管理器被禁止，需手动打开").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + packageName));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        context.startActivity(intent);
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else {



            try {
                Uri uri = Uri.parse(url);
                DownloadManager downloadManager = (DownloadManager) context
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                //在通知栏中显示
                request.setVisibleInDownloadsUi(true);
                request.setTitle(title);

                // VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
                // VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
                // VISIBILITY_HIDDEN:                    始终不显示通知
                if (!UpdateAppUtils.showNotification)
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);


                Long mobile = downloadManager.getMaxBytesOverMobile(context);

                String filePath = null;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    Log.i(TAG,"没有SD卡");
                    return;
                }

                downloadUpdateApkFilePath = filePath + File.separator + fileName;
                deleteFile(downloadUpdateApkFilePath);// 若存在，则删除
                Uri fileUri = Uri.fromFile(new File(downloadUpdateApkFilePath));
                request.setDestinationUri(fileUri);
                downloadUpdateApkId = downloadManager.enqueue(request);

            } catch (Exception e) {
                e.printStackTrace();
                downloadForWebView(context, url);
            }finally {
//            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }


        }


    }


    private static boolean deleteFile(String fileStr) {
        File file = new File(fileStr);
        return file.delete();
    }
}
