package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.model.AdvanceError;
import com.advance.utils.ScreenUtil;

public class BannerActivity extends AppCompatActivity implements AdvanceBannerListener {
    private AdvanceBanner advanceBanner;
    FrameLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        rl = findViewById(R.id.banner_layout);


        advanceBanner = new AdvanceBanner(this, rl, Constants.TestIds.bannerAdspotId);
        //推荐：核心事件监听回调
        advanceBanner.setAdListener(this);
        //设置穿山甲布局尺寸，宽度全屏，高度自适应
        advanceBanner.setCsjExpressViewAcceptedSize(ScreenUtil.px2dip(this,ScreenUtil.getScreenWidth(this)),0);
        advanceBanner.loadStrategy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        advanceBanner.destroy();
    }

    @Override
    public void onDislike() {
        DemoUtil.logAndToast(this, "广告关闭");

        rl.removeAllViews();
    }

    @Override
    public void onAdShow() {
        DemoUtil.logAndToast(this, "广告展现");
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        DemoUtil.logAndToast(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
    }

    @Override
    public void onSdkSelected(String id) {
        DemoUtil.logAndToast(this, "策略选中SDK id = " + id);
    }

    @Override
    public void onAdClicked() {
        DemoUtil.logAndToast(this, "广告点击");
    }


    @Override
    public void onAdLoaded() {
        DemoUtil.logAndToast(this, "广告加载成功");
    }

}
