package com.advance.advancesdkdemo.custom.reward;

import android.content.res.Configuration;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.utils.AdvanceUtil;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

public class MyCsjRewardAdapter extends BaseCustomAdapter {

    private TTRewardVideoAd ttRewardVideoAd;

    @Override
    public void loadAD() {
        try {
            AdvanceUtil.initCsj(activity, sdkSupplier.mediaid);

            final TTAdManager ttAdManager = TTAdSdk.getAdManager();
            if (AdvanceConfig.getInstance().isNeedPermissionCheck()) {
                ttAdManager.requestPermissionIfNecessary(activity);
            }
            TTAdNative ttAdNative = ttAdManager.createAdNative(activity);

            boolean isPortrait = activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

            int orientation;
            if (isPortrait) { // 基于当前屏幕方向来设定广告期望方向
                orientation = TTAdConstant.VERTICAL;
            } else {
                orientation = TTAdConstant.HORIZONTAL;
            }

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(sdkSupplier.adspotid)
                    .setSupportDeepLink(true)
                    .setAdCount(1)
                    .setImageAcceptedSize(1080, 1920)
                    .setRewardName("") //奖励的名称
                    .setRewardAmount(1)   //奖励的数量
                    //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                    //可设置为空字符串
                    .setUserID("")
                    .setOrientation(orientation)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                    .setMediaExtra("") //用户透传的信息，可不传
                    .build();
            ttAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
                @Override
                public void onError(int i, String s) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd)
                        customizeAd.adapterDidFailed();

                }

                @Override
                public void onRewardVideoAdLoad(TTRewardVideoAd ttRewardVideoAd) {
                    MyCsjRewardAdapter.this.ttRewardVideoAd = ttRewardVideoAd;
                    MyCsjRewardAdapter.this.isVideoCached = false;
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd)
                        customizeAd.adapterDidSucceed();
                    //收到广告回调
                    if (customRewardListener!=null){
                        customRewardListener.onLoaded();
                    }
                    ttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
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
                        public void onVideoError() {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd)
                                customizeAd.adapterDidFailed();
                        }

                        @Override
                        public void onRewardVerify(boolean b, int i, String s) {
                            if (customRewardListener != null && b)
                                customRewardListener.onReward();
                        }

                        @Override
                        public void onSkippedVideo() {

                        }
                    });
                }

                @Override
                public void onRewardVideoCached() {
                    isVideoCached = true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd)
                customizeAd.adapterDidFailed();
        }
    }

    @Override
    public void showAD() {
        if (ttRewardVideoAd != null)
            ttRewardVideoAd.showRewardVideoAd(activity);
    }

    @Override
    public void destroy() {
    }
}
