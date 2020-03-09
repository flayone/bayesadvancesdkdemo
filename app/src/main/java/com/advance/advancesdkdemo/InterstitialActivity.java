package com.advance.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceInterstitial;
import com.advance.AdvanceInterstitialListener;
import com.advance.model.SdkSupplier;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;

public class InterstitialActivity extends AppCompatActivity implements AdvanceInterstitialListener, UnifiedInterstitialMediaListener {
    private AdvanceInterstitial advanceInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        advanceInterstitial = new AdvanceInterstitial(this, Constants.mediaId, Constants.interstitialAdspotId);
        //期望模板广告view的size,单位dp。高度为0代表自适应
        advanceInterstitial.setCsjExpressViewAcceptedSize(300, 300);
        advanceInterstitial.setDefaultSdkSupplier(new SdkSupplier("100171", "10000398",
                "e1d0d3aaf95d3f1980367e75bc41141d", AdvanceConfig.SDK_TAG_MERCURY));
        advanceInterstitial.setAdListener(this);
        advanceInterstitial.setGdtMediaListener(this);//非必须，设置广点通的视频广告的播放回调，不需要可忽略。
    }

    public void showAd(View view) {
        advanceInterstitial.show();
    }

    @Override
    public void onAdClose() {
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onAdReady() {
        Toast.makeText(this, "广告就绪", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdShow() {

        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailed() {

        Toast.makeText(this, "广告失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {

        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceInterstitial.destroy();
    }

    public void loadAd(View view) {
        advanceInterstitial.loadAd();

    }

    // 以下是广点通视频广告的相关回调
    @Override
    public void onVideoInit() {

    }

    @Override
    public void onVideoLoading() {

    }

    @Override
    public void onVideoReady(long l) {

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onVideoPause() {

    }

    @Override
    public void onVideoComplete() {

    }

    @Override
    public void onVideoError(AdError adError) {

    }

    @Override
    public void onVideoPageOpen() {

    }

    @Override
    public void onVideoPageClose() {

    }
}
