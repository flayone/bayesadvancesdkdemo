package com.advance.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceSDK;
import com.mercury.sdk.core.config.MercuryAD;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //必要配置：初始化聚合SDK，三个参数依次为context上下文，appId媒体id，isDebug调试模式开关
        AdvanceSDK.initSDK(this,"100171",true);
        //推荐配置：允许Mercury预缓存素材
        MercuryAD.needPreLoadMaterial(true);
    }

}
