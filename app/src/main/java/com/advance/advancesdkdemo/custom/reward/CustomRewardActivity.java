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
import com.advance.model.SdkSupplier;

public class CustomRewardActivity extends Activity {
    AdvanceCustomizeAd ad;
    BaseCustomAdapter adapter;

    private boolean isReady = false;
    private boolean isPause = false;

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
                        public void onLoaded() {
                            isReady = true;
                            //页面非暂停状态才展示广告
                            if (adapter != null&& !isPause)
                                adapter.showAD();
                        }

                        @Override
                        public void onReward() {
                            Toast.makeText(CustomRewardActivity.this, "激励奖励发放", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClose() {
                            Toast.makeText(CustomRewardActivity.this, "广告关闭", Toast.LENGTH_SHORT).show();
                            isReady = false;
                        }
                    });
                    adapter.loadAD();
                }
            }
        });

    }

    public void onLoad(View view) {
        //请求策略并加载广告
        ad.loadStrategy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        if (isReady && adapter != null) {
            //广告已经调用过，且页面从后台恢复前台后重新展示广告
            adapter.showAD();
        }
    }

}
