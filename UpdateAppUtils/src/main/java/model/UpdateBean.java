package model;

import android.os.Parcel;
import android.os.Parcelable;

import util.UpdateAppUtils;

public class UpdateBean implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.downloadBy);
        dest.writeString(this.apkPath);
        dest.writeString(this.updateInfo);
        dest.writeValue(this.isForce);
        dest.writeString(this.serverVersionName);
        dest.writeInt(this.serverVersionCode);
        dest.writeString(this.localVersionName);
        dest.writeInt(this.localVersionCode);
        dest.writeInt(this.checkBy);
        dest.writeByte(this.showNotification ? (byte) 1 : (byte) 0);
    }

    public UpdateBean() {
    }

    protected UpdateBean(Parcel in) {
        this.downloadBy = in.readInt();
        this.apkPath = in.readString();
        this.updateInfo = in.readString();
        this.isForce = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.serverVersionName = in.readString();
        this.serverVersionCode = in.readInt();
        this.localVersionName = in.readString();
        this.localVersionCode = in.readInt();
        this.checkBy = in.readInt();
        this.showNotification = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UpdateBean> CREATOR = new Parcelable.Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel source) {
            return new UpdateBean(source);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };
}
