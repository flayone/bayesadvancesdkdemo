package com.advance.advancesdkdemo;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化SDK
        AdvanceAD.initAD(this);
    }

}
