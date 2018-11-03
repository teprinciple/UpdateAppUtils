package activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import customview.ConfirmDialog;
import model.UpdateBean;
import teprinciple.updateapputils.R;
import util.DownloadAppUtils;
import util.UpdateAppService;
import util.UpdateAppUtils;

public class UpdateAppActivity extends AppCompatActivity {

    private static String KEY_OF_INTENT_UPDATE_BEAN = "KEY_OF_INTENT_UPDATE_BEAN";


    public static void launch(Context context, UpdateBean updateBean) {
        Intent intent = new Intent(context, UpdateAppActivity.class);
        intent.putExtra(KEY_OF_INTENT_UPDATE_BEAN, updateBean);
        context.startActivity(intent);
    }

    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    private UpdateBean updateBean;


    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_version_tips_dialog);

        updateBean = getIntent().getParcelableExtra(KEY_OF_INTENT_UPDATE_BEAN);

        initView();
        initOperation();
    }


    private void initView() {
        sureBtn = (TextView) findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView) findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) findViewById(R.id.dialog_confirm_title);


        String contentStr = "发现新版本:" + updateBean.getServerVersionName() + "\n是否下载更新?";
        if (!TextUtils.isEmpty(updateBean.getUpdateInfo())) {
            contentStr = "发现新版本:" + updateBean.getServerVersionName() + "是否下载更新?\n\n" + updateBean.getUpdateInfo();
        }

        content.setText(contentStr);

        if (updateBean.getForce()) {
            cancleBtn.setText("退出");
        } else {
            cancleBtn.setText("取消");
        }

    }


    private void initOperation() {


        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateBean.getForce()) {
                    System.exit(0);
                } else {
                    finish();
                }
            }
        });

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preDownLoad();
            }
        });
    }


    /**
     * 预备下载 进行 6.0权限检查
     */
    private void preDownLoad() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            download();
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                download();

            } else {//申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
        }
    }


    private void download() {

        startService(new Intent(this, UpdateAppService.class));

        if (updateBean.getDownloadBy() == UpdateAppUtils.DOWNLOAD_BY_APP) {
            if (isWifiConnected(this)) {

                DownloadAppUtils.download(this, updateBean.getApkPath(), updateBean.getServerVersionName());
            } else {
                new ConfirmDialog(this, new ConfirmDialog.Callback() {
                    @Override
                    public void callback(int position) {
                        if (position == 1) {
                            DownloadAppUtils.download(UpdateAppActivity.this, updateBean.getApkPath(), updateBean.getServerVersionName());
                        } else {
                            if (updateBean.getForce()) {
                                System.exit(0);
                            } else {
                                finish();
                            }
                        }
                    }
                }).setContent("目前手机不是WiFi状态\n确认是否继续下载更新？").show();
            }
        } else if (updateBean.getDownloadBy() == UpdateAppUtils.DOWNLOAD_BY_BROWSER) {
            DownloadAppUtils.downloadForWebView(this, updateBean.getApkPath());
        }

        finish();
    }


    /**
     * 权限请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download();
                } else {
                    new ConfirmDialog(this, new ConfirmDialog.Callback() {
                        @Override
                        public void callback(int position) {
                            if (position == 1) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                }
                break;
        }
    }

    /**
     * 检测wifi是否连接
     */
    private boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }
}
