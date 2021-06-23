package com.advance.advancesdkdemo.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.custom.banner.CustomBannerActivity;
import com.advance.advancesdkdemo.custom.full.CustomFullScreenActivity;
import com.advance.advancesdkdemo.custom.interstitial.CustomInterstitialActivity;
import com.advance.advancesdkdemo.custom.nativ.NativeCustomizeActivity;
import com.advance.advancesdkdemo.custom.nativeexpress.CustomNativeExpressListActivity;
import com.advance.advancesdkdemo.custom.reward.CustomRewardActivity;
import com.advance.advancesdkdemo.custom.splash.CustomSplashActivity;

public class CustomActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus);
    }



    public void onCusBanner(View view) {
        startActivity(new Intent(this, CustomBannerActivity.class));
    }

    public void onCusNative(View view) {
        startActivity(new Intent(this, NativeCustomizeActivity.class));
    }


    public void cusSplash(View view) {
        startActivity(new Intent(this, CustomSplashActivity.class));
    }

    public void cusInterstitial(View view) {
        startActivity(new Intent(this, CustomInterstitialActivity.class));
    }

    public void cusExpress(View view) {
        startActivity(new Intent(this, CustomNativeExpressListActivity.class));
    }

    public void cusReward(View view) {
        startActivity(new Intent(this, CustomRewardActivity.class));
    }

    public void cusFullSV(View view) {
        startActivity(new Intent(this, CustomFullScreenActivity.class));
    }
}
