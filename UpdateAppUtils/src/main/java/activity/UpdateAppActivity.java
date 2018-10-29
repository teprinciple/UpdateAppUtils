package activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import teprinciple.updateapputils.R;

public class UpdateAppActivity extends AppCompatActivity {

    private static String KEY_OF_INTENT_DES = "KEY_OF_INTENT_DES";

    private static String KEY_OF_INTENT_IS_FORCE = "KEY_OF_INTENT_IS_FORCE";

    private static String KEY_OF_INTENT_VERSION_NAME = "KEY_OF_INTENT_VERSION_NAME";

    public static void launch(Context context, Boolean isForce, String des, String versionName) {
        Intent intent = new Intent(context, UpdateAppActivity.class);
        intent.putExtra(KEY_OF_INTENT_IS_FORCE, isForce);
        intent.putExtra(KEY_OF_INTENT_DES, des);
        intent.putExtra(KEY_OF_INTENT_VERSION_NAME, versionName);
        context.startActivity(intent);
    }

    private TextView content;
    private TextView sureBtn;
    private TextView cancleBtn;

    private String des = "";
    private Boolean isForce = false;
    private String versionName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.view_version_tips_dialog);

        des = getIntent().getStringExtra(KEY_OF_INTENT_DES);
        isForce = getIntent().getBooleanExtra(KEY_OF_INTENT_IS_FORCE, false);

        initView();
        initOperation();
    }


    private void initView() {
        sureBtn = (TextView) findViewById(R.id.dialog_confirm_sure);
        cancleBtn = (TextView) findViewById(R.id.dialog_confirm_cancle);
        content = (TextView) findViewById(R.id.dialog_confirm_title);


        String contentStr = "发现新版本:" + versionName + "\n是否下载更新?";
        if (!TextUtils.isEmpty(des)) {
            contentStr = "发现新版本:" + versionName + "是否下载更新?\n\n" + des;
        }

        content.setText(contentStr);

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initOperation() {

        // todo 在这里面 适配6.0 7.0 8.0

        // todo 增加强制更新 下载进度条
    }

    /**
     * 检测wifi是否连接
     */
    private static boolean isWifiConnected(Context context) {
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
