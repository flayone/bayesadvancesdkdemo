package com.advance.advancesdkdemo.custom;

import android.app.Activity;

import com.advance.AdvanceCustomizeAd;
import com.advance.advancesdkdemo.custom.reward.CustomRewardListener;
import com.advance.model.SdkSupplier;

public abstract class BaseCustomAdapter {
    protected Activity activity;
    protected AdvanceCustomizeAd customizeAd;
    protected SdkSupplier sdkSupplier;
    protected CustomRewardListener customRewardListener;
    public boolean isVideoCached = false;

    public void init(Activity activity, AdvanceCustomizeAd customizeAd, SdkSupplier sdkSupplier) {
        this.activity = activity;
        this.customizeAd = customizeAd;
        this.sdkSupplier = sdkSupplier;
    }


    public abstract void loadAD();

    public abstract void showAD();

    public abstract void destroy();

    public void setCustomRewardListener(CustomRewardListener customRewardListener) {
        this.customRewardListener = customRewardListener;
    }

    public CustomRewardListener getCustomRewardListener() {
        return customRewardListener;
    }
}
