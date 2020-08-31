package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private String TAG = "DEMO BANNER";
    private AdvanceBanner advanceBanner;
    FrameLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        rl = findViewById(R.id.banner_layout);
        advanceBanner = new AdvanceBanner(this, rl, ADManager.getInstance().getBannerAdspotId());

        //可选：个性化设置，穿山甲个性化模板广告的尺寸属性，期望模板广告view的size，单位dp
        advanceBanner.setCsjExpressViewAcceptedSize(640, 100)
                //可选：穿山甲可以接受的尺寸大小，单位px
                .setCsjAcceptedSize(640, 100)
                //可选：设置广告刷新间隔（s）
                .setRefreshInterval(30);
        //推荐：核心事件监听回调
        advanceBanner.setAdListener(this);
        advanceBanner.loadStrategy();
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
        rl.removeAllViews();
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceBanner.destroy();
    }
}
