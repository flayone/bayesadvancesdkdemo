package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceRewardVideo;
import com.advance.AdvanceRewardVideoItem;
import com.advance.AdvanceRewardVideoListener;
import com.advance.csj.CsjRewardVideoAdItem;
import com.advance.model.AdvanceError;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

public class RewardVideoActivity extends AppCompatActivity implements AdvanceRewardVideoListener {
    private AdvanceRewardVideo advanceRewardVideo;
    private AdvanceRewardVideoItem advanceRewardVideoItem;
    private boolean isReady = false;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        advanceRewardVideo = new AdvanceRewardVideo(this, ADManager.getInstance().getRewardAdspotId());
        //如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
        advanceRewardVideo.setCsjExpressSize(500, 500);
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(this);

    }

    public void onLoad(View view) {
        advanceRewardVideo.loadStrategy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        //广告已经调用过，且页面从后台恢复前台后重新展示广告
        if (isReady && advanceRewardVideoItem != null) {
            //展示广告
            advanceRewardVideoItem.showAd();
        }
    }

    private void showReward() {
        if (advanceRewardVideoItem != null) {
            //设置穿山甲的回调（必须）
            if (advanceRewardVideoItem.getSdkId().equals(AdvanceConfig.SDK_ID_CSJ)) {
                CsjRewardVideoAdItem csjRewardVideoAdItem = (CsjRewardVideoAdItem) advanceRewardVideoItem;
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
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {

                    }

//                    @Override
//                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName, int errorCode, String errMsg) {
//
//                    }

                    @Override
                    public void onSkippedVideo() {

                    }
                });
            }
            //展示广告
            advanceRewardVideoItem.showAd();
        }
    }

    @Override
    public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
        isReady = true;

        Log.d("DEMO", "LOADED");
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
        this.advanceRewardVideoItem = advanceRewardVideoItem;

        //页面非暂停状态才展示广告
        if (!isPause) {
            showReward();
        }
    }

    @Override
    public void onAdShow() {
        Log.d("DEMO", "SHOW");
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        isReady = false;

        Log.d("DEMO", "FAILED");
        Toast.makeText(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d("DEMO", "CLICKED");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onVideoCached() {
        isReady = true;

        Log.d("DEMO", "CACHED");
        Toast.makeText(this, "广告缓存成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoComplete() {
        isReady = false;

        Log.d("DEMO", "VIDEO COMPLETE");
        Toast.makeText(this, "视频播放完毕", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClose() {
        isReady = false;

        Log.d("DEMO", "AD CLOSE");
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdReward() {
        isReady = false;

        Log.d("DEMO", "AD REWARD");
        Toast.makeText(this, "激励发放", Toast.LENGTH_SHORT).show();
    }

}
