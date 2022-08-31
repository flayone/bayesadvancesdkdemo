package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.advance.advancesdkdemo.AdvanceAD;
import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.SplashActivity;
import com.advance.advancesdkdemo.SplashToMainActivity;
import com.bayescom.admore.core.AMError;
import com.bayescom.admore.splash.AdMoreSplash;
import com.bayescom.admore.splash.AdMoreSplashListener;

public class AdMoreSplashActivity extends Activity {
    FrameLayout adContainer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);

        //初始化广告处理类
        AdMoreSplash splash = new AdMoreSplash(this, Constants.TestIds.adMoreSplashAdspotId, adContainer, new AdMoreSplashListener() {
            @Override
            public void onAdSkip() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onAdSkip");
            }

            @Override
            public void onAdTimeOver() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "onAdTimeOver");

            }

            @Override
            public void jumpToMain() {
                AdvanceAD.logAndToast(AdMoreSplashActivity.this, "jumpToMain");

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
        //注意！！：如果开屏页是fragment或者dialog盖在主页上的实现，这里需要置为true。不设置时默认值为false，代表开屏和首页为两个不同的activity
//        splash.getAdvanceSplash().setShowInSingleActivity(true);
        //请求并展示开屏广告。
        splash.loadAndShow();
    }



    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent( this, SplashToMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        this.finish();
    }

}
