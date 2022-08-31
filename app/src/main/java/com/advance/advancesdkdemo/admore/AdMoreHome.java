package com.advance.advancesdkdemo.admore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.advance.advancesdkdemo.R;

public class AdMoreHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_more_home);
    }

    public void amSplash(View view) {
        startActivity(new Intent(this, AdMoreSplashActivity.class));
    }

    public void amNative(View view) {
        startActivity(new Intent(this, AdMoreNativeActivity.class));
    }

    public void amReward(View view) {

    }
}