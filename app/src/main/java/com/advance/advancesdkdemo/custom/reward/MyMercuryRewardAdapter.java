package com.advance.advancesdkdemo.custom.reward;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.mercury.sdk.core.rewardvideo.RewardVideoAD;
import com.mercury.sdk.core.rewardvideo.RewardVideoADListener;
import com.mercury.sdk.util.ADError;

import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_SHOW;

public class MyMercuryRewardAdapter extends BaseCustomAdapter {

    private RewardVideoAD rewardVideoAD;

    @Override
    public void loadAD() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);

            rewardVideoAD = new RewardVideoAD(activity, sdkSupplier.adspotid, new RewardVideoADListener() {
                @Override
                public void onADLoad() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                    isVideoCached = false;
                    //收到广告回调
                    if (customRewardListener != null) {
                        customRewardListener.onLoaded();
                    }
                }

                @Override
                public void onVideoCached() {

                    isVideoCached = true;


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
                    if (customRewardListener != null)
                        customRewardListener.onClose();
                }

                @Override
                public void onNoAD(ADError adError) {
                    int code = -1;
                    String msg = "default onNoAD";
                    if (adError != null) {
                        code = adError.code;
                        msg = adError.msg;
                    }
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(code, msg));
                    }
                    LogUtil.AdvanceLog(code + msg);
                }
            });
            rewardVideoAD.loadAD();
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd) {
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
            }
        }

    }

    @Override
    public void showAD() {
        if (rewardVideoAD != null) {
            if (rewardVideoAD.hasShown()) {
                //这里一定要调用customizeAd 的事件方法
                if (null != customizeAd) {
                    customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_SHOW));
                }
            } else {
                rewardVideoAD.showAD();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
