package com.advance.advancesdkdemo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DemoUtil {
    public static void logAndToast(Context context,String msg){
        Log.d("[DemoUtil][logAndToast]", msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
