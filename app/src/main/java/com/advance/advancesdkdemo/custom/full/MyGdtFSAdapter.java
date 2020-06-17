package com.advance.advancesdkdemo.custom.full;

import android.widget.Toast;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.utils.AdvanceUtil;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

public class MyGdtFSAdapter extends BaseCustomAdapter {
    private UnifiedInterstitialAD iad;

    @Override
    public void loadAD() {
        try {
            iad = new UnifiedInterstitialAD(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    isVideoCached = false;
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                }

                @Override
                public void onVideoCached() {
                    //只有loadFullScreenAD成功后立即调用showFullScreenAD时缓冲成功才会回掉，目前没有参考意义
                    isVideoCached = true;
                }

                @Override
                public void onNoAD(AdError adError) {

                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd)
                        customizeAd.adapterDidFailed();
                }

                @Override
                public void onADOpened() {

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
                public void onADLeftApplication() {

                }

                @Override
                public void onADClosed() {

                }
            });

            VideoOption videoOption = new VideoOption.Builder().setAutoPlayMuted(false)
                    .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)
                    .build();
            iad.setMinVideoDuration(0);
            iad.setMaxVideoDuration(60);
            iad.setVideoOption(videoOption);
            iad.loadFullScreenAD();
        } catch (Exception e) {
            e.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd)
                customizeAd.adapterDidFailed();

        }
    }

    @Override
    public void showAD() {
        if (iad != null) {
            iad.showFullScreenAD(activity);
        }
    }

    @Override
    public void destroy() {
        if (iad != null) {
            iad.destroy();
        }
    }
}
