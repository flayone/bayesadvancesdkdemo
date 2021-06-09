package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.advance.AdvanceRewardVideo;
import com.advance.AdvanceRewardVideoItem;
import com.advance.AdvanceRewardVideoListener;
import com.advance.RewardServerCallBackInf;
import com.advance.model.AdvanceError;

public class RewardVideoActivity extends AppCompatActivity implements AdvanceRewardVideoListener {
    private AdvanceRewardVideo advanceRewardVideo;
    private AdvanceRewardVideoItem advanceRewardVideoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);


        //初始化，注意需要时再初始化，不要复用。
        advanceRewardVideo = new AdvanceRewardVideo(this, Constants.TestIds.rewardAdspotId);
        //按需必填，注意：如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
        advanceRewardVideo.setCsjExpressSize(500, 500);
        //按需填写，如果在广点通后台创建的为激励视频2.0，设置值为true，否则false
        advanceRewardVideo.setGdtExpress(true);
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(this);

    }

    public void onLoad(View view) {
        advanceRewardVideo.loadStrategy();
    }


    private void showReward() {
        if (advanceRewardVideoItem != null) {
            //展示广告
            advanceRewardVideoItem.showAd();
        }
    }

    @Override
    public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
        DemoUtil.logAndToast(this, "广告加载成功");

        this.advanceRewardVideoItem = advanceRewardVideoItem;
        //广告加载成功后立即展示。也可选择提前加载，但要控制好调用顺序以及初始化操作，一次广告初始化一次，切忌复用。
        showReward();
    }

    @Override
    public void onAdShow() {
        DemoUtil.logAndToast(this, "广告展示");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "onSdkSelected = " + id);
    }

    @Override
    public void onAdClicked() {
        DemoUtil.logAndToast(this, "广告点击");
    }


    @Override
    public void onVideoCached() {
        DemoUtil.logAndToast(this, "广告缓存成功");
    }

    @Override
    public void onVideoComplete() {
        DemoUtil.logAndToast(this, "视频播放完毕");
    }

    @Override
    public void onAdClose() {
        DemoUtil.logAndToast(this, "广告关闭");
    }

    @Override
    public void onAdReward() {
        DemoUtil.logAndToast(this, "激励发放");
    }

    @Override
    public void onRewardServerInf(RewardServerCallBackInf inf) {
        //广点通和穿山甲支持回调服务端激励验证信息，详见RewardServerCallBackInf中字段信息
        DemoUtil.logAndToast(this, "onRewardServerInf");
    }

}
