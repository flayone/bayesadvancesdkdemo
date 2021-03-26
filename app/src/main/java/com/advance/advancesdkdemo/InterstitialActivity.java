package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceInterstitial;
import com.advance.AdvanceInterstitialListener;
import com.advance.model.AdvanceError;

public class InterstitialActivity extends AppCompatActivity implements AdvanceInterstitialListener {
    private AdvanceInterstitial advanceInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        //初始化
        advanceInterstitial = new AdvanceInterstitial(this, Constants.Csj.interstitialAdspotId);
        //推荐：核心事件监听回调
        advanceInterstitial.setAdListener(this);
    }

    public void loadAd(View view) {
        advanceInterstitial.loadStrategy();

    }

    public void showAd(View view) {
        advanceInterstitial.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceInterstitial.destroy();
    }

    @Override
    public void onAdClose() {
        DemoUtil.logAndToast(this, "广告关闭");
    }

    @Override
    public void onAdReady() {
        DemoUtil.logAndToast(this, "广告就绪");
    }

    @Override
    public void onAdShow() {
        DemoUtil.logAndToast(this, "广告展示");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "onSdkSelected = " + id);
    }

    @Override
    public void onAdClicked() {
        DemoUtil.logAndToast(this, "广告点击");
    }

}
