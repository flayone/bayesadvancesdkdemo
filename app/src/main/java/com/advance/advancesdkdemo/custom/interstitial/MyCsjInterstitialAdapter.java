package com.advance.advancesdkdemo.custom.interstitial;

import android.view.View;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.List;

import static com.advance.model.AdvanceError.ERROR_DATA_NULL;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyCsjInterstitialAdapter extends BaseCustomAdapter {
    private TTNativeExpressAd mTTAd;

    @Override
    public void showAD() {
        if (null != mTTAd) {
            mTTAd.showInteractionExpressAd(activity);
        }
    }

    @Override
    public void loadAD() {
        try {
            //初始化advance默认的穿山甲配置，也可以自己选择初始化方式
            AdvanceUtil.initCsj(activity, sdkSupplier.mediaid);

            final TTAdManager ttAdManager = TTAdSdk.getAdManager();
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity);
            }
            TTAdNative ttAdNative = ttAdManager.createAdNative(activity);
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(sdkSupplier.adspotid)
                    .setSupportDeepLink(true)
                    .setExpressViewAcceptedSize(300, 300)
                    .setImageAcceptedSize(600, 600) //根据广告平台选择的尺寸，传入同比例尺寸
                    .build();
            ttAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                @Override
                public void onError(int i, String s) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(i,s));
                    }
                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {

                    if (ads == null || ads.size() == 0) {
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd) {
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
                        }
                        return;
                    }
                    mTTAd = ads.get(0);
                    if (null == mTTAd) {
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd) {
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
                        }
                        return;
                    }
                    mTTAd.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
                        @Override
                        public void onAdDismiss() {

                        }

                        @Override
                        public void onAdClicked(View view, int type) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidClicked(sdkSupplier);
                            }
                        }

                        @Override
                        public void onAdShow(View view, int type) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidShow(sdkSupplier);
                            }
                        }

                        @Override
                        public void onRenderFail(View view, String msg, int code) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidFailed(AdvanceError.parseErr(code,msg));
                            }
                        }

                        @Override
                        public void onRenderSuccess(View view, float width, float height) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidSucceed(sdkSupplier);
                            }
                        }
                    });
                    mTTAd.render();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd) {
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
            }
        }
    }

    @Override
    public void destroy() {
        if (mTTAd != null) {
            mTTAd.destroy();
        }
    }
}
