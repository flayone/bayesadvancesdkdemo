package com.bayescom.advancesdkdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bayesadvance.AdvanceConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置SDK配置
        AdvanceConfig.getInstance().setAppName("测试app");
        AdvanceConfig.getInstance().setDebug(true);
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
    public void onNativeExpressRecyclerView(View view)
    {
        startActivity(new Intent(this,NativeExpressRecyclerViewActivity.class));
    }
    public void onNativeRecyclerView(View view)
    {
       startActivity(new Intent(this,NativeRecyclerViewActivity.class));
    }
}
