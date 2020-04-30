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
import com.advance.model.AdvanceSupplierID;
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
        myBannerAd = new MyBannerAd(this, adContainer,  ADManager.getInstance().getBannerAdspotId());
        //推荐：核心事件监听回调
        myBannerAd.setAdListener(this);
        //推荐：设置是否将获取到的SDK选择策略进行缓存
        myBannerAd.enableStrategyCache(false);
        //必须：设置打底SDK参数
        myBannerAd.setDefaultSdkSupplier(new SdkSupplier( "10000396" , AdvanceSupplierID.MERCURY));
        //注意：如果是使用自定义渠道的广告做打底，需要额外设置媒体id参数！！
//        myBannerAd.setDefaultSdkSupplier(new SdkSupplier( "自定义sdk渠道媒体id","自定义sdk渠道广告位id" , "自定义sdk渠道id"));
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
