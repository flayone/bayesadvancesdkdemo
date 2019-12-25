package com.bayescom.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //聚合SDK初始化，请在bugly等sdk初始化之前
        AdvanceConfig.getInstance().setOaid("").setDebug(true).initSDKs(this);
    }
}
