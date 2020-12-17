package com.advance.advancesdkdemo;

import android.app.Application;

import com.advance.AdvanceConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //聚合SDK初始化
        AdvanceConfig.getInstance()
                //按需必填：只有设置了打底广告，才需要填写打底渠道的媒体参数。比如设置广点通的广告位为打底，仅需设置setGdtMediaId 即可。
//                .setMercuryMediaId("你在聚合SDK后台申请的的 媒体ID")
//                .setMercuryMediaKey("你在聚合SDK后台申请的的 媒体Key")
//                .setCsjAppId("你在穿山甲后台申请的的 媒体ID")
//                .setAppName("你在穿山甲后台申请的的 媒体应用名称")
//                .setGdtMediaId("你在广点通后台申请的的 媒体ID")
                //可选：设置debug开发调试模式,tag为：Advance SDK
                .setDebug(true)
                //可选：设置是否在请求穿山甲sdk广告前校验权限，防止无权限不返回广告，默认为false不去校验。
                .setNeedPermissionCheck(false)
                .initSDKs(this);

    }

}
