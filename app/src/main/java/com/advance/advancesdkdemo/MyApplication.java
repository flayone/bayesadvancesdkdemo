package com.advance.advancesdkdemo;

import android.app.Application;
import android.content.Context;

import com.advance.advancesdkdemo.advance.ADUtil;

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


        ADUtil.init(this);
    }


}
