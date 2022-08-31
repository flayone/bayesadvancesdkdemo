package com.advance.advancesdkdemo.advance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.mercury.sdk.core.config.MercuryAD;
import com.mercury.sdk.core.splash.SplashShakeClickType;


public class SplashActivity extends Activity {
    TextView skipView;
    LinearLayout logo;
    FrameLayout adContainer;
    private String TAG = "SplashActivity";
    AdvanceAD ad;
    boolean cusXiaoMi = false;
    boolean cusHuaWei = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        logo = findViewById(R.id.ll_asc_logo);

        MercuryAD.setSplashShakeClickController(SplashShakeClickType.ICON);
        //获取是否需要支持自定义SDK渠道
        cusXiaoMi = getIntent().getBooleanExtra("cusXM", false);
        cusHuaWei = getIntent().getBooleanExtra("cusHW", false);

//        AdvanceSDK.setCSJSplashButtonType(TTAdConstant.SPLASH_BUTTON_TYPE_DOWNLOAD_BAR);
        /**
         * 加载并展示开屏广告
         */
        ad = new AdvanceAD(this);
        //自定义参数传递
        ad.cusXiaoMi = cusXiaoMi;
        ad.cusHuaWei = cusHuaWei;
        //建议skipView传入null，代表使用SDK内部默认跳过按钮。如果需要自定义跳过按钮，skipView传入自定义跳过布局即可，注意：部分渠道不支持自定义，即使传了也不会生效。
        ad.loadSplash(Constants.TestIds.splashAdspotId,adContainer, null, null, new AdvanceAD.SplashCallBack() {
            @Override
            public void jumpMain() {
                goToMainActivity();
            }
        });
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


    /**
     * 开屏页禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
