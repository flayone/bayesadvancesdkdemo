package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

public class MyCsjBannerAdapter {
    private Activity activity;
    private SdkSupplier sdkSupplier;
    private AdvanceCustomizeAd advanceBanner;
    private ViewGroup adContainer;
    private long startTime = 0;
    private int csjAcceptedSizeWidth = 640;
    private int csjAcceptedSizeHeight = 100;
    private int refreshInterval = 30;

    private int csjExpressViewAcceptedWidth = 640;
    private int csjExpressViewAcceptedHeight = 100;

    public MyCsjBannerAdapter(Activity activity, ViewGroup adContainer, final AdvanceCustomizeAd advanceBanner, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.advanceBanner = advanceBanner;
        this.sdkSupplier = sdkSupplier;
        this.adContainer = adContainer;
    }

    public void loadAd() {
        try {
            AdvanceUtil.initCsj(activity, sdkSupplier.mediaid);

            final TTAdManager ttAdManager = TTAdSdk.getAdManager();
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity);
            }
            TTAdNative ttAdNative = ttAdManager.createAdNative(activity);
            if (adContainer != null) {
                adContainer.removeAllViews();
            }
            AdSlot adSlot = new AdSlot.Builder()
                    // 必选参数 设置您的CodeId
                    .setCodeId(sdkSupplier.adspotid)
                    //期望模板广告view的size,单位dp
                    .setExpressViewAcceptedSize(csjExpressViewAcceptedWidth, csjExpressViewAcceptedHeight)
                    // 必选参数 设置广告图片的最大尺寸及期望的图片宽高比，单位Px
                    .setImageAcceptedSize(csjAcceptedSizeWidth, csjAcceptedSizeHeight)
                    // 可选参数 设置是否支持deeplink
                    .setSupportDeepLink(true)
                    //请求原生广告时候需要设置，参数为TYPE_BANNER或TYPE_INTERACTION_AD
                    .build();
            ttAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {

                @Override
                public void onError(int code, String message) {
                    LogUtil.AdvanceLog(code + message);
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidFailed(AdvanceError.parseErr(code,message));
                    }
                    if (adContainer != null) {
                        adContainer.removeAllViews();
                    }
                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                    if (ads == null || ads.size() == 0) {
                        if (null != advanceBanner) {
                            advanceBanner.adapterDidFailed(AdvanceError.parseErr(AdvanceError.ERROR_DATA_NULL));
                        }
                        return;
                    }
                    TTNativeExpressAd ad = ads.get(0);
                    // 加载成功的回调，接入方可在此处做广告的展示，请确保您的代码足够健壮，能够处理异常情况；
                    if (null == ad) {
                        if (null != advanceBanner) {
                            advanceBanner.adapterDidFailed(AdvanceError.parseErr(AdvanceError.ERROR_DATA_NULL));
                        }
                        return;
                    }

                    bindAdListener(ad);
                    startTime = System.currentTimeMillis();
                    ad.render();
                }

            });
        } catch (Throwable e) {
            e.printStackTrace();
            if (null != advanceBanner) {
                advanceBanner.adapterDidFailed(AdvanceError.parseErr(AdvanceError.ERROR_EXCEPTION_LOAD));
            }
        }
    }

    private void bindAdListener(TTNativeExpressAd ad) {

        try {
            if (null != advanceBanner) {
                ad.setSlideIntervalTime(refreshInterval * 1000);
            }
            ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int i) {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidClicked();
                    }
                }

                @Override
                public void onAdShow(View view, int i) {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidShow();
                    }
                }

                @Override
                public void onRenderFail(View view, String s, int i) {
                    LogUtil.AdvanceLog("ExpressView render fail:" + (System.currentTimeMillis() - startTime));

                    if (null != advanceBanner) {
                        advanceBanner.adapterDidFailed(AdvanceError.parseErr(i,s));
                    }
                }

                @Override
                public void onRenderSuccess(View view, float v, float v1) {
                    LogUtil.AdvanceLog("ExpressView render suc:" + (System.currentTimeMillis() - startTime));
                    if (adContainer != null) {
                        adContainer.removeAllViews();
                        adContainer.addView(view);
                    }
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidSucceed();
                    }
                }
            });


            //使用默认模板中默认dislike弹出样式
            ad.setDislikeCallback(activity, new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onShow() {

                }

                @Override
                public void onSelected(int position, String value) {
                    //用户选择不喜欢原因后，移除广告展示
                    if (adContainer != null) {
                        adContainer.removeAllViews();
                    }

                }

                @Override
                public void onCancel() {
                }
                @Override
                public void onRefuse() {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
