package com.advance.advancesdkdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.custom.banner.CustomBannerActivity;
import com.advance.advancesdkdemo.custom.full.CustomFullScreenActivity;
import com.advance.advancesdkdemo.custom.interstitial.CustomInterstitialActivity;
import com.advance.advancesdkdemo.custom.nativ.NativeCustomizeActivity;
import com.advance.advancesdkdemo.custom.nativeexpress.CustomNativeExpressListActivity;
import com.advance.advancesdkdemo.custom.reward.CustomRewardActivity;
import com.advance.advancesdkdemo.custom.splash.CustomSplashActivity;
import com.advance.utils.LogUtil;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.mercury.sdk.core.config.AdConfigManager;
import com.qq.e.comm.managers.status.SDKStatus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner sdks;
    Button fullVideo;
    Button fullVideoCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 注意！：由于工信部对设备权限等隐私权限要求愈加严格,强烈推荐APP提前申请好权限再加载广告
         */
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29) {
            checkAndRequestPermission();
        }

        fullVideo = findViewById(R.id.fullvideo_button);
        fullVideoCus = findViewById(R.id.cus_fullvideo_button);

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
        sdks.setSelection(0);
        /**
         *  注意！：
         *  手动切换为在Demo中的演示功能，实际项目不需要切换功能，只需配置好聚合广告位ID即可。
         *  手动切换对于setUseCache(true); 的广告位，并不会立即生效，因为可能存在缓存的策略，会优先使用缓存，故第二次请求时切换才会生效。
         */
        sdks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sdkName = "";
                switch (position) {
                    case 0: //Mercury
                        sdkName = "Mercury";
                        ADManager.getInstance().setBannerAdspotId(Constants.Mercury.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Mercury.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Mercury.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Mercury.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Mercury.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Mercury.fullScreenVideoAdspotId);
                        ADManager.getInstance().setCustomNativeAdspotId(Constants.Mercury.customNativeAdspotId);
                        fullVideo.setEnabled(false);
                        fullVideoCus.setEnabled(false);
                        break;
                    case 1: //穿山甲
                        sdkName = "穿山甲";
                        ADManager.getInstance().setBannerAdspotId(Constants.Csj.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Csj.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Csj.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Csj.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Csj.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Csj.fullScreenVideoAdspotId);
                        ADManager.getInstance().setCustomNativeAdspotId(Constants.Csj.customNativeAdspotId);

                        //设置穿山甲允许直接下载的网络状态集合，可以在调用广告之前的任何时间来设置
                        int[] directDownloadNetworkType;
                        boolean csjDownloadForWifi = true;
                        if (csjDownloadForWifi) {//可以根据自己需求来设置状态值
                            directDownloadNetworkType = new int[]{TTAdConstant.NETWORK_STATE_WIFI};
                        } else {
                            directDownloadNetworkType = new int[]{TTAdConstant.NETWORK_STATE_4G};
                        }
                        AdvanceConfig.getInstance().setCsjDirectDownloadNetworkType(directDownloadNetworkType);
                        fullVideo.setEnabled(true);
                        fullVideoCus.setEnabled(true);

                        break;
                    case 2: //广点通
                        sdkName = "广点通";
                        ADManager.getInstance().setBannerAdspotId(Constants.Gdt.bannerAdspotId);
                        ADManager.getInstance().setInterstitialAdspotId(Constants.Gdt.interstitialAdspotId);
                        ADManager.getInstance().setNativeExpressAdspotId(Constants.Gdt.nativeExpressAdspotId);
                        ADManager.getInstance().setRewardAdspotId(Constants.Gdt.rewardAdspotId);
                        ADManager.getInstance().setSplashAdspotId(Constants.Gdt.splashAdspotId);
                        ADManager.getInstance().setFullScreenVideoAdspotId(Constants.Gdt.fullScreenVideoAdspotId);
                        ADManager.getInstance().setCustomNativeAdspotId(Constants.Gdt.customNativeAdspotId);
                        fullVideo.setEnabled(true);
                        fullVideoCus.setEnabled(true);
                        break;
                }
                LogUtil.AdvanceLog("sdk Demo 选择：" + sdkName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        // 缺少权限，进行申请
        if (lackedPermission.size() > 0) {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
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

    public void onCusBanner(View view) {
        startActivity(new Intent(this, CustomBannerActivity.class));
    }

    public void onCusNative(View view) {
        startActivity(new Intent(this, NativeCustomizeActivity.class));
    }


    public void cusSplash(View view) {
        startActivity(new Intent(this, CustomSplashActivity.class));
    }

    public void cusInterstitial(View view) {
        startActivity(new Intent(this, CustomInterstitialActivity.class));
    }

    public void cusExpress(View view) {
        startActivity(new Intent(this, CustomNativeExpressListActivity.class));
    }

    public void cusReward(View view) {
        startActivity(new Intent(this, CustomRewardActivity.class));
    }

    public void cusFullSV(View view) {
        startActivity(new Intent(this, CustomFullScreenActivity.class));
    }
}
