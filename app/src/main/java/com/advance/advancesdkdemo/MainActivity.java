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
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.custom.CustomActivity;
import com.baidu.mobads.sdk.api.AdSettings;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.kwad.sdk.api.KsAdSDK;
import com.mercury.sdk.core.config.MercuryAD;
import com.qq.e.comm.managers.status.SDKStatus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button fullVideo;
    Button fullVideoCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 注意！：由于工信部对设备权限等隐私权限要求愈加严格，强烈推荐APP提前申请好权限，且用户同意隐私政策后再加载广告
         */
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29) {
            checkAndRequestPermission();
        }

        fullVideo = findViewById(R.id.fullvideo_button);
        fullVideoCus = findViewById(R.id.cus_fullvideo_button);

        String csjV = TTAdSdk.getAdManager().getSDKVersion();
        String merV = MercuryAD.getVersion();
        String gdtV = SDKStatus.getSDKVersion();
        String bdV = AdSettings.getSDKVersion() + "";
        String ksV = KsAdSDK.getSDKVersion();
        String av = AdvanceConfig.AdvanceSdkVersion;

        TextView tv = findViewById(R.id.tv_version);
        tv.setText("聚合 SDK 版本号： " + av + "\n" + "\n" +
                "Mercury SDK 版本号： " + merV + "\n" +
                "穿山甲 SDK 版本号： " + csjV + "\n" +
                "广点通 SDK 版本号： " + gdtV + "\n" +
                "百度 SDK 版本号： " + bdV + "\n" +
                "快手 SDK 版本号： " + ksV + "\n"
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
        new AdvanceAD(this).loadReward();
    }

    public void onNativeExpressRecyclerView(View view) {
        startActivity(new Intent(this, NativeExpressRecyclerViewActivity.class));
    }

    public void onInterstitial(View view) {
        new AdvanceAD(this).loadInterstitial();
    }

    public void onFullVideo(View view) {
        new AdvanceAD(this).loadFullVideo();
    }

    public void cusAD(View view) {
        startActivity(new Intent(this, CustomActivity.class));
    }
}
