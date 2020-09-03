package com.advance.advancesdkdemo.custom.reward;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.utils.AdvanceUtil;
import com.mercury.sdk.core.rewardvideo.RewardVideoAD;
import com.mercury.sdk.core.rewardvideo.RewardVideoADListener;
import com.mercury.sdk.util.ADError;

public class MyMercuryRewardAdapter extends BaseCustomAdapter {

    private RewardVideoAD rewardVideoAD;

    @Override
    public void loadAD() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid,sdkSupplier.mediakey);

            rewardVideoAD = new RewardVideoAD(activity, sdkSupplier.adspotid, new RewardVideoADListener() {
                @Override
                public void onADLoad() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                    isVideoCached = false;
                    //收到广告回调
                    if (customRewardListener!=null){
                        customRewardListener.onLoaded();
                    }
                }

                @Override
                public void onVideoCached() {
                    try {
                        //判断是否已经展示过
                        if (  rewardVideoAD.hasShown()) {
                            //这里一定要调用customizeAd 的事件方法
                            if (null != customizeAd) {
                                customizeAd.adapterDidFailed();
                            }
                        } else {
                            isVideoCached = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onADShow() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow();
                    }
                }

                @Override
                public void onADExposure() {

                }

                @Override
                public void onReward() {
                    if (customRewardListener != null)
                        customRewardListener.onReward();
                }

                @Override
                public void onADClicked() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked();
                    }
                }

                @Override
                public void onVideoComplete() {

                }

                @Override
                public void onADClose() {

                }

                @Override
                public void onNoAD(ADError adError) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed();
                    }
                }
            });
            rewardVideoAD.loadAD();
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd) {
                customizeAd.adapterDidFailed();
            }
        }

    }

    @Override
    public void showAD() {
        if (rewardVideoAD != null) {
            rewardVideoAD.showAD();
        }
    }

    @Override
    public void destroy() {

    }
}
