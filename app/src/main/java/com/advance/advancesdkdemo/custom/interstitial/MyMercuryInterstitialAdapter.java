package com.advance.advancesdkdemo.custom.interstitial;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.mercury.sdk.core.interstitial.InterstitialAD;
import com.mercury.sdk.core.interstitial.InterstitialADListener;
import com.mercury.sdk.util.ADError;

import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

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
                        customizeAd.adapterDidSucceed(sdkSupplier);
                    }
                }

                @Override
                public void onADOpened() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow(sdkSupplier);
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
                        customizeAd.adapterDidClicked(sdkSupplier);
                    }
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
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(code,msg));
                    }
                    LogUtil.AdvanceLog(code + msg);

                }
            });
            interstitialAD.loadAD();
        } catch (Throwable t) {
            t.printStackTrace();
            if (customizeAd != null)
                customizeAd.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
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
