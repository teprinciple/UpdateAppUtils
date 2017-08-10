package com.example.teprinciple.updateappdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import teprinciple.library.util.UpdateAppUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void updateApp(View view) {

        //服务器apk path,这里放了百度云盘的apk 作为测试
        String apkPath = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";

        //首先获取自己的服务器上的版本号，以及apk的下载地址
        UpdateAppUtils.updateApp(this,2,apkPath,"测试2.0");

    }
}
