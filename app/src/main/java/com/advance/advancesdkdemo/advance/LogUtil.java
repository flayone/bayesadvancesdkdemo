package com.advance.advancesdkdemo.advance;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "LogUtil";

    public static void high(String msg){
        Log.d(TAG, "high: "+msg);
    }
}
