package com.advance.supplier;

import android.app.Activity;
import android.os.Handler;

import com.advance.SplashSetting;
import com.advance.custom.AdvanceSplashCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.LogUtil;
import com.miui.zeus.mimo.sdk.SplashAd;

import java.lang.ref.SoftReference;

/**
 * 自定义小米开屏渠道
 */
public class XiaoMiSplashAdapter extends AdvanceSplashCustomAdapter {
    SplashAd mSplashAd;
    private boolean isCountingEnd = false;//用来判断是否倒计时走到了最后，false 回调dismiss的话代表是跳过，否则倒计时结束
    String TAG = "[XiaoMiSplashAdapter] ";

    public XiaoMiSplashAdapter(SoftReference<Activity> activity, SplashSetting splashSetting) {
        super(activity.get(), splashSetting);
        supportPara = false;  //代表是否支持并行加载广告，因为小米不支持广告单独load，所以无法进行并行加载广告
    }

    @Override
    public void paraLoadAd() {//并行加载广告
        LogUtil.simple(TAG + "paraLoadAd");
    }

    @Override
    public void orderLoadAd() {//串行加载广告
        LogUtil.simple(TAG + "orderLoadAd");

        loadAndShow();
    }


    @Override
    public void adReady() {//广告就绪（不区分串、并行）
        LogUtil.simple(TAG + "adReady");
    }

    @Override
    public void doDestroy() { //销毁广告
        if (mSplashAd != null) {
            mSplashAd.destroy();
        }
    }


    /**
     * 初始化小米SDK，并请求和展示广告
     */
    private void loadAndShow() {
//        MimoSdk.init(getADActivity());
        mSplashAd = new SplashAd();
        //执行广告展示方法，并在对应回调中执行对应生命周期回调
        mSplashAd.loadAndShow(adContainer, getPosID(), new SplashAd.SplashAdListener() {
            @Override
            public void onAdShow() {
                handleShow();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isCountingEnd = true;
                    }
                }, 4800);
            }

            @Override
            public void onAdClick() {
                handleClick();
            }

            @Override
            public void onAdDismissed() {
                //必要回调：广告关闭事件回调，回调区分用户点击了跳过还是计时结束
                if (splashSetting != null) {
                    if (isCountingEnd) {
                        splashSetting.adapterDidTimeOver();
                    } else {
                        splashSetting.adapterDidSkip();
                    }
                }
            }

            @Override
            public void onAdLoadFailed(int i, String s) {
                handleFailed(i + "", s);
            }

            @Override
            public void onAdLoaded() {
                handleSucceed();
            }

            @Override
            public void onAdRenderFailed() {
                //渲染失败也要执行失败回调处理
                handleFailed(AdvanceError.ERROR_RENDER_FAILED, "");
            }
        });
    }

    @Override
    public void show() {

    }
}
