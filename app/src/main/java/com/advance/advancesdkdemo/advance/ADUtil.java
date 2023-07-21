package com.advance.advancesdkdemo.advance;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.advance.advancesdkdemo.BuildConfig;
import com.advance.advancesdkdemo.MyApplication;
import com.bytedance.msdk.api.v2.GMAdConfig;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMLocation;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMPangleOption;
import com.bytedance.msdk.api.v2.GMPrivacyConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Advance SDK广告加载逻辑统一处理类
 */
public class ADUtil {


//    5329994


    //sdk 初始化
    public static void init(Context context) {

        try {


            GMAdConfig.Builder builder = new GMAdConfig.Builder()
                    .setAppId("5329994");
//            配置了自定义的兜底config信息
//            if (AdMoreConfig.getInstance().gmCustomConfig != null) {
//                builder.setCustomLocalConfig(AdMoreConfig.getInstance().gmCustomConfig);
//            }
            builder.setAppName("app")
                    .setDebug(true)
                    //                .setPublisherDid(getAndroidId(context))
                    .setOpenAdnTest(false)
                    //                .setConfigUserInfoForSegment(userInfo)
                    .setPangleOption(new GMPangleOption.Builder()
                            .setIsPaid(false)
                            //                        .setTitleBarTheme(GMAdConstant.TITLE_BAR_THEME_DARK)
                            .setAllowShowNotify(true)
                            .setAllowShowPageWhenScreenLock(true)
                            .setDirectDownloadNetworkType(GMAdConstant.NETWORK_STATE_WIFI, GMAdConstant.NETWORK_STATE_4G)
                            .setIsUseTextureView(true)
                            .setNeedClearTaskReset()
                            .setKeywords("")
                            .build());

            GMMediationAdSdk.initialize(context, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一处理打印日志，并且toast提示。
     *
     * @param context 上下文
     * @param msg     需要显示的内容
     */
    public static void logAndToast(Context context, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("[AdvanceAD][logAndToast]", msg);
            //如果不想弹出toast可以在此注释掉下面代码
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 统一处理打印日志，并且toast提示。
     *
     * @param msg 需要显示的内容
     */
    public static void logAndToast(String msg) {
        Log.d("Advance SDK: [DemoUtil]", msg);
        //如果不想弹出toast可以在此注释掉下面代码
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
