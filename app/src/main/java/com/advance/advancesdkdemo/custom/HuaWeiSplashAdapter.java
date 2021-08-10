package com.advance.advancesdkdemo.custom;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Handler;

import com.advance.SplashSetting;
import com.advance.custom.AdvanceSplashCustomAdapter;
import com.advance.utils.LogUtil;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;

public class HuaWeiSplashAdapter extends AdvanceSplashCustomAdapter {
    SplashView splashView;
    private boolean isCountingEnd = false;//用来辅助判断用户行为，用户是点击了跳过还是倒计时结束，false 回调dismiss的话代表是跳过，否则倒计时结束
    String TAG = "[HuaWeiSplashAdapter] ";

    public HuaWeiSplashAdapter(Activity activity, SplashSetting splashSetting) {
        super(activity, splashSetting);
        supportPara = false;
    }

    @Override
    protected void paraLoadAd() {
        LogUtil.simple(TAG + "paraLoadAd");
        loadAD();
    }

    @Override
    public void orderLoadAd() {
        LogUtil.simple(TAG + "orderLoadAd");
        loadAD();
    }


    @Override
    protected void adReady() {
        LogUtil.simple(TAG + "adReady");


    }

    @Override
    public void doDestroy() {
        LogUtil.simple(TAG + "doDestroy");

    }

    private void loadAD() {
        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        AdParam adParam = new AdParam.Builder().build();
        splashView = new SplashView(getADActivity());
        if (splashView != null) {
            adContainer.addView(splashView);
        }
        SplashAdDisplayListener adDisplayListener = new SplashAdDisplayListener() {
            @Override
            public void onAdShowed() {
                //必须：广告显示时调用
                handleShow();

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCountingEnd = true;
                        }
                    }, 4800);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdClick() {
                //必须： 广告被点击时调用
                handleClick();
            }
        };
        splashView.setAdDisplayListener(adDisplayListener);
        splashView.load(getPosID(), orientation, adParam, new SplashView.SplashAdLoadListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                handleFailed(errorCode + "", "");

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                handleSucceed();


            }

            @Override
            public void onAdDismissed() {
                super.onAdDismissed();
                //必须：广告关闭事件回调，回调区分用户点击了跳过还是计时结束
                if (splashSetting != null) {
                    if (isCountingEnd) {
                        splashSetting.adapterDidTimeOver();
                    } else {
                        splashSetting.adapterDidSkip();
                    }
                }
            }
        });
    }


}
