package com.advance.advancesdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class BannerActivity extends AppCompatActivity {
    private AdvanceAD ad;
    FrameLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        rl = findViewById(R.id.banner_layout);

        //初始化并加载banner广告
        ad = new AdvanceAD(this);
        ad.loadBanner(rl);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁广告
        if (ad != null) {
            ad.destroy();
        }
    }

}
