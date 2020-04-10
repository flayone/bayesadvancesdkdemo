package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.advance.AdvanceConfig;
import com.advance.model.SdkSupplier;
import com.advance.utils.AdvanceUtil;
import com.advance.utils.LogUtil;
import com.mercury.sdk.core.banner.BannerAD;
import com.mercury.sdk.core.banner.BannerADListener;
import com.mercury.sdk.core.config.AdConfigManager;
import com.mercury.sdk.util.ADError;

public class MyMercuryBannerAdapter {
    private Activity activity;
    private SdkSupplier sdkSupplier;
    private MyBannerAd advanceBanner;
    private ViewGroup adContainer;

    public MyMercuryBannerAdapter(Activity activity, ViewGroup adContainer, final MyBannerAd advanceBanner, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.advanceBanner = advanceBanner;
        this.sdkSupplier = sdkSupplier;
        this.adContainer = adContainer;
    }


    public void loadAd() {
        try {
            AdvanceUtil.initMercuryAccount(sdkSupplier.mediaid, sdkSupplier.mediakey);
            AdConfigManager.getInstance().setOaId(AdvanceConfig.getInstance().getOaid());
            BannerAD mercuryBanner = new BannerAD(activity, sdkSupplier.adspotid, new BannerADListener() {
                @Override
                public void onADReceived() {
                    if (null != advanceBanner) {
                        advanceBanner.onLoaded();
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
                        advanceBanner.onShow();
                    }
                }

                @Override
                public void onADClicked() {
                    if (null != advanceBanner) {
                        advanceBanner.onClicked();
                    }
                }

                @Override
                public void onNoAD(ADError adError) {
                    LogUtil.AdvanceLog(adError.code + adError.msg);
                    if (null != advanceBanner) {
                        advanceBanner.onFailed();
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
                advanceBanner.onFailed();
        }

    }
}
