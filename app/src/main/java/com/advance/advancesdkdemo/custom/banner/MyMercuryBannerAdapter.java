package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.advance.AdvanceCustomizeAd;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.mercury.sdk.core.banner.BannerAD;
import com.mercury.sdk.core.banner.BannerADListener;
import com.mercury.sdk.util.ADError;

public class MyMercuryBannerAdapter {
    private Activity activity;
    private SdkSupplier sdkSupplier;
    private AdvanceCustomizeAd advanceBanner;
    private ViewGroup adContainer;

    public MyMercuryBannerAdapter(Activity activity, ViewGroup adContainer, final AdvanceCustomizeAd advanceBanner, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.advanceBanner = advanceBanner;
        this.sdkSupplier = sdkSupplier;
        this.adContainer = adContainer;
    }


    public void loadAd() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);
            BannerAD mercuryBanner = new BannerAD(activity, sdkSupplier.adspotid, new BannerADListener() {
                @Override
                public void onADReceived() {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidSucceed();
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
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidShow();
                    }
                }

                @Override
                public void onADClicked() {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidClicked();
                    }
                }

                @Override
                public void onNoAD(ADError adError) {
                    if (null != advanceBanner) {
                        advanceBanner.adapterDidFailed();
                    }
                }
            });
            RelativeLayout.LayoutParams rbl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            rbl.addRule(RelativeLayout.CENTER_HORIZONTAL);
            adContainer.removeAllViews();
            adContainer.addView(mercuryBanner, rbl);
            mercuryBanner.loadAD();

        } catch (Exception e) {
            e.printStackTrace();
            if (null != advanceBanner)
                advanceBanner.adapterDidFailed();
        }

    }
}
