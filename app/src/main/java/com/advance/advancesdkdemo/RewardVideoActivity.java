package com.advance.advancesdkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceRewardVideo;
import com.advance.AdvanceRewardVideoItem;
import com.advance.AdvanceRewardVideoListener;
import com.advance.csj.CsjRewardVideoAdItem;
import com.advance.gdt.GdtRewardVideoAdItem;
import com.advance.mercury.MercuryRewardVideoAdItem;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

public class RewardVideoActivity extends AppCompatActivity implements AdvanceRewardVideoListener {
    private AdvanceRewardVideo advanceRewardVideo;
    private AdvanceRewardVideoItem advanceRewardVideoItem;
    private boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        advanceRewardVideo = new AdvanceRewardVideo(this, ADManager.getInstance().getRewardAdspotId());
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(this);

    }

    public void onLoad(View view) {
        isReady = false;
        advanceRewardVideo.loadStrategy();

    }

    public void onShow(View view) {
        if (isReady) {
            //展示广告
            advanceRewardVideoItem.showAd();
        } else {
            Toast.makeText(this, "广告未加载", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
        isReady = true;
        Log.d("DEMO", "LOADED");
        this.advanceRewardVideoItem = advanceRewardVideoItem;
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdShow() {
        Log.d("DEMO", "SHOW");
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {
        Log.d("DEMO", "FAILED");
        Toast.makeText(this, "广告加载失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d("DEMO", "CLICKED");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onVideoCached() {
        Log.d("DEMO", "CACHED");
        isReady = true;
        Toast.makeText(this, "广告缓存成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoComplete() {
        Log.d("DEMO", "VIDEO COMPLETE");
        Toast.makeText(this, "视频播放完毕", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClose() {
        Log.d("DEMO", "AD CLOSE");

        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdReward() {
        Log.d("DEMO", "AD REWARD");
        Toast.makeText(this, "激励发放", Toast.LENGTH_SHORT).show();
        isReady = false;
    }

}
