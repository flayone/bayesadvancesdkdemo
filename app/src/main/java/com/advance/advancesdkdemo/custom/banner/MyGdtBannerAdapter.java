package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.advance.AdvanceCustomizeAd;
import com.advance.model.AdvanceError;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

import static com.advance.model.AdvanceError.ERROR_EXCEPTION_LOAD;

public class MyGdtBannerAdapter {
    private Activity activity;
    private SdkSupplier sdkSupplier;
    private AdvanceCustomizeAd advanceBanner;
    private ViewGroup adContainer;
    private UnifiedBannerView bv;
    private int refreshInterval = 30;

    public MyGdtBannerAdapter(Activity activity, ViewGroup adContainer, final AdvanceCustomizeAd advanceBanner, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.advanceBanner = advanceBanner;
        this.sdkSupplier = sdkSupplier;
        this.adContainer = adContainer;
    }

    public void loadAd() {
        try {

            bv = new UnifiedBannerView(activity, AdvanceUtil.getGdtAccount(sdkSupplier.mediaid), sdkSupplier.adspotid, new UnifiedBannerADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    int code = -1;
                    String msg = "default onNoAD";
                    if (adError != null) {
                        code = adError.getErrorCode();
                        msg = adError.getErrorMsg();
                    }
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidFailed(AdvanceError.parseErr(code,msg));
                    }
                    LogUtil.AdvanceLog(code + msg);
                }

                @Override
                public void onADReceive() {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidSucceed(sdkSupplier);
                    }

                }

                @Override
                public void onADExposure() {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidShow(sdkSupplier);
                    }

                }

                @Override
                public void onADClosed() {

                }

                @Override
                public void onADClicked() {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidClicked(sdkSupplier);
                    }

                }

                @Override
                public void onADLeftApplication() {

                }

                @Override
                public void onADOpenOverlay() {

                }

                @Override
                public void onADCloseOverlay() {

                }
            });
            bv.setRefresh(refreshInterval);
            adContainer.removeAllViews();
            adContainer.addView(bv, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            /* 发起广告请求，收到广告数据后会展示数据   */
            bv.loadAD();
        } catch (Exception e) {
            e.printStackTrace();
            if (advanceBanner != null)
                advanceBanner.adapterDidFailed(AdvanceError.parseErr(ERROR_EXCEPTION_LOAD));
        }
    }

    public void destroy() {
        if (null != bv) {
            bv.destroy();
        }
    }
}
