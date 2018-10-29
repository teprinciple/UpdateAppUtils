package model;

import util.UpdateAppUtils;

public class UpdateBean {

    private int downloadBy = UpdateAppUtils.DOWNLOAD_BY_APP; // 下载方式：默认app下载
    private String apkPath = ""; // apk 下载地址
    private String updateInfo = ""; // 更新说明

    private Boolean isForce = false; // 是否强制更新

    private String serverVersionName = ""; // 服务器上版本名
    private int serverVersionCode = 0; // 服务器上版本号

    private String localVersionName = ""; // 当前本地版本名
    private int localVersionCode = 0; // 当前本地版本号

    private int checkBy = UpdateAppUtils.CHECK_BY_VERSION_CODE; // 检查方式 按版本名或版本号

    private boolean showNotification = true; // 是否在通知栏显示

    public boolean isShowNotification() {
        return showNotification;
    }

    public void setShowNotification(boolean showNotification) {
        this.showNotification = showNotification;
    }

    public int getDownloadBy() {
        return downloadBy;
    }

    public void setDownloadBy(int downloadBy) {
        this.downloadBy = downloadBy;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }

    public String getServerVersionName() {
        return serverVersionName;
    }

    public void setServerVersionName(String serverVersionName) {
        this.serverVersionName = serverVersionName;
    }

    public int getServerVersionCode() {
        return serverVersionCode;
    }

    public void setServerVersionCode(int serverVersionCode) {
        this.serverVersionCode = serverVersionCode;
    }

    public String getLocalVersionName() {
        return localVersionName;
    }

    public void setLocalVersionName(String localVersionName) {
        this.localVersionName = localVersionName;
    }

    public int getLocalVersionCode() {
        return localVersionCode;
    }

    public void setLocalVersionCode(int localVersionCode) {
        this.localVersionCode = localVersionCode;
    }

    public int getCheckBy() {
        return checkBy;
    }

    public void setCheckBy(int checkBy) {
        this.checkBy = checkBy;
    }
}
