package com.bayescom.advancesdkdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onBanner(View view)
    {
        startActivity(new Intent(this,BannerActivity.class));

    }
    public void onSplash(View view)
    {
        startActivity(new Intent(this,SplashActivity.class));
    }
    public void onNativeExpress(View view)
    {
       startActivity(new Intent(this,NativeExpressActivity.class));
    }
    public void onRewardVideo(View view)
    {
        startActivity(new Intent(this,RewardVideoActivity.class));
    }
    public void onNative(View view)
    {
        startActivity(new Intent(this,NativeActivity.class));
    }
}
