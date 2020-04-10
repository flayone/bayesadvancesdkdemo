package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceBannerListener;
import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.model.SdkSupplier;

public class BannerCustomizeActivity extends Activity implements AdvanceBannerListener {
    private String TAG = "BannerCustomizeActivity";

    MyBannerAd myBannerAd;
    FrameLayout adContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_cus);
        adContainer = findViewById(R.id.fl_ad);
        myBannerAd = new MyBannerAd(this, adContainer, ADManager.getInstance().getMediaId(), ADManager.getInstance().getBannerAdspotId());
        myBannerAd.setAdListener(this);
        //设置是否将获取到的SDK选择策略进行缓存
        myBannerAd.setUseCache(false);
        //设置打底SDK参数
        myBannerAd.setDefaultSdkSupplier(new SdkSupplier("100171", "10000396", "e1d0d3aaf95d3f1980367e75bc41141d", AdvanceConfig.SDK_TAG_MERCURY));
        myBannerAd.loadAd();
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
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
        adContainer.removeAllViews();

    }

}
