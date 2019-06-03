package util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import activity.UpdateAppActivity;
import model.UpdateBean;


/**
 * Created by Teprinciple on 2016/11/15.
 */
public class UpdateAppUtils {

    private final String TAG = "UpdateAppUtils";

    public static final int CHECK_BY_VERSION_NAME = 1001;
    public static final int CHECK_BY_VERSION_CODE = 1002;
    public static final int DOWNLOAD_BY_APP = 1003;
    public static final int DOWNLOAD_BY_BROWSER = 1004;

    private Activity activity;

    public static boolean showNotification = true;

   // 将所有的 属性 放入model
    private UpdateBean updateBean = new UpdateBean();

    private UpdateAppUtils(Activity activity) {
        this.activity = activity;
        getAPPLocalVersion(activity);
    }

    public static UpdateAppUtils from(Activity activity) {
        return new UpdateAppUtils(activity);
    }

    public UpdateAppUtils checkBy(int checkBy) {
        updateBean.setCheckBy(checkBy);
        return this;
    }

    public UpdateAppUtils apkPath(String apkPath) {
        updateBean.setApkPath(apkPath);
        return this;
    }

    public UpdateAppUtils downloadBy(int downloadBy) {
        updateBean.setDownloadBy(downloadBy);
        return this;
    }

    public UpdateAppUtils showNotification(boolean showNotification) {
        updateBean.setShowNotification(showNotification);
        this.showNotification = showNotification;
        return this;
    }

    public UpdateAppUtils updateInfo(String updateInfo) {
        updateBean.setUpdateInfo(updateInfo);
        return this;
    }


    public UpdateAppUtils serverVersionCode(int serverVersionCode) {
        updateBean.setServerVersionCode(serverVersionCode);
        return this;
    }

    public UpdateAppUtils serverVersionName(String serverVersionName) {
        updateBean.setServerVersionName(serverVersionName);
        return this;
    }

    public UpdateAppUtils isForce(boolean isForce) {
        updateBean.setForce(isForce);
        return this;
    }


    /**
     * 获取apk的版本号 currentVersionCode
     */
    private void getAPPLocalVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);

            updateBean.setLocalVersionCode(info.versionCode);
            updateBean.setLocalVersionName(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查更新
     */
    public void update() {

        switch (updateBean.getCheckBy()) {
            case CHECK_BY_VERSION_CODE:
                if (updateBean.getServerVersionCode() > updateBean.getLocalVersionCode()) {
                    toUpdate();
                } else {
                    Log.i(TAG, "当前版本是最新版本" + updateBean.getServerVersionCode() + "/" + updateBean.getServerVersionName());
                }
                break;

            case CHECK_BY_VERSION_NAME:
                if (!updateBean.getServerVersionName().equals(updateBean.getLocalVersionName())) {
                    toUpdate();
                } else {
                    Log.i(TAG, "当前版本是最新版本" + updateBean.getServerVersionCode() + "/" + updateBean.getServerVersionName());
                }
                break;
        }
    }

    /**
     * 更新
     */
    private void toUpdate() {
        UpdateAppActivity.Companion.launch(activity, updateBean);
    }
}