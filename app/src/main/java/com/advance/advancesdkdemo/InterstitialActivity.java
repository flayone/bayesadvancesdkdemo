package com.advance.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceInterstitial;
import com.advance.AdvanceInterstitialListener;
import com.advance.model.SdkSupplier;

public class InterstitialActivity extends AppCompatActivity implements AdvanceInterstitialListener {
    private AdvanceInterstitial advanceInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);
        advanceInterstitial = new AdvanceInterstitial(this, "10018", "200041");
        advanceInterstitial.setDefaultSdkSupplier(new SdkSupplier("100171", "10000398",
                "e1d0d3aaf95d3f1980367e75bc41141d", AdvanceConfig.SDK_TAG_MERCURY));
        advanceInterstitial.setAdListener(this);
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
}
