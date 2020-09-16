package com.advance.advancesdkdemo;

import android.app.Application;
import com.advance.AdvanceConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //聚合SDK初始化
        AdvanceConfig.getInstance()
                //按需必填：设置各个sdk的媒体参数（如果有使用 setDefaultSdkSupplier 方法设置了打底渠道的话，这里必填！否则无法对应上广告位id和媒体id，未使用setDefaultSdkSupplier方法 设置打底可以不填，具体设置打底方法参看各广告位下API说明）
//                .setMercuryMediaId("你在 SDK后台申请的的MediaId")
//                .setMercuryMediaKey("你在 SDK后台申请的的MediaKey")
//                .setCsjAppId("你在穿山甲后台申请的的appId")
//                .setAppName("你在穿山甲后台申请的的应用名称")
//                .setGdtMediaId("你在广点通后台申请的的MediaId")
                //------- 以下为演示用id，开发者接入时一定要替换为自己的平台id  -------
//                .setMercuryMediaId("100171")
//                .setMercuryMediaKey("e1d0d3aaf95d3f1980367e75bc41141d")
//                .setCsjAppId("5051624")
//                .setAppName("聚合测试用")
//                .setGdtMediaId("1101152570")
                //------- 以上为演示用id，开发者接入时一定要替换为自己的平台id  -------
                //可选：设置debug开发调试模式,tag为：Advance SDK
                .setDebug(true)
                //可选：设置是否在请求穿山甲sdk广告前校验权限，防止无权限不返回广告，默认为false不去校验。
                .setNeedPermissionCheck(false)
                .initSDKs(this);

    }

}
