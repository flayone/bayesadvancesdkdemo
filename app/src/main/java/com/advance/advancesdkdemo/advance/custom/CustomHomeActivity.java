package com.advance.advancesdkdemo.advance.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.advance.advancesdkdemo.R;
import com.advance.advancesdkdemo.advance.SplashActivity;
import com.advance.advancesdkdemo.advance.custom.nativ.NativeCustomizeActivity;

public class CustomHomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus);
    }



    public void cusAD(View view) {
        startActivity(new Intent(this, NativeCustomizeActivity.class));
    }

    public void cusHW(View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("cusHW", true);
        startActivity(intent);
    }

    public void cusXM(View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("cusXM", true);
        startActivity(intent);
    }
}
