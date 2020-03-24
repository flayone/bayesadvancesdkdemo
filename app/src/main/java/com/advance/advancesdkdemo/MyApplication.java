package com.advance.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.mercury.sdk.core.config.AdConfigManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Mercury SDK 3.1.0以后支持资源预缓存，可提升收益，建议开启
        AdConfigManager.getInstance().setNeedPreLoadMaterial(true);

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
