package com.advance.advancesdkdemo;

import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化SDK
        AdvanceAD.initAD(this);
        // 初始化HUAWEI Ads SDK
        HwAds.init(this);
    }

}
