package com.advance.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.AdvanceConfig;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.mercury.sdk.core.config.AdConfigManager;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private String TAG = "DEMO BANNER";
    private AdvanceBanner advanceBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        RelativeLayout rl = findViewById(R.id.banner_layout);
        advanceBanner = new AdvanceBanner(this, rl, ADManager.getInstance().getBannerAdspotId());

        //可选：个性化设置，穿山甲个性化模板广告的尺寸属性，期望模板广告view的size，刷新间隔（s）
        advanceBanner.setCsjExpressViewAcceptedSize(640, 100)
                .setRefreshInterval(60);
        //推荐：核心事件监听回调
        advanceBanner.setAdListener(this);
        //推荐：设置是否开启策略缓存
        advanceBanner.enableStrategyCache(true);
        //必须：设置打底SDK参数
        advanceBanner.setDefaultSdkSupplier(new SdkSupplier("10000396", AdvanceSupplierID.MERCURY));
        advanceBanner.loadAd();

    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "SHOW");
        Toast.makeText(this, "广告展现", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {

        Log.d(TAG, "Failed");
        Toast.makeText(this, "广告失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d(TAG, "Clicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDislike() {
        RelativeLayout rl = findViewById(R.id.banner_layout);
        rl.removeAllViews();
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceBanner.destroy();

    }
}
