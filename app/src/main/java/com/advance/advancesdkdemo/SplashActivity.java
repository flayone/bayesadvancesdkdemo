package com.advance.advancesdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.AdvanceSplash;
import com.advance.AdvanceSplashListener;
import com.advance.model.AdvanceError;


public class SplashActivity extends Activity {
    private AdvanceSplash advanceSplash;
    private boolean canJump = false;
    private String sdkId;
    TextView skipView;
    LinearLayout logo;
    FrameLayout adContainer;
    private String TAG = "SplashActivity";
    AdvanceAD ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        logo = findViewById(R.id.ll_asc_logo);


        /**
         * 加载并展示开屏广告
         */
        ad = new AdvanceAD(this);
        ad.loadSplash(adContainer, logo, skipView, new AdvanceAD.SplashCallBack() {
            @Override
            public void jumpMain() {
                goToMainActivity();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 穿山甲处理逻辑,h5类型的广告，跳转后返回不会回调onAdSkip或者onAdTimeOver，会导致无法跳转首页，需要在这里额外处理跳转逻辑
        String id = "";
        if (ad != null) {
            id = ad.sdkId;
        }
        canJump = TextUtils.equals(id, "3");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            goToMainActivity();
        }
        canJump = true;
    }


    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, SplashToMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad != null)
            ad.destroy();
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
