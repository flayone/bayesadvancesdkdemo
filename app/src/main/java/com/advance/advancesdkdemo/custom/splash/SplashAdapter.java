package com.advance.advancesdkdemo.custom.splash;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.advancesdkdemo.R;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.mercury.sdk.core.config.LargeADCutType;
import com.mercury.sdk.core.config.MercuryAD;
import com.mercury.sdk.core.splash.SplashAD;
import com.mercury.sdk.core.splash.SplashADListener;
import com.mercury.sdk.util.ADError;
import com.qq.e.comm.util.AdError;

import java.lang.ref.SoftReference;

public class SplashAdapter {

    public static void loadCsjAD(final SoftReference<Activity> activity, final AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, final ViewGroup adContainer, final MySplashListener mySplashListener) {
        try {
            //初始化advance默认的穿山甲配置，也可以自己选择初始化方式
            AdvanceUtil.initCsj(activity.get(), sdkSupplier.mediaid);

            final TTAdManager ttAdManager = TTAdSdk.getAdManager();
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity.get());
            }
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(sdkSupplier.adspotid) //建议使用下发的广告位id，方便在后台替换
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)//尺寸大小可以自己去定义
                    .build();

            TTAdNative ttAdNative = ttAdManager.createAdNative(activity.get());
            int timeout = sdkSupplier.timeout == 0 ? 5000 : sdkSupplier.timeout;
            ttAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                @MainThread
                public void onError(int code, String message) {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null) {
                        customizeAd.adapterDidFailed();
                    }
                }

                @Override
                @MainThread
                public void onTimeout() {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null) {
                        customizeAd.adapterDidFailed();
                    }
                }

                @Override
                @MainThread
                public void onSplashAdLoad(TTSplashAd ad) {
                    if (ad == null) {
                        //这里一定要调用customizeAd 的事件方法
                        if (customizeAd != null) {
                            customizeAd.adapterDidFailed();
                        }
                        return;
                    }
//
                    //获取SplashView
                    View view = ad.getSplashView();
                    //渲染之前判断activity生命周期状态
                    if (!AdvanceUtil.isActivityDestroyed(activity)) {
                        adContainer.removeAllViews();
                        //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                        adContainer.addView(view);
                    }

                    //设置SplashView的交互监听器
                    ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int type) {
                            //这里一定要调用customizeAd 的事件方法
                            if (customizeAd != null) {
                                customizeAd.adapterDidClicked();
                            }
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            //这里一定要调用customizeAd 的事件方法
                            if (customizeAd != null) {
                                customizeAd.adapterDidShow();
                            }
                        }

                        @Override
                        public void onAdSkip() {
                            if (mySplashListener != null) {
                                mySplashListener.onClose();
                            }
                        }

                        @Override
                        public void onAdTimeOver() {
                            if (mySplashListener != null) {
                                mySplashListener.onClose();
                            }
                        }
                    });
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null) {
                        customizeAd.adapterDidSucceed();
                    }
                }
            }, timeout);

        } catch (Throwable e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (customizeAd != null)
                customizeAd.adapterDidFailed();
        }
    }

    public static void loadGdtAD(final SoftReference<Activity> activity, final AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, ViewGroup adContainer, final TextView skipView, final MySplashListener mySplashListener) {

        try {
            final String skipText = "跳过 %d";
            int timeout = sdkSupplier.timeout == 0 ? 5000 : sdkSupplier.timeout;
            com.qq.e.ads.splash.SplashADListener listener = new com.qq.e.ads.splash.SplashADListener() {
                @Override
                public void onADDismissed() {
                    if (null != mySplashListener) {
                        mySplashListener.onClose();
                    }
                }

                @Override
                public void onNoAD(AdError adError) {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null) {
                        customizeAd.adapterDidFailed();
                    }
                }

                @Override
                public void onADPresent() {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null) {
                        customizeAd.adapterDidShow();
                    }
                    if (skipView != null) {
                        skipView.setVisibility(View.VISIBLE);
                        skipView.setBackgroundDrawable(ContextCompat.getDrawable(activity.get(), R.drawable.background_circle));
                    }
                }

                @Override
                public void onADClicked() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked();
                    }
                }

                @Override
                public void onADTick(long l) {
                    if (null != skipView) {
                        skipView.setText(String.format(skipText, Math.round(l / 1000f)));
                    }
                }

                @Override
                public void onADExposure() {
                }

                @Override
                public void onADLoaded(long expireTimestamp) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                }
            };
            com.qq.e.ads.splash.SplashAD splashAD = new com.qq.e.ads.splash.SplashAD(activity.get(), skipView, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, listener, timeout);
            splashAD.fetchAndShowIn(adContainer);
        } catch (Throwable e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd)
                customizeAd.adapterDidFailed();
        }
    }


    public static void loadMercuryAD(SoftReference<Activity> activity, final AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, ViewGroup adContainer, final TextView skipView, final MySplashListener mySplashListener) {

        try {
            final String skipText = "跳过 %d";
            //初始化Mercury SDK广告位参数设置
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);

            //自由选择：设置mercury素材展示模式，LargeADCutType.CUT_BOTTOM 代表对过长广告素材，保持宽度不变底部进行剪切。默认为LargeADCutType.DEFAULT 不对素材做剪切处理
            MercuryAD.setLargeADCutType(LargeADCutType.CUT_BOTTOM);
            //自由选择：是否强制展示logo，默认false即大图小手机下会不展示logo
            MercuryAD.setSplashForceShowLogo(true);
            //可选，自定义强制显示的开屏logo高度，单位dp，默认-1不限制高度，跟随素材高度
            MercuryAD.setSplashForceLogoHeight(100);
            //可选，设置开屏页面的底色,默认无色透明
            MercuryAD.setSplashBackgroundColor(ContextCompat.getColor(activity.get(), R.color.adv_white));

            int timeout = sdkSupplier.timeout == 0 ? 5000 : sdkSupplier.timeout;
            SplashAD mercurySplash = new SplashAD(activity.get(), sdkSupplier.adspotid, skipView, timeout, new SplashADListener() {
                @Override
                public void onADDismissed() {
                    if (null != mySplashListener) {
                        mySplashListener.onClose();
                    }

                }

                @Override
                public void onADPresent() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                }

                @Override
                public void onADTick(long l) {
                    if (null != skipView) {
                        skipView.setText(String.format(skipText, Math.round(l / 1000f)));
                    }

                }

                @Override
                public void onADExposure() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow();
                    }

                }

                @Override
                public void onADClicked() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked();
                    }

                }

                @Override
                public void onNoAD(ADError adError) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed();
                    }
                }
            });
            //可选设置，设置底部logo图片，建议使用宽高比6：1左右的细长图片，展示效果最好
            mercurySplash.setLogoImage(R.mipmap.logo);
            //可选设置，设置开屏素材展示之前的占位图
            mercurySplash.setSplashHolderImage(R.mipmap.background);
            //展示广告
            mercurySplash.fetchAndShowIn(adContainer);
        } catch (Throwable e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (customizeAd != null)
                customizeAd.adapterDidFailed();
        }
    }
}
