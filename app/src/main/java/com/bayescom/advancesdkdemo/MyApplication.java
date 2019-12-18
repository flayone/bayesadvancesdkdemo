package com.bayescom.advancesdkdemo;

import android.app.Application;

import com.bayesadvance.AdvanceConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //聚合SDK初始化
        AdvanceConfig.getInstance().setOaid("").setDebug(true).initSDKs(this);
    }
}
