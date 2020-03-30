package com.advance.advancesdkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.utils.LogUtil;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.mercury.sdk.core.config.AdConfigManager;
import com.qq.e.comm.managers.status.SDKStatus;

public class MainActivity extends AppCompatActivity {

    Spinner sdks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String csjV = TTAdSdk.getAdManager().getSDKVersion();
        String merV = AdConfigManager.getInstance().getSDKVersion();
        String gdtV = SDKStatus.getSDKVersion();
        String av = AdvanceConfig.AdvanceSdkVersion;

        TextView tv = findViewById(R.id.tv_version);
        tv.setText("聚合 SDK 版本号： " + av + "\n" +
                "Mercury SDK 版本号： " + merV + "\n" +
                "穿山甲 SDK 版本号： " + csjV + "\n" +
                "广点通 SDK 版本号： " + gdtV + "\n"
        );
        sdks = findViewById(R.id.sp_sdk);
        sdks.setSelection(2);
        /**
         *  注意：手动切换对于setUseCache(true); 的广告位，并不会立即生效，因为可能存在缓存的策略，会优先使用缓存，故第二次请求时切换才会生效。
         */
        sdks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sdkName = "";
                switch (position) {
                    case 0: //Mercury
                        sdkName = "Mercury";
                        ADManager.getInstance().setMediaId(Constants.Mercury.mediaId);
                        ADManager.getInstance().setBannerAdspotId(Constants.Mercury.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Mercury.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Mercury.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Mercury.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Mercury.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Mercury.fullScreenVideoAdspotId);
                        break;
                    case 1: //穿山甲
                        sdkName = "穿山甲";
                        ADManager.getInstance().setMediaId(Constants.Csj.mediaId);
                        ADManager.getInstance().setBannerAdspotId(Constants.Csj.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Csj.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Csj.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Csj.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Csj.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Csj.fullScreenVideoAdspotId);
                        break;
                    case 2: //广点通
                        sdkName = "广点通";
                        ADManager.getInstance().setMediaId(Constants.Gdt.mediaId);
                        ADManager.getInstance().setBannerAdspotId(Constants.Gdt.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Gdt.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Gdt.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Gdt.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Gdt.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Gdt.fullScreenVideoAdspotId);
                        break;
                }
                LogUtil.AdvanceLog("sdk Demo 选择：" + sdkName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onBanner(View view) {
        startActivity(new Intent(this, BannerActivity.class));
    }

    public void onSplash(View view) {
        startActivity(new Intent(this, SplashActivity.class));
    }

    public void onNativeExpress(View view) {
        startActivity(new Intent(this, NativeExpressActivity.class));
    }

    public void onRewardVideo(View view) {
        startActivity(new Intent(this, RewardVideoActivity.class));
    }

    public void onNativeExpressRecyclerView(View view) {
        startActivity(new Intent(this, NativeExpressRecyclerViewActivity.class));
    }

    public void onInterstitial(View view) {
        startActivity(new Intent(this, InterstitialActivity.class));
    }

    public void onFullVideo(View view) {
        startActivity(new Intent(this, FullScreenVideoActivity.class));
    }
}
