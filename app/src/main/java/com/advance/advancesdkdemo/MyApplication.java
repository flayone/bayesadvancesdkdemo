package com.advance.advancesdkdemo;

import android.app.Application;
import android.content.Context;

import com.advance.AdvanceSetting;
import com.advance.advancesdkdemo.advance.AdvanceAD;
import com.advance.model.AdvanceLogLevel;
import com.mercury.sdk.core.config.MercuryAD;
import com.mercury.sdk.core.config.MercuryLogLevel;

public class MyApplication extends Application {

    static MyApplication instance;

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        boolean hasPri = getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE).getBoolean(Constants.SP_AGREE_PRIVACY, false);

        //建议当用户同意了隐私政策以后才调用SDK初始化
        if (hasPri) {
            initSDK();
        }
    }

    public void initSDK() {
        //admore不需要初始化，但advance SDK需要进行初始化才可以
        AdvanceAD.initAD(this);

    }


}
