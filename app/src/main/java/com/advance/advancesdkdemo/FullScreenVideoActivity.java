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


        //初始化
        advanceFullScreenVideo = new AdvanceFullScreenVideo(this, Constants.Csj.fullScreenVideoAdspotId);
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
        DemoUtil.logAndToast(this, "广告加载成功");


        //广点通 onVideoCached 方法在show以后才会触发，所以在获取广告后就可以去做展示
        if (AdvanceConfig.SDK_ID_GDT.equals(advanceFullScreenItem.getSdkId())) isReady = true;
        this.advanceFullScreenItem = advanceFullScreenItem;

    }

    @Override
    public void onAdClose() {
        DemoUtil.logAndToast(this, "广告关闭");
    }

    @Override
    public void onVideoComplete() {
        DemoUtil.logAndToast(this, "视频播放结束");
    }

    @Override
    public void onVideoSkipped() {
        DemoUtil.logAndToast(this, "跳过视频");
    }

    @Override
    public void onVideoCached() {
        //穿山甲可以广告缓存成功后再去展示广告
        isReady = true;

        DemoUtil.logAndToast(this, "广告缓存成功");
    }

    @Override
    public void onAdShow() {
        DemoUtil.logAndToast(this, "广告展示");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "onSdkSelected = "+ id);
    }

    @Override
    public void onAdClicked() {
        DemoUtil.logAndToast(this, "广告点击");
    }
}
