package com.advance.advancesdkdemo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.advance.AdvanceConfig;
import com.bun.miitmdid.core.JLibrary;
import com.mercury.sdk.core.config.AdConfigManager;

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

        //必须：获取oaid，Android10 中手机唯一标识
        new MiitHelper(new MiitHelper.OaidUpdater() {
            @Override
            public void IdReceived(@NonNull String id) {
                //mercury sdk 需要在Android10以上机型设置oaid，否则可能无法获取广告
                Log.d("MiitHelper", "IdReceived OAID = " + id);
                //必须：Mercury SDK设置项，设置OAID参数，否则Android10设备无法展示广告
                AdvanceConfig.getInstance().setOaid(id);
            }
        }).getDeviceIds(this);


        //可选：Mercury SDK设置项，true允许提前缓存部分广告素材，优化弱网表现。默认false
        AdConfigManager.getInstance().setNeedPreLoadMaterial(false);

    }

    //必须：OAID获取初始化
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
