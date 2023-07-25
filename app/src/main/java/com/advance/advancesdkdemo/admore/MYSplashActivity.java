package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.advance.LogUtil;
import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash;

public class MYSplashActivity extends Activity {
    private static final String TAG = "MYSplashActivity";
    FrameLayout adContainer;
    FrameLayout adRealContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);
        adRealContainer = adContainer;
        adContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadGM();
            }
        }, 200);

    }

    private void loadGM() {
        RelativeLayout wrapC = new RelativeLayout(this);
        adRealContainer = new FrameLayout(this);
        int adHeight = adContainer.getHeight();
//        if (oriH > 0) {
            adHeight = adHeight - 300;
//            if (adHeight > 0) {
//                //赋值新的高度像素值
////                csjAcceptedSizeHeight = adHeight;
////                LogUtil.devDebug(TAG + "use new height ,csjAcceptedSizeHeight:" + csjAcceptedSizeHeight);
//
//                //赋值新的承载布局
//                adRealContainer = new FrameLayout(this);
//            } else {
//                adHeight = ViewGroup.LayoutParams.MATCH_PARENT;
//            }
//        }
        RelativeLayout.LayoutParams adLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, adHeight);
        wrapC.addView(adRealContainer, adLp);

//                            添加广告布局至底部
        RelativeLayout.LayoutParams logoLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
        logoLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        View logoLayoutView =    LayoutInflater.from(this).inflate(R.layout.splash_logo_layout, null);
        wrapC.addView(logoLayoutView, logoLp);

        adContainer.addView(wrapC, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        loadAD();
    }

    private void loadAD() {
        String gmid = "102126980";
        GMSplashAd mGMSplashAd = new GMSplashAd(this, gmid);
        GMSplashAdLoadCallback gmSplashAdLoadCallback = new GMSplashAdLoadCallback() {
            @Override
            public void onSplashAdLoadFail(@NonNull AdError adError) {
                LogUtil.high(TAG + "(GMSplashAdLoadCallback) onSplashAdLoadFail ");
                goToMainActivity();
            }

            @Override
            public void onSplashAdLoadSuccess() {
                LogUtil.high(TAG + "(GMSplashAdLoadCallback) onSplashAdLoadSuccess  splash ad success ");
                mGMSplashAd.showAd(adRealContainer);
            }

            // 注意：***** 开屏广告加载超时回调已废弃，统一走onSplashAdLoadFail，GroMore作为聚合不存在SplashTimeout情况。*****
            @Override
            public void onAdLoadTimeout() {

            }
        };
        GMSplashAdListener mSplashAdListener = new GMSplashAdListener() {

            @Override
            public void onAdClicked() {
                LogUtil.high(TAG + "GMSplashAdListener onAdClicked ");
            }

            @Override
            public void onAdShow() {
                LogUtil.high(TAG + "GMSplashAdListener onAdShow ");

            }

            @Override
            public void onAdShowFail(@NonNull AdError adError) {
                LogUtil.high(TAG + "GMSplashAdListener onAdShowFail ");
                goToMainActivity();

            }

            @Override
            public void onAdSkip() {
                LogUtil.high(TAG + "GMSplashAdListener onAdSkip ");
                goToMainActivity();

            }

            @Override
            public void onAdDismiss() {
                LogUtil.high(TAG + "GMSplashAdListener onAdDismiss ");
                goToMainActivity();

            }
        };
        mGMSplashAd.setAdSplashListener(mSplashAdListener);
        /**
         * 创建开屏广告请求类型参数GMAdSlotSplash,具体参数含义参考文档
         */
        GMAdSlotSplash.Builder gmBuilder = new GMAdSlotSplash.Builder();
        //宽高默认设置全屏，根据广告承载布局实际大小进行重定义
        gmBuilder.setImageAdSize(adRealContainer.getWidth(), adRealContainer.getHeight()) // 单位px
                .setBidNotify(true)//开启bidding比价结果通知，默认值为false
                .setSplashPreLoad(true)
                .setSplashShakeButton(true); //开屏摇一摇开关，默认开启，目前只有gdt支持

        gmBuilder.setTimeOut(5000);//设置超时


        GMAdSlotSplash adSlot = gmBuilder.build();
        mGMSplashAd.loadAd(adSlot, null, gmSplashAdLoadCallback);
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
