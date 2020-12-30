package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.model.AdvanceError;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private String TAG = "DEMO BANNER";
    private AdvanceBanner advanceBanner;
    FrameLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        rl = findViewById(R.id.banner_layout);
        //这里是获取测试广告位id，实际请替换成自己应用的正式广告位id！
        String adspotId = ADManager.getInstance().getBannerAdspotId();
        advanceBanner = new AdvanceBanner(this, rl, adspotId);

        //推荐：核心事件监听回调
        advanceBanner.setAdListener(this);
        advanceBanner.loadStrategy();
    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "SHOW");
        Toast.makeText(this, "广告展现", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {

        Log.d(TAG, "Failed");
        Toast.makeText(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSdkSelected(String id) {

    }

    @Override
    public void onAdClicked() {
        Log.d(TAG, "Clicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDislike() {
        rl.removeAllViews();
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceBanner.destroy();
    }
}
