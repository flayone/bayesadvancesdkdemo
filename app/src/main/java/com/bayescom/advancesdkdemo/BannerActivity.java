package com.bayescom.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bayesadvance.AdvanceBanner;
import com.bayesadvance.AdvanceBannerListener;
import com.bayesadvance.AdvanceConfig;
import com.bayesadvance.model.SdkSupplier;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private String TAG="DEMO BANNER";
    private AdvanceBanner advanceBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        RelativeLayout rl = findViewById(R.id.banner_layout);
        advanceBanner = new AdvanceBanner(this,rl,Constants.mediaId,Constants.bannerAdspotId);
        advanceBanner.setCsjAcceptedSize(640,100)
                     .setRefreshInterval(60)
                     .setAdListener(this);
        advanceBanner.setDefaultSdkSupplier(new SdkSupplier("100171","10000396","e1d0d3aaf95d3f1980367e75bc41141d",AdvanceConfig.SDK_TAG_BAYES));
        advanceBanner.loadAd();


    }

    @Override
    public void onAdShow() {
        Log.d(TAG,"SHOW");
        Toast.makeText(this,"广告展现",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {

        Log.d(TAG,"Failed");
        Toast.makeText(this,"广告失败",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d(TAG,"Clicked");
        Toast.makeText(this,"广告点击",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDislike() {
        RelativeLayout rl = findViewById(R.id.banner_layout);
        rl.removeAllViews();
        Toast.makeText(this,"广告关闭",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        advanceBanner.destroy();

    }
}
