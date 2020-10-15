package com.advance.advancesdkdemo.custom.full;

import android.content.res.Configuration;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;

import static com.advance.model.AdvanceError.ERROR_DATA_NULL;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyCsjFSAdapter extends BaseCustomAdapter {
    private TTFullScreenVideoAd fullScreenVideoAd;

    @Override
    public void loadAD() {
        try {
            AdvanceUtil.initCsj(activity, sdkSupplier.mediaid);

            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdSdk.getAdManager();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity);
            }
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTAdNative mTTAdNative = ttAdManager.createAdNative(activity.getApplicationContext());

            boolean isPortrait = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

            int orientation;
            if (isPortrait) { // 基于当前屏幕方向来设定广告期望方向
                orientation = TTAdConstant.VERTICAL;
            } else {
                orientation = TTAdConstant.HORIZONTAL;
            }
            //step4:创建广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(sdkSupplier.adspotid)
                    .setSupportDeepLink(true)
                    //如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
                    .setExpressViewAcceptedSize(500,500)
                    .setOrientation(orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
//step5:请求广告
            mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {

                @Override
                public void onError(int i, String s) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd)
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(i,s));
                }

                @Override
                public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                    fullScreenVideoAd = ttFullScreenVideoAd;
                    isVideoCached = false;
                    if (fullScreenVideoAd == null) {
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd)
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_DATA_NULL));
                        return;
                    }
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd)
                        customizeAd.adapterDidSucceed();
                    fullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                        @Override
                        public void onAdShow() {

                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd)
                                customizeAd.adapterDidShow();
                        }

                        @Override
                        public void onAdVideoBarClick() {

                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd)
                                customizeAd.adapterDidClicked();
                        }

                        @Override
                        public void onAdClose() {

                        }

                        @Override
                        public void onVideoComplete() {

                        }

                        @Override
                        public void onSkippedVideo() {

                        }
                    });
                }

                @Override
                public void onFullScreenVideoCached() {
                    isVideoCached = true;
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd)
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));

        }
    }

    @Override
    public void showAD() {
        if (fullScreenVideoAd != null) {
            fullScreenVideoAd.showFullScreenVideoAd(activity);
        }
    }

    @Override
    public void destroy() {

    }
}
