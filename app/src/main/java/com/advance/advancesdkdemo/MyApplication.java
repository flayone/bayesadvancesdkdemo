package com.advance.advancesdkdemo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.advance.AdvanceConfig;
import com.bun.miitmdid.core.JLibrary;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //聚合SDK初始化，请在bugly等sdk初始化之前
        AdvanceConfig.getInstance()
                .setDebug(true)//设置debug模式
                .setNeedPermissionCheck(true)//设置是否在请求穿山甲sdk广告前校验权限，防止无权限不返回广告，默认为false不去校验。
                //!!! 设置各个sdk的媒体id，目前后台也可以配置，后续会去掉后台的配置入口，统一在这里设置。
//                .setCsjAppId("你在穿山甲后台申请的的appId")
//                .setGdtMediaId("你在广点通后台申请的的MediaId")
//                .setMercuryMediaId("你在Mercury后台申请的的MediaId")
//                .setMercuryMediaKey("你在Mercury后台申请的的MediaKey")
                .setCsjAppId("5051624")
                .setGdtMediaId("1101152570")
                .setMercuryMediaId("100171")
                .setMercuryMediaKey("e1d0d3aaf95d3f1980367e75bc41141d")
                .initSDKs(this);

        //获取oaid，Android10 中手机唯一标识
        new MiitHelper(new MiitHelper.OaidUpdater() {
            @Override
            public void IdReceived(@NonNull String id) {
                //mercury sdk 需要在Android10以上机型设置oaid，否则可能无法获取广告
                Log.d("MiitHelper", "IdReceived OAID = " + id);
                AdvanceConfig.getInstance().setOaid(id);
            }
        }).getDeviceIds(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            JLibrary.InitEntry(base);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
