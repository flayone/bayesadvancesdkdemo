package com.advance.advancesdkdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
