package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.model.AdvanceError;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private String TAG = "DEMO BANNER";
    private AdvanceBanner advanceBanner;
    FrameLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        rl = findViewById(R.id.banner_layout);


        advanceBanner = new AdvanceBanner(this, rl, Constants.Csj.bannerAdspotId);
        //推荐：核心事件监听回调
        advanceBanner.setAdListener(this);
        advanceBanner.loadStrategy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceBanner.destroy();
    }

    @Override
    public void onDislike() {
        DemoUtil.logAndToast(this, "广告关闭");

        rl.removeAllViews();
    }

    @Override
    public void onAdShow() {
        DemoUtil.logAndToast(this, "广告展现");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "策略选中SDK id = " + id);
    }

    @Override
    public void onAdClicked() {
        DemoUtil.logAndToast(this, "广告点击");
    }


    @Override
    public void onAdLoaded() {
        DemoUtil.logAndToast(this, "广告加载成功");
    }

}
