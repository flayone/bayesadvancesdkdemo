package com.advance.advancesdkdemo.custom.interstitial;

import com.advance.advancesdkdemo.custom.BaseCustomAdapter;
import com.advance.model.AdvanceError;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyGdtInterstitialAdapter extends BaseCustomAdapter {
    private UnifiedInterstitialAD interstitialAD;

    @Override
    public void loadAD() {
        try {
            UnifiedInterstitialADListener listener = new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidSucceed(sdkSupplier);
                    }
                    // onADReceive之后才能调用getAdPatternType()
                    if (interstitialAD != null && interstitialAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
//                        interstitialAD.setMediaListener(mediaListener);
                    }
                }

                @Override
                public void onVideoCached() {

                }

                @Override
                public void onNoAD(AdError adError) {
                    int code = -1;
                    String msg = "default onNoAD";
                    if (adError != null) {
                        code = adError.getErrorCode();
                        msg = adError.getErrorMsg();
                    }
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidFailed(AdvanceError.parseErr(code,msg));
                    }
                    LogUtil.AdvanceLog(code + msg);


                }

                @Override
                public void onADOpened() {

                }

                @Override
                public void onADExposure() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidShow(sdkSupplier);
                    }
                }

                @Override
                public void onADClicked() {
                    //这里一定要调用customizeAd 的事件方法
                    if (null != customizeAd) {
                        customizeAd.adapterDidClicked(sdkSupplier);
                    }
                }

                @Override
                public void onADLeftApplication() {

                }

                @Override
                public void onADClosed() {

                }
            };

            interstitialAD = new UnifiedInterstitialAD(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, listener);
            interstitialAD.loadAD();

        } catch (Throwable t) {
            t.printStackTrace();
            //这里一定要调用customizeAd 的事件方法
            if (null != customizeAd)
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
