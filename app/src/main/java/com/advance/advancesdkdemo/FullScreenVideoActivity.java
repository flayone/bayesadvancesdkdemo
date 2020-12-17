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
import com.advance.model.AdvanceError;

public class FullScreenVideoActivity extends Activity implements AdvanceFullScreenVideoListener {

    AdvanceFullScreenVideo advanceFullScreenVideo;
    AdvanceFullScreenItem advanceFullScreenItem;
    boolean isReady = false;
    String TAG = FullScreenVideoActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_video);
        //这里是获取测试广告位id，实际请替换成自己应用的正式广告位id！
        String adspotId =ADManager.getInstance().getFullScreenVideoAdspotId();
        //初始化
        advanceFullScreenVideo = new AdvanceFullScreenVideo(this, adspotId);
        //注意：如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
        advanceFullScreenVideo.setCsjExpressSize(500, 500);
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
    public void onAdFailed(AdvanceError advanceError) {
        Log.d(TAG, "onAdFailed");
        Toast.makeText(this, "广告加载失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSdkSelected(String id) {

    }

    @Override
    public void onAdClicked() {
        Log.d(TAG, "onAdClicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();

    }
}
