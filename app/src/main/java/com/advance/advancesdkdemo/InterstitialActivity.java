package com.advance.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceInterstitial;
import com.advance.AdvanceInterstitialListener;
import com.advance.model.AdvanceError;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;

public class InterstitialActivity extends AppCompatActivity implements AdvanceInterstitialListener {
    private AdvanceInterstitial advanceInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        //这里是获取测试广告位id，实际请替换成自己应用的正式广告位id！
        String adspotId = ADManager.getInstance().getInterstitialAdspotId();
        //初始化
        advanceInterstitial = new AdvanceInterstitial(this, adspotId);
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
    public void onAdFailed(AdvanceError advanceError) {

        Toast.makeText(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSdkSelected(String id) {

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


}
