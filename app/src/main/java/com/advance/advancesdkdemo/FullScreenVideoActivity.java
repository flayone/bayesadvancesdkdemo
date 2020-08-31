package com.advance.advancesdkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.advance.AdvanceConfig;
import com.advance.AdvanceFullScreenItem;
import com.advance.AdvanceFullScreenVideo;
import com.advance.AdvanceFullScreenVideoListener;
import com.advance.csj.CsjFullScreenVideoItem;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;

public class FullScreenVideoActivity extends Activity implements AdvanceFullScreenVideoListener {

    AdvanceFullScreenVideo advanceFullScreenVideo;
    AdvanceFullScreenItem advanceFullScreenItem;
    boolean isReady = false;
    String TAG = FullScreenVideoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);

        advanceFullScreenVideo = new AdvanceFullScreenVideo(this, ADManager.getInstance().getFullScreenVideoAdspotId());
        //推荐：核心事件监听回调
        advanceFullScreenVideo.setAdListener(this);

    }

    public void loadFull(View view) {
        isReady = false;
        advanceFullScreenVideo.loadStrategy();
    }

    public void showFull(View view) {
        if (isReady && advanceFullScreenItem != null) {
            advanceFullScreenItem.showAd();
        }
    }

    @Override
    public void onAdLoaded(AdvanceFullScreenItem advanceFullScreenItem) {
        Log.d(TAG, "onAdLoaded");

        //广点通 onVideoCached 方法在show以后才会触发，所以在获取广告后就可以去做展示
        if (AdvanceConfig.SDK_ID_GDT.equals(advanceFullScreenItem.getSdkId())) isReady = true;
        this.advanceFullScreenItem = advanceFullScreenItem;
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClose() {
        Log.d(TAG, "onAdClose");
        Toast.makeText(this, "广告关闭", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoComplete() {
        Log.d(TAG, "onVideoComplete");
        Toast.makeText(this, "视频播放结束", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoSkipped() {
        Log.d(TAG, "onVideoSkipped");
        Toast.makeText(this, "跳过视频", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVideoCached() {
        //穿山甲可以广告缓存成功后再去展示广告
        isReady = true;

        Log.d(TAG, "onVideoCached");
        Toast.makeText(this, "广告缓存成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdShow() {
        Log.d(TAG, "onAdShow");
        Toast.makeText(this, "广告展示", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdFailed() {
        Log.d(TAG, "onAdFailed");
        Toast.makeText(this, "广告加载失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAdClicked() {
        Log.d(TAG, "onAdClicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }
}
