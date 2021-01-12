package com.advance.advancesdkdemo;

import android.app.Application;
import android.support.annotation.Keep;

import com.advance.AdvanceConfig;
import com.mercury.sdk.core.config.MercuryAD;

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
//                .setGdtMediaId("你在广点通后台申请的的 媒体ID")
                //聚合穿山甲必须：在穿山甲平台填入的媒体应用名称
                .setAppName("聚合测试")
                //可选：设置debug开发调试模式,tag为：Advance SDK
                .setDebug(true)
                .initSDKs(this);

        //如果媒体有cpt品牌订单、或者需求视频类开屏广告，请打开这里的素材预缓存，同时上面setMercuryMediaId()方法进行设置值
//        MercuryAD.needPreLoadMaterial(true);
    }

}
