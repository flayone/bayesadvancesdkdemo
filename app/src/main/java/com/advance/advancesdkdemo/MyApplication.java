package com.advance.advancesdkdemo;

import android.app.Application;
import android.content.Context;

import com.huawei.hms.ads.HwAds;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        boolean hasPri = getSharedPreferences("preference", Context.MODE_PRIVATE).getBoolean("agree_privacy", false);

        if (hasPri){
            //初始化聚合SDK
            AdvanceAD.initAD(this);
            //初始化HUAWEI Ads SDK，用于自定义SDK渠道
            HwAds.init(this);
        }
    }

}
