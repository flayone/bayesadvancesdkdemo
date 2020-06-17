package com.advance.advancesdkdemo.custom.interstitial;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.utils.AdvanceUtil;
import com.mercury.sdk.core.interstitial.InterstitialAD;
import com.mercury.sdk.core.interstitial.InterstitialADListener;
import com.mercury.sdk.util.ADError;

public class MyMercuryInterstitialAdapter extends BaseCustomAdapter {
    private InterstitialAD interstitialAD;

    @Override
    public void loadAD() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);
            interstitialAD = new InterstitialAD(activity, sdkSupplier.adspotid, new InterstitialADListener() {
                @Override
                public void onADReceive() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed();
                    }
                }

                @Override
                public void onADOpened() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow();
                    }
                }

                @Override
                public void onADClosed() {

                }

                @Override
                public void onADLeftApplication() {

                }

                @Override
                public void onADExposure() {

                }

                @Override
                public void onADClicked() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked();
                    }
                }

                @Override
                public void onNoAD(ADError adError) {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed();
                    }

                }
            });
            interstitialAD.loadAD();
        } catch (Throwable t) {
            t.printStackTrace();
            if (customizeAd != null)
                customizeAd.adapterDidFailed();
        }


    }

    @Override
    public void showAD() {
        if (null != interstitialAD) {
            interstitialAD.show();
        }
    }

    @Override
    public void destroy() {
        if (null != interstitialAD) {
            interstitialAD.destroy();
        }
    }
}
