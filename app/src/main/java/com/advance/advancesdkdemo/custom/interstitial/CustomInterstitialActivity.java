package com.advance.advancesdkdemo.custom.interstitial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

public class CustomInterstitialActivity extends Activity {
    AdvanceCustomizeAd myInterstitialAd;
    BaseCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        myInterstitialAd = new AdvanceCustomizeAd(this, ADManager.getInstance().getInterstitialAdspotId());
        //设置渠道的结果监听
        myInterstitialAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调

            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        adapter = new MyCsjInterstitialAdapter();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        adapter = new MyGdtInterstitialAdapter();
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        adapter = new MyMercuryInterstitialAdapter();
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        myInterstitialAd.selectSdkSupplier();
                        break;
                }
                //初始化必要信息,并加载广告
                if (adapter != null) {
                    adapter.init(CustomInterstitialActivity.this, myInterstitialAd, selectedSupplier);
                    adapter.loadAD();
                }
            }
        });

    }

    public void loadAd(View view) {
        //请求策略
        myInterstitialAd.loadStrategy();
    }

    public void showAd(View view) {
        if (adapter != null) {
            adapter.showAD();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.destroy();
        }
    }
}
