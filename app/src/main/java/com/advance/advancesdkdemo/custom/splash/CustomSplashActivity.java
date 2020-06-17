package com.advance.advancesdkdemo.custom.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.AdvanceCustomizeSupplierListener;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.SplashToMainActivity;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

import java.lang.ref.SoftReference;

public class CustomSplashActivity extends Activity {
    AdvanceCustomizeAd customizeAd;
    FrameLayout adContainer;
    TextView skipView;
    LinearLayout logo;
    boolean canJump = false;
    boolean isCsj = false;
    MySplashListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        logo = findViewById(R.id.ll_asc_logo);

        loadSplash();
    }

    private void loadSplash() {
        //自定义开屏统一事件回调
        listener = new MySplashListener() {
            @Override
            public void onClose() {
                jump();
            }
        };
        //开屏建议使用软引用activity，第二个参数为后台申请的广告位id
        customizeAd = new AdvanceCustomizeAd(new SoftReference<Activity>(this), ADManager.getInstance().getSplashAdspotId());
        //设置渠道的结果监听
        customizeAd.setSupplierListener(new AdvanceCustomizeSupplierListener() {
            @Override
            public void onSupplierFailed() {
                //一般是策略无填充，或者所有策略均加载失败时回调
                jump();
            }

            @Override
            public void onSupplierSelected(SdkSupplier selectedSupplier) {
                isCsj = AdvanceConfig.SDK_ID_CSJ.equals(selectedSupplier.id);
                //策略选择回调，可根据不同的渠道ID来加载各渠道广告
                switch (selectedSupplier.id) {
                    case AdvanceConfig.SDK_ID_CSJ:
                        logo.setVisibility(View.VISIBLE);
                        SplashAdapter.loadCsjAD(new SoftReference<Activity>(CustomSplashActivity.this), customizeAd, selectedSupplier, adContainer, listener);
                        break;
                    case AdvanceConfig.SDK_ID_GDT:
                        logo.setVisibility(View.VISIBLE);
                        SplashAdapter.loadGdtAD(new SoftReference<Activity>(CustomSplashActivity.this), customizeAd, selectedSupplier, adContainer, skipView, listener);
                        break;
                    case AdvanceConfig.SDK_ID_MERCURY:
                        logo.setVisibility(View.GONE);
                        SplashAdapter.loadMercuryAD(new SoftReference<Activity>(CustomSplashActivity.this), customizeAd, selectedSupplier, adContainer, skipView, listener);
                        break;
                    default:
                        //不需要支持的渠道，建议选择重新调度策略
                        customizeAd.selectSdkSupplier();
                }
            }
        });

        //设置是否使用策略缓存
        customizeAd.enableStrategyCache(true);
        //设置打底SDK参数
        customizeAd.setDefaultSdkSupplier(new SdkSupplier("887301946", AdvanceSupplierID.CSJ));
        //请求策略
        customizeAd.loadStrategy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //穿山甲需要强制跳转
        canJump = isCsj;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //再次回到页面时需要跳转主页
        jump();
    }

    //开屏广告页面展示完毕或者出错，需要跳转主页等操作
    private void jump() {
        if (canJump) {
            startActivity(new Intent(this, SplashToMainActivity.class));
            finish();
        } else {
            canJump = true;
        }
    }
}
