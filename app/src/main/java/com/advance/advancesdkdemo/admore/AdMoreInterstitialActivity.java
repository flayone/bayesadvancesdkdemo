package com.advance.advancesdkdemo.admore;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.advance.AdvanceAD;
import com.bayescom.admore.core.AMError;
import com.bayescom.admore.interstitial.AdMoreInterstitial;
import com.bayescom.admore.interstitial.AdMoreInterstitialListener;

public class AdMoreInterstitialActivity extends Activity {

    AdMoreInterstitial adMoreInterstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

    }

    private void initAD() {
        adMoreInterstitial = new AdMoreInterstitial(this, Constants.TestIds.adMoreInterstitialAdspotId, new AdMoreInterstitialListener() {
            @Override
            public void onAdClose() {
                AdvanceAD.logAndToast("onAdClose");
            }

            @Override
            public void onSuccess() {
                AdvanceAD.logAndToast("onSuccess");

            }

            @Override
            public void onShow() {
                AdvanceAD.logAndToast("onShow");

            }

            @Override
            public void onClick() {
                AdvanceAD.logAndToast("onClick");

            }

            @Override
            public void onFailed(AMError amError) {
                AdvanceAD.logAndToast("onFailed :" + amError.toString());
            }
        });
        adMoreInterstitial.loadOnly();
    }


    public void loadAd(View view) {
        initAD();
    }


    public void showAd(View view) {
        if (adMoreInterstitial != null)
            adMoreInterstitial.show();
    }
}

