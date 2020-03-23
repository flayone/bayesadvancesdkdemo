package com.advance.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //聚合SDK初始化，请在bugly等sdk初始化之前
        AdvanceConfig.getInstance().setOaid("").setDebug(true).initSDKs(this);

        //设置穿山甲允许直接下载的网络状态集合，可以在调用广告之前的任何时间来设置
        int[] directDownloadNetworkType;
        boolean csjDownloadForWifi = true;
        if (csjDownloadForWifi) {//可以根据自己需求来设置状态值
            directDownloadNetworkType = new int[]{TTAdConstant.NETWORK_STATE_WIFI};
        } else {
            directDownloadNetworkType = new int[]{TTAdConstant.NETWORK_STATE_4G};
        }
        AdvanceConfig.getInstance().setCsjDirectDownloadNetworkType(directDownloadNetworkType);
    }
}
