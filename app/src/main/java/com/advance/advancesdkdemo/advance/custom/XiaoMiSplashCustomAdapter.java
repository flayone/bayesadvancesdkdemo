package com.advance.advancesdkdemo.advance.custom;

import android.app.Activity;
import android.os.Handler;

import com.advance.SplashSetting;
import com.advance.custom.AdvanceSplashCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.LogUtil;
import com.miui.zeus.mimo.sdk.MimoSdk;
import com.miui.zeus.mimo.sdk.SplashAd;

/**
 * 自定义小米开屏渠道
 */
public class XiaoMiSplashCustomAdapter extends AdvanceSplashCustomAdapter {
    SplashAd mSplashAd;
    private boolean isCountingEnd = false;//用来辅助判断用户行为，用户是点击了跳过还是倒计时结束，false 回调dismiss的话代表是跳过，否则倒计时结束
    String TAG = "[XiaoMiSplashAdapter] ";

    public XiaoMiSplashCustomAdapter(Activity activity, SplashSetting splashSetting) {
        super(activity, splashSetting);
        supportPara = false;  //代表是否支持并行加载广告，因为小米不支持广告单独load，所以无法进行并行加载广告
    }

    @Override
    public void paraLoadAd() {//并行加载广告
        LogUtil.simple(TAG + "paraLoadAd");

        //假如支持并行加载，需要在此进行仅加载广告请求方法，在adReady()回调中处理广告展示逻辑。
    }

    @Override
    public void orderLoadAd() {//串行加载广告
        LogUtil.simple(TAG + "orderLoadAd");

        //不支持并行加载的，可以在此方法进行广告请求并展示
        loadAndShow();
    }


    @Override
    public void adReady() {//广告就绪（不区分串、并行），可以进行addView等吊起广告展示的操作
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
        //注意要初始化SDK，此步骤也可以放在application中进行
        MimoSdk.init(getADActivity());
        //初始化小米开屏广告实例
        mSplashAd = new SplashAd();
        //执行广告展示方法，并在对应回调中执行对应生命周期回调
        mSplashAd.loadAndShow(adContainer, getPosID(), new SplashAd.SplashAdListener() {
            @Override
            public void onAdShow() {
                //必须：广告展示回调
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
                //必须：广告点击回调
                handleClick();
            }

            @Override
            public void onAdDismissed() {
                //必须：广告关闭事件回调，回调区分用户点击了跳过还是计时结束
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
                //必须：广告失败回调
                handleFailed(i + "", s);
            }

            @Override
            public void onAdLoaded() {
                //必须：广告成功回调
                handleSucceed();
            }

            @Override
            public void onAdRenderFailed() {
                //必须：渲染失败也要执行失败回调处理
                handleFailed(AdvanceError.ERROR_RENDER_FAILED, "");
            }
        });
    }


    @Override
    public void show() {

    }
}
