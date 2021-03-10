package com.advance.advancesdkdemo.custom.interstitial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;

public class CustomInterstitialActivity extends Activity {
    AdvanceCustomizeAd myInterstitialAd;
    BaseCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        myInterstitialAd = new AdvanceCustomizeAd(this, Constants.Csj.interstitialAdspotId);
        //设置渠道的结果监听
        myInterstitialAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed(AdvanceError advanceError)  {
                //一般是策略无填充，或者所有策略均加载失败时回调
                Toast.makeText(CustomInterstitialActivity.this, "策略加载失败" + advanceError.code + "," + advanceError.msg, Toast.LENGTH_LONG).show();

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
