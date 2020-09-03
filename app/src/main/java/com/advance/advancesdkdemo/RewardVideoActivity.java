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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_video);
        advanceRewardVideo = new AdvanceRewardVideo(this, ADManager.getInstance().getRewardAdspotId());
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(this);

    }

    public void onLoad(View view) {
        advanceRewardVideo.loadStrategy();

    }



    @Override
    public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
        Log.d("DEMO", "LOADED");
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
        //设置穿山甲的回调（必须）
        if (advanceRewardVideoItem.getSdkId().equals(AdvanceConfig.SDK_ID_CSJ)){
            CsjRewardVideoAdItem csjRewardVideoAdItem =  (CsjRewardVideoAdItem) advanceRewardVideoItem;
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
        }
        //展示广告
        advanceRewardVideoItem.showAd();

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
