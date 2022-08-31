package com.advance.advancesdkdemo.advance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;

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
        ad.loadBanner(Constants.TestIds.bannerAdspotId,rl);
    }


}
