package com.advance.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceConfig;
import com.mercury.sdk.core.config.AdConfigManager;
import com.mercury.sdk.core.config.MercuryAD;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Mercury预缓存配置，开启预缓存需要设置媒体id
        AdConfigManager.getInstance().setMediaId("100171");
        MercuryAD.needPreLoadMaterial(true);

        //聚合SDK初始化
        AdvanceConfig.getInstance()
                .setAppName("聚合测试")//穿山甲必须：填入穿山甲平台创建媒体时的应用名称
                .setDebug(true)//可选：设置debug开发调试模式,tag为：Advance SDK
                .initSDKs(this);

    }

}
