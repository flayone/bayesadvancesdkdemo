package com.advance.advancesdkdemo.custom.reward;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

public class CustomRewardActivity extends Activity {
    AdvanceCustomizeAd ad;
    BaseCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);

        ad = new AdvanceCustomizeAd(this, ADManager.getInstance().getRewardAdspotId());
        //设置渠道的结果监听
        ad.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调

            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        adapter = new MyCsjRewardAdapter();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        adapter = new MyGdtRewardAdapter();
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        adapter = new MyMercuryRewardAdapter();
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        ad.selectSdkSupplier();
                        break;
                }
                //初始化必要信息,并加载广告
                if (adapter != null) {
                    adapter.init(CustomRewardActivity.this, ad, selectedSupplier);
                    adapter.setCustomRewardListener(new CustomRewardListener() {
                        @Override
                        public void onReward() {
                            Toast.makeText(CustomRewardActivity.this, "激励奖励发放", Toast.LENGTH_SHORT).show();
                        }
                    });
                    adapter.loadAD();
                }
            }
        });
        //可选：设置是否使用策略缓存
        ad.enableStrategyCache(false);
        //必须：设置打底广告，SdkSupplier（"对应渠道平台申请的广告位id", 渠道平台id标识）
        ad.setDefaultSdkSupplier(new SdkSupplier("2090845242931421", AdvanceSupplierID.GDT));

    }

    public void onLoad(View view) {
        //请求策略并加载广告
        ad.loadStrategy();
    }

    public void onShow(View view) {
        if (adapter != null) {
            if (adapter.isVideoCached) {
                adapter.showAD();
            } else {
                Toast.makeText(this, "视频还未准备好", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "广告未加载", Toast.LENGTH_SHORT).show();
        }
    }

}
