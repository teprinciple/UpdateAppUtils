package com.example.teprinciple.updateappdemo.updateapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.teprinciple.updateappdemo.customview.ConfirmDialog;
import com.example.teprinciple.updateappdemo.customview.feature.Callback;


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

//    public static void getAPPServerVersion(Context context, final VersionCallBack callBack){
////        HttpUtil.getObject(Api.GETVERSION.mapClear().addBody(), VersionInfo.class, new HttpUtil.ObjectCallback() {
////            @Override
////            public void result(boolean b, @Nullable Object obj) {
////                if (b){
//////                        BaseApplication.getIntance().checkVersion();
////                        callBack.callBack((VersionInfo) obj);
////                }
////            }
////        });
//    }

    /**
     * 获取服务器的版本号
     * @param context  上下文
     * @param callBack 为了控制线程，需要获取服务器版本号成功的回掉
     */
    public static void getAPPServerVersion(Context context, final VersionCallBack callBack){
        //todo 自己的网络请求获取 服务器上apk的版本号（需要与后台协商好）
        callBack.callBack(2);
    }

    /**
     * 更新APP
     * @param context
     */
    public static void updateApp(final Context context){
        getAPPServerVersion(context, new VersionCallBack() {
            @Override
            public void callBack(final int versionCode) {
                if (versionCode > 0){
                    Log.i("this","版本信息：当前"+getAPPLocalVersion(context)+",服务器："+ versionCode);
                    if (versionCode > getAPPLocalVersion(context)){
                        ConfirmDialog dialog = new ConfirmDialog(context, new Callback() {
                            @Override
                            public void callback() {
                                //服务器apk path,这里放了百度云盘的apk 作为测试
                                String apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";
                                DownloadAppUtils.downloadForAutoInstall(context, apkPath, "demo.apk", "更新demo");
//                                DownloadAppUtils.downloadForWebView(context,apkPath);
                            }
                        });
                        dialog .setContent("发现新版本:demo2.0\n是否下载更新?");
                        dialog.setCancelable(false);
                        dialog .show();
                    }
                }
            }
        });
    }

    public interface VersionCallBack{
        void callBack(int versionCode);
    }
}
