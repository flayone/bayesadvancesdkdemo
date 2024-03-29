package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.R;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

public class NativeCustomizeActivity extends Activity {

    AdvanceCustomizeAd nativeCustomizeAd;
    FrameLayout fl;
    String Tag = NativeCustomizeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_container);
        fl = findViewById(R.id.fl_ad);

        //创建自己的自渲染广告位，第二个参数为后台申请的广告位id
        nativeCustomizeAd = new AdvanceCustomizeAd(this, "");
        //必须：设置广告载体
        nativeCustomizeAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调
                fl.removeAllViews();
            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        new MyCsjNCAdapter(NativeCustomizeActivity.this, nativeCustomizeAd, selectedSupplier, fl).loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        new MyGdtNCAdapter(NativeCustomizeActivity.this, nativeCustomizeAd, selectedSupplier, fl).loadAd();
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        new MyMercuryNCAdapter(NativeCustomizeActivity.this, nativeCustomizeAd, selectedSupplier, fl).loadAd();
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        nativeCustomizeAd.selectSdkSupplier();
                }
            }
        });
        //可选：设置是否采用策略缓存
        nativeCustomizeAd.enableStrategyCache(false);
//        必须：设置打底广告，比如app第一次打开时，会先走这里设置的打底广告
        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("4090398440079274", AdvanceSupplierID.GDT));
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("10002805", AdvanceSupplierID.MERCURY));
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier( "10002806",  AdvanceSupplierID.MERCURY));
//        AdvanceConfig.getInstance().setCsjAppId("");
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("5001121", "901121737", AdvanceConfig.SDK_ID_CSJ));

//        发起策略请求
        nativeCustomizeAd.loadStrategy();
    }


}
