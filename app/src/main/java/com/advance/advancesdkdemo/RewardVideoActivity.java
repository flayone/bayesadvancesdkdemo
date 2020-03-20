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
        advanceRewardVideo = new AdvanceRewardVideo(this, ADManager.getInstance().getMediaId(), ADManager.getInstance().getRewardAdspotId());
        //设置穿山甲相关参数(如果有的话)
        advanceRewardVideo.setCsjImageAcceptedSize(1080, 1920);
        advanceRewardVideo.setCsjRewardName("金币");
        advanceRewardVideo.setOrientation(AdvanceRewardVideo.ORIENTATION_HORIZONTAL);
        advanceRewardVideo.setCsjUserId("user123");
        advanceRewardVideo.setCsjRewardAmount(Toast.LENGTH_SHORT);
        advanceRewardVideo.setDefaultSdkSupplier(new SdkSupplier("1101152570", "2090845242931421", null, AdvanceConfig.SDK_TAG_GDT));
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(this);

    }

    public void onLoad(View view) {
        isReady = false;
        advanceRewardVideo.loadAd();

    }

    public void onShow(View view) {
        if (isReady) {
            if (AdvanceConfig.SDK_TAG_CSJ.equals(advanceRewardVideoItem.getSdkTag())) {
                //穿山甲SDK特定设置
                CsjRewardVideoAdItem csjRewardVideoAdItem = (CsjRewardVideoAdItem) advanceRewardVideoItem;
                //设置穿山甲SDK监听器（必须），可以监听穿山甲sdk特定回调,通用的回调同时会回调通用监听器
                csjRewardVideoAdItem.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                    @Override
                    public void onAdShow() {

                    }

                    @Override
                    public void onAdVideoBarClick() {

                    }

                    @Override
                    public void onAdClose() {

                    }

                    @Override
                    public void onVideoComplete() {

                    }

                    @Override
                    public void onVideoError() {

                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s) {

                    }

                    @Override
                    public void onSkippedVideo() {

                    }
                });
                csjRewardVideoAdItem.setShowDownLoadBar(true);

                csjRewardVideoAdItem.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {

                    }

                    @Override
                    public void onDownloadActive(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadPaused(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadFailed(long l, long l1, String s, String s1) {

                    }

                    @Override
                    public void onDownloadFinished(long l, String s, String s1) {

                    }

                    @Override
                    public void onInstalled(String s, String s1) {

                    }
                });
            }
            advanceRewardVideoItem.showAD();

        } else {
            Toast.makeText(this, "广告未加载", Toast.LENGTH_SHORT).show();
        }

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
    public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
        Log.d("DEMO", "LOADED");
        this.advanceRewardVideoItem = advanceRewardVideoItem;
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();


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

    }

}
