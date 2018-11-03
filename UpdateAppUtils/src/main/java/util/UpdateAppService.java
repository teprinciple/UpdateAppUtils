package util;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * desc:
 * author: teprinciple on 2018/11/3.
 */
public class UpdateAppService extends Service {

    private BroadcastReceiver receiver = new UpdateAppReceiver();


    @Override
    public void onCreate() {
        super.onCreate();

        // 动态注册receiver 适配8.0 receiver 静态注册没收不到广播
        IntentFilter intentFilter = new IntentFilter("teprinciple.update");
        registerReceiver(receiver, intentFilter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver); // 注销广播
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
