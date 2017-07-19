package teprinciple.library.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import teprinciple.library.customview.ConfirmDialog;
import teprinciple.library.customview.feature.Callback;


/**
 * Created by Teprinciple on 2016/11/15.
 */
public class UpdateAppUtil {

    /**
     * 获取apk的版本号 currentVersionCode
     * @param ctx
     * @return
     */
    public static int getAPPLocalVersion(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }




    public static void updateApp(final Activity activity, int serverVersion, final String apkPath, final String appName){

        if (serverVersion>getAPPLocalVersion(activity)){

            ConfirmDialog dialog = new ConfirmDialog(activity, new Callback() {
                @Override
                public void callback() {

                    DownloadAppUtils.downloadForAutoInstall(activity, apkPath, "demo.apk", appName);
//                                DownloadAppUtils.downloadForWebView(activity,apkPath);
                }
            });
            dialog .setContent("发现新版本:"+appName+"\n是否下载更新?");
            dialog.setCancelable(false);
            dialog .show();
        }
    }
}
