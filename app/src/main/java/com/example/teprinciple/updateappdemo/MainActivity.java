package com.example.teprinciple.updateappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    //服务器apk path,这里放了百度云盘的apk 作为测试
    String apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_9.6.63.apk";
    private int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void updateApp(View view) {
        realUpdate(1);
    }

    public void downloadByWeb(View view) {
        realUpdate(2);
    }


    public void forceUpdate(View view) {
        realUpdate(3);
    }


    public void checkByName(View view) {
        realUpdate(4);
    }

    public void kotlin(View view) {
        realUpdate(5);
    }



    private void realUpdate(int code) {
        this.code = code;
        switch (code) {
            case 1:
                updat1();
                break;
            case 2:
                update2();
                break;
            case 3:
                update3();
                break;
            case 4:
                update4();
                break;
            case 5:
                startActivity(new Intent(this,KotlinDemoActivity.class));
                break;

        }
    }


    //基本更新
    private void updat1() {

//        UpdateAppUtils
//                .getInstance()
//                .apkPath(apkPath)
//                .serverVersionName("4.0")
//                .downloadBy(DownLoadBy.APP)
//                .update(this);

    }

    //通过浏览器下载
    private void update2() {
//        update.UpdateAppUtils.Companion.from(this)
//                .serverVersionCode(2)
//                .serverVersionName("2.0")
//                .apkPath(apkPath)
//                .downloadBy(update.UpdateAppUtils.Companion.getDOWNLOAD_BY_BROWSER())
//                .update();
    }

    //强制更新
    private void update3() {

//        update.UpdateAppUtils.
//                .serverVersionCode(2)
//                .serverVersionName("2.0")
//                .apkPath(apkPath)
//                .isForce(true)
//                .update();
    }

    //根据versionName判断跟新
    private void update4() {



//        update.UpdateAppUtils
//                .checkBy(update.UpdateAppUtils.Companion.getCHECK_BY_VERSION_NAME())
//                .serverVersionName("2.0")
//                .serverVersionCode(2)
//                .apkPath(apkPath)
//                .downloadBy(update.UpdateAppUtils.Companion.getDOWNLOAD_BY_BROWSER())
//                .isForce(true)
//                .update();
    }
}
