package com.advance.advancesdkdemo;

import android.app.Application;
import com.advance.AdvanceConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //聚合SDK初始化，请在bugly等sdk初始化之前
        AdvanceConfig.getInstance()
                //可选：设置debug开发调试模式
                .setDebug(true)
                //可选：设置是否在请求穿山甲sdk广告前校验权限，防止无权限不返回广告，默认为false不去校验。
                .setNeedPermissionCheck(false)
                //必须：设置各个sdk的媒体参数
//                .setCsjAppId("你在穿山甲后台申请的的appId")
//                .setGdtMediaId("你在广点通后台申请的的MediaId")
//                .setMercuryMediaId("你在Mercury SDK后台申请的的MediaId")
//                .setMercuryMediaKey("你在Mercury SDK后台申请的的MediaKey")
                //------- 以下为演示用id，开发者接入时一定要替换为自己的平台id  -------
                .setCsjAppId("5051624")
                .setGdtMediaId("1101152570")
                .setMercuryMediaId("100171")
                .setMercuryMediaKey("e1d0d3aaf95d3f1980367e75bc41141d")
                .initSDKs(this);

    }


}
