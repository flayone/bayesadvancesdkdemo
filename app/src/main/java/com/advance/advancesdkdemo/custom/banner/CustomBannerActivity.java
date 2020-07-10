package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

public class CustomBannerActivity extends Activity {
    private String TAG = "CustomBannerActivity";
    FrameLayout adContainer;
    AdvanceCustomizeAd myBannerAd;
    MyGdtBannerAdapter gdtBannerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_cus);
        adContainer = findViewById(R.id.fl_ad);

        loadBanner();
    }

    private void loadBanner() {

        myBannerAd = new AdvanceCustomizeAd(this, ADManager.getInstance().getBannerAdspotId());
        //设置渠道的结果监听
        myBannerAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调
            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        new MyCsjBannerAdapter(CustomBannerActivity.this, adContainer, myBannerAd, selectedSupplier).loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        gdtBannerAdapter = new MyGdtBannerAdapter(CustomBannerActivity.this, adContainer, myBannerAd, selectedSupplier);
                        gdtBannerAdapter.loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        new MyMercuryBannerAdapter(CustomBannerActivity.this, adContainer, myBannerAd, selectedSupplier).loadAd();
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        myBannerAd.selectSdkSupplier();
                        break;
                }
            }
        });
        //可选：设置是否使用策略缓存
        myBannerAd.enableStrategyCache(true);
        //必须：设置打底SDK参数
        myBannerAd.setDefaultSdkSupplier(new SdkSupplier("10000396", AdvanceSupplierID.MERCURY));
        //请求策略
        myBannerAd.loadStrategy();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //广点通需要离开时销毁广告
        if (gdtBannerAdapter != null)
            gdtBannerAdapter.destroy();
    }
}
