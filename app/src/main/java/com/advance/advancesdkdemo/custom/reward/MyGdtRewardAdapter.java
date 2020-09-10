package com.advance.advancesdkdemo.custom.reward;

import android.os.SystemClock;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.utils.AdvanceUtil;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

public class MyGdtRewardAdapter extends BaseCustomAdapter {
    private RewardVideoAD rewardVideoAD;

    @Override
    public void loadAD() {
        try {
            rewardVideoAD = new RewardVideoAD(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, new RewardVideoADListener() {
                @Override
                public void onADLoad() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                    //收到广告回调
                    if (customRewardListener != null) {
                        customRewardListener.onLoaded();
                    }
                    isVideoCached = false;
                }

                @Override
                public void onVideoCached() {
                    try {
                        //判断是否超过视频展示的最长有效时间，或者已经展示过
                        isVideoCached = true;
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
                public void onADExpose() {

                }

                @Override
                public void onReward() {
                    if (customRewardListener != null)
                        customRewardListener.onReward();
                }

                @Override
                public void onADClick() {
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
                    if (customRewardListener != null )
                        customRewardListener.onClose();
                }

                @Override
                public void onError(AdError adError) {
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
