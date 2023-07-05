package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.advance.AdvanceAD;
import com.bayescom.admore.core.AMError;
import com.bayescom.admore.splash.AdMoreSplash;
import com.bayescom.admore.splash.AdMoreSplashListener;
import com.mercury.sdk.core.config.MercuryAD;

public class AdMoreSplashActivity extends Activity {
    FrameLayout adContainer;
    AdMoreSplash splash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);

        //初始化广告处理类
        splash = new AdMoreSplash(this, Constants.TestIds.adMoreSplashAdspotId, adContainer, new AdMoreSplashListener() {
//            @Override
//            public void onAdSkip() {
//                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onAdSkip");
//            }
//
//            @Override
//            public void onAdTimeOver() {
//            }

            @Override
            public void jumpToMain() {
//                1; //广告执行失败，对应onAdFailed回调
//                2; //用户点击了广告跳过，对应旧onAdSkip回调
//                3; //广告倒计时结束，对应旧onAdTimeOver回调
                int jumpType = 0;
                if (splash != null) {
                    jumpType = splash.getJumpType();
                }
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "jumpToMain,jumpType = " + jumpType);

                goToMainActivity();
            }

            @Override
            public void onSuccess() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onSuccess");
            }

            @Override
            public void onShow() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onShow");

            }

            @Override
            public void onClick() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onClick");

            }

            @Override
            public void onFailed(AMError amError) {
                String eMsg = "";
                if (amError != null) {
                    eMsg = amError.toString();
                }
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onFailed,eMsg = " + eMsg);

            }
        });
//      建议：设置底部logo布局及高度值（单位px），如不设置广告将会填满展示
        splash.getAdvanceSplash().setLogoLayout(R.layout.splash_logo_layout, getResources().getDimensionPixelSize(R.dimen.logo_layout_height));
        //重要：注意！！：如果开屏页是fragment或者dialog盖在主页上的实现，这里需要置为true。不设置时默认值为false，代表开屏和首页为两个不同的activity
//        splash.getAdvanceSplash().setShowInSingleActivity(true);
        //请求并展示开屏广告。
        splash.loadAndShow();
    }


    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(this, SplashToMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        this.finish();
    }

}
