package com.advance.advancesdkdemo.custom.nativeexpress;

import android.app.Activity;
import android.view.View;

import com.advance.AdvanceConfig;
import com.advance.AdvanceCustomizeAd;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;

import java.util.ArrayList;
import java.util.List;

import static com.advance.model.AdvanceError.ERROR_DATA_NULL;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyCsjNEAdapter {

    private Activity activity;
    private AdvanceCustomizeAd customizeAd;
    private SdkSupplier sdkSupplier;
    private CustomExpressEventListener listener;

    public MyCsjNEAdapter(Activity activity, AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier, CustomExpressEventListener listener) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
        this.listener = listener;
    }

    public void loadAd() {
        try {
            //初始化advance默认的穿山甲配置，也可以自己选择初始化方式
            AdvanceUtil.initCsj(activity, sdkSupplier.mediaid);

            final TTAdManager ttAdManager = TTAdSdk.getAdManager();
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity);
            }
            TTAdNative ttAdNative = ttAdManager.createAdNative(activity);
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(sdkSupplier.adspotid) //广告位id
                    .setSupportDeepLink(true)
                    .setAdCount(sdkSupplier.adCount) //请求广告数量为1到3条
                    .setExpressViewAcceptedSize(640, 320) //期望模板广告view的size,单位dp
                    .setImageAcceptedSize(640, 320)
                    .build();
            //加载广告
            ttAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                    try {
                        if (list == null || list.size() == 0) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
                            }
                        } else {
                            List<CustomExpressAdItem> customExpressAdItems = new ArrayList<>();
                            for (TTNativeExpressAd ttNativeExpressAd : list) {
                                CustomExpressAdItem advanceNativeExpressAdItem = new CsjCustomExpressAdItem(ttNativeExpressAd);
                                customExpressAdItems.add(advanceNativeExpressAdItem);
                            }
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidSucceed();
                            }
                            if (null != listener) {
                                listener.onADLoaded(customExpressAdItems);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //这里一定要调用customizeAd 的事件方法
                        if (customizeAd != null)
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd) {
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
            }
        }

    }

    class CsjCustomExpressAdItem implements CustomExpressAdItem {
        TTNativeExpressAd ttNativeExpressAd;
        public CsjCustomExpressAdItem(final TTNativeExpressAd ttNativeExpressAd) {
            this.ttNativeExpressAd = ttNativeExpressAd;

            ttNativeExpressAd.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                @Override
                public void onAdClicked(View view, int i) {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null)
                        customizeAd.adapterDidClicked();
                }

                @Override
                public void onAdShow(View view, int i) {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null)
                        customizeAd.adapterDidShow();

                }

                @Override
                public void onRenderFail(View view, String s, int i) {
                    //这里一定要调用customizeAd 的事件方法
                    if (customizeAd != null)
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(i,s));

                    if (listener!=null){
                        listener.onADRenderFailed(ttNativeExpressAd.getExpressAdView());
                    }
                }

                @Override
                public void onRenderSuccess(View view, float v, float v1) {


                }
            });

            TTAdDislike.DislikeInteractionCallback dislikeInteractionCallback1 = new TTAdDislike.DislikeInteractionCallback() {
                @Override
                public void onShow() {

                }

                @Override
                public void onSelected(int i, String s) {
                    if (listener!=null){
                        listener.onADClosed(ttNativeExpressAd.getExpressAdView());
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onRefuse() {

                }
            };
            ttNativeExpressAd.setDislikeCallback(activity, dislikeInteractionCallback1);
        }

        @Override
        public void destroy() {
            if (null != ttNativeExpressAd) {
                ttNativeExpressAd.destroy();
            }
        }

        @Override
        public void render() {
            if (ttNativeExpressAd != null) {
                ttNativeExpressAd.render();
            }
        }

        @Override
        public View getExpressAdView() {
            if (ttNativeExpressAd != null) {
                return ttNativeExpressAd.getExpressAdView();
            }
            return null;
        }
    }
}
