package com.advance.advancesdkdemo.custom.reward;

import android.os.SystemClock;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;

import java.util.Map;

import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;
import static com.advance.model.AdvanceError.ERROR_EXCEPTION_SHOW;

public class MyGdtRewardAdapter extends BaseCustomAdapter {
    private RewardVideoAD rewardVideoAD;

    private long expireTimestamp = 0;
    private long expireBuffer = 1000;

    @Override
    public void loadAD() {
        try {
            rewardVideoAD = new RewardVideoAD(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, new RewardVideoADListener() {
                @Override
                public void onADLoad() {
                    //检查激励视频是否可用
                    if (checkRewardOk(rewardVideoAD)) {
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd) {
                            customizeAd.adapterDidSucceed(sdkSupplier);
                        }
                        //收到广告回调
                        if (customRewardListener != null) {
                            customRewardListener.onLoaded();
                        }
                    } else {
                        //这里一定要调用customizeAd 的事件方法
                        if (null != customizeAd) {
                            customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
                        }
                    }
                    isVideoCached = false;
                }

                @Override
                public void onVideoCached() {
                    isVideoCached = true;
                }

                @Override
                public void onADShow() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow(sdkSupplier);
                    }
                }

                @Override
                public void onADExpose() {

                }

                @Override
                public void onReward(Map<String, Object> map) {
                    if (customRewardListener != null)
                        customRewardListener.onReward();
                }

                @Override
                public void onADClick() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked(sdkSupplier);
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
                public void onError(AdError adError) {
                    int code = -1;
                    String msg = "default onNoAD";
                    if (adError != null) {
                        code = adError.getErrorCode();
                        msg = adError.getErrorMsg();
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
            //检查广告是否可以展示
            if (checkRewardOk(rewardVideoAD)) {
                rewardVideoAD.showAD();
            } else {
                //这里一定要调用customizeAd 的事件方法
                if (null != customizeAd) {
                    customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_SHOW));
                }
            }
        }
    }

    @Override
    public void destroy() {

    }


    public boolean checkRewardOk(RewardVideoAD rewardVideoAD) {
        try {
            if (rewardVideoAD != null)
                expireTimestamp = rewardVideoAD.getExpireTimestamp();
            LogUtil.AdvanceLog("[gdt] elapsedRealtime = " + SystemClock.elapsedRealtime() + "  expireTimestamp = " + expireTimestamp);

            if (SystemClock.elapsedRealtime() >= (expireTimestamp - expireBuffer)) {
                LogUtil.AdvanceLog("[gdt] 激励视频广告已过期");
                return false;
            } else if (rewardVideoAD != null && rewardVideoAD.hasShown()) {
                LogUtil.AdvanceLog("[gdt] 当前激励视频广告已被展示过");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
