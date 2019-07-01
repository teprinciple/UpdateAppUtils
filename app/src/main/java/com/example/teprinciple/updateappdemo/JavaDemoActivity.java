package com.example.teprinciple.updateappdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import constacne.UiType;
import listener.Md5CheckResultListener;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * desc: java使用实例
 * time: 2019/6/27
 * @author yk
 */
public class JavaDemoActivity extends AppCompatActivity {

    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
    private String updateTitle = "发现新版本V2.0.0";
    private String updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_demo);

        findViewById(R.id.btn_java).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.setCheckWifi(true);
                updateConfig.setNeedCheckMd5(true);
                updateConfig.setNotifyImgRes(R.drawable.ic_logo);

                UiConfig uiConfig = new UiConfig();
                uiConfig.setUiType(UiType.PLENTIFUL);

                UpdateAppUtils
                        .getInstance()
                        .apkUrl(apkUrl)
                        .updateTitle(updateTitle)
                        .updateContent(updateContent)
                        .uiConfig(uiConfig)
                        .updateConfig(updateConfig)
                        .setMd5CheckResultListener(new Md5CheckResultListener() {
                            @Override
                            public void onResult(boolean result) {

                            }
                        })
                        .setUpdateDownloadListener(new UpdateDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onDownload(int progress) {

                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onError(@NotNull Throwable e) {

                            }
                        })
                        .update();
            }
        });
    }
}
