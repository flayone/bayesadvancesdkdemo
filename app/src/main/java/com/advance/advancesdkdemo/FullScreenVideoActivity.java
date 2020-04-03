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

        advanceFullScreenVideo = new AdvanceFullScreenVideo(this, ADManager.getInstance().getMediaId(), ADManager.getInstance().getFullScreenVideoAdspotId());
        advanceFullScreenVideo.setAdListener(this);
        advanceFullScreenVideo.setDefaultSdkSupplier(new SdkSupplier("5051624", "945065337", null, AdvanceConfig.SDK_TAG_CSJ));
//        设置广点通视频播放策略
        advanceFullScreenVideo.setGdtVideoOption(new VideoOption.Builder().setAutoPlayMuted(false)
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.NEVER)
                .build());
//        设置广点通媒体状态监听
        advanceFullScreenVideo.setGdtMediaListener(new UnifiedInterstitialMediaListener() {
            @Override
            public void onVideoInit() {
                Log.d(TAG, "onVideoInit");

            }

            @Override
            public void onVideoLoading() {
                Log.d(TAG, "onVideoLoading");

            }

            @Override
            public void onVideoReady(long l) {
                Log.d(TAG, "onVideoReady ,video duration = " + l);

            }

            @Override
            public void onVideoStart() {
                Log.d(TAG, "onVideoStart");

            }

            @Override
            public void onVideoPause() {
                Log.d(TAG, "onVideoPause");

            }

            @Override
            public void onVideoComplete() {
                Log.d(TAG, "MediaListener onVideoComplete");

            }

            @Override
            public void onVideoError(AdError adError) {
                Log.d(TAG, "onVideoError code:" + adError.getErrorCode() + " msg:" + adError.getErrorMsg());

            }

            @Override
            public void onVideoPageOpen() {
                Log.d(TAG, "onVideoPageOpen");

            }

            @Override
            public void onVideoPageClose() {
                Log.d(TAG, "onVideoPageClose");

            }
        });
    }

    public void loadFull(View view) {
        isReady = false;
        advanceFullScreenVideo.loadAd();
    }

    public void showFull(View view) {
        if (isReady && advanceFullScreenItem != null) {
            if (AdvanceConfig.SDK_TAG_CSJ.equals(advanceFullScreenItem.getSdkTag())) {
                CsjFullScreenVideoItem csj = (CsjFullScreenVideoItem) advanceFullScreenItem;
                csj.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        Log.d(TAG, "onIdle");

                    }

                    @Override
                    public void onDownloadActive(long l, long l1, String s, String s1) {
                        Log.d(TAG, "onDownloadActive");

                    }

                    @Override
                    public void onDownloadPaused(long l, long l1, String s, String s1) {
                        Log.d(TAG, "onDownloadPaused");

                    }

                    @Override
                    public void onDownloadFailed(long l, long l1, String s, String s1) {
                        Log.d(TAG, "onDownloadFailed");

                    }

                    @Override
                    public void onDownloadFinished(long l, String s, String s1) {
                        Log.d(TAG, "onDownloadFinished");

                    }

                    @Override
                    public void onInstalled(String s, String s1) {
                        Log.d(TAG, "onInstalled");

                    }
                });
            }
            advanceFullScreenItem.showAd();
        }
    }

    @Override
    public void onAdLoaded(AdvanceFullScreenItem advanceFullScreenItem) {
        Log.d(TAG, "onAdLoaded");

        //广点通 onVideoCached 方法不触发，所以在获取广告后就可以去做展示
        if (AdvanceConfig.SDK_TAG_GDT.equals(advanceFullScreenItem.getSdkTag())) isReady = true;
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
