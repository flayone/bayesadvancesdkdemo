package com.bayescom.admore.gm;

import android.content.Context;
import android.util.Log;

import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomInitConfig;
import com.bytedance.msdk.api.v2.ad.custom.init.GMCustomAdapterConfiguration;

import java.util.Map;

public class AdMoreCustomConfig extends GMCustomAdapterConfiguration {
    String TAG = "[" + AdMoreCustomConfig.class.getSimpleName() + "] ";

    @Override
    public void initializeADN(final Context context, final GMCustomInitConfig gmCustomInitConfig, Map<String, Object> map) {
        // getAdapterSdkVersion 一定要赋值，否则无法执行到此初始化方法
        Log.i(TAG,"initializeADN：" + gmCustomInitConfig.getAppId());
        callInitSuccess();
    }

    @Override
    public String getNetworkSdkVersion() {
        return "5.0.0";
    }

    @Override
    public String getAdapterSdkVersion() {
        return "1.0.0";
    }
}
