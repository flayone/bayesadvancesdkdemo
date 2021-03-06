package com.advance.advancesdkdemo.custom.full;

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

public class CustomFullScreenActivity extends Activity {
    AdvanceCustomizeAd ad;
    BaseCustomAdapter adapter;
    boolean isGdt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        ad = new AdvanceCustomizeAd(this, ADManager.getInstance().getFullScreenVideoAdspotId());
        //设置渠道的结果监听
        ad.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调

            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                isGdt = AdvanceConfig.SDK_ID_GDT.equals(selectedSupplier.id);

                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        adapter = new MyCsjFSAdapter();
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        adapter = new MyGdtFSAdapter();
                        break;
//                    MERCURY渠道不支持该类型广告
//                    case AdvanceConfig.SDK_ID_MERCURY:
//                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        ad.selectSdkSupplier();
                        break;
                }
                //初始化必要信息,并加载广告
                if (adapter != null) {
                    adapter.init(CustomFullScreenActivity.this, ad, selectedSupplier);
                    adapter.loadAD();
                }
            }
        });
        //可选：设置是否使用策略缓存
        ad.enableStrategyCache(false);
        //必须：设置打底广告，SdkSupplier（"对应渠道平台申请的广告位id", 渠道平台id标识）
        ad.setDefaultSdkSupplier(new SdkSupplier("945065337", AdvanceSupplierID.CSJ));

    }

    public void loadFull(View view) {
        //请求策略并加载广告
        ad.loadStrategy();
    }

    public void showFull(View view) {
        if (adapter != null) {
            if (!adapter.isVideoCached &&  !isGdt) {
                Toast.makeText(this, "视频还未准备好", Toast.LENGTH_SHORT).show();
                return;
            }
            adapter.showAD();
        } else {
            Toast.makeText(this, "广告未加载", Toast.LENGTH_SHORT).show();
        }
    }

}
