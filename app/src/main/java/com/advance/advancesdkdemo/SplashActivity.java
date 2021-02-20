package com.advance.advancesdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.AdvanceSplash;
import com.advance.AdvanceSplashListener;
import com.advance.model.AdvanceError;


public class SplashActivity extends Activity implements AdvanceSplashListener {
    private AdvanceSplash advanceSplash;
    private boolean canJump = false;
    private String sdkId;
    TextView skipView;
    LinearLayout logo;
    FrameLayout adContainer;
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        adContainer = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        logo = findViewById(R.id.ll_asc_logo);


        //这里是获取测试广告位id，实际请替换成自己应用的正式广告位id！
        String adspotId = ADManager.getInstance().getSplashAdspotId();
        //开屏初始化；adspotId代表广告位id，adContainer为广告容器，skipView不需要自定义可以为null
        advanceSplash = new AdvanceSplash(this, adspotId, adContainer, skipView);
        //必须：设置开屏核心回调事件的监听器。
        advanceSplash.setAdListener(this);
        //必须：请求广告
        advanceSplash.loadStrategy();
    }

    /**
     * @param id 代表当前被选中的策略id，值为"1" 代表mercury策略 ，值为"2" 代表广点通策略， 值为"3" 代表穿山甲策略
     */
    @Override
    public void onSdkSelected(String id) {
        //给sdkId赋值用来判断被策略选中的是哪个SDK
        sdkId = id;

        Log.d(TAG, "Splash ad onSdkSelected " + id);
        Toast.makeText(this, "策略选中，选中SDK id = " + id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdLoaded() {
        //穿山甲广告加载成功到展现时间很快，所以最好在这里进行logo布局的展示
        if ("3".equals(sdkId)) {
            logo.setVisibility(View.VISIBLE);
        } else {
            logo.setVisibility(View.GONE);
        }

        Log.d(TAG, "Splash ad onAdLoaded");
        Toast.makeText(this, "广告加载成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdShow() {
        //logo展示建议：广告展示的时候再展示logo，其他时刻都是展示的全屏的background图片
        adContainer.setBackgroundColor(Color.WHITE);
        logo.setVisibility(View.VISIBLE);

        //强烈建议：skipView只有在广告展示出来以后才将背景色进行填充，默认加载时设置成透明状态，这样展现效果较佳
        if (skipView != null)
            skipView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_circle));

        Log.d(TAG, "Splash ad show");
        Toast.makeText(this, "广告展示成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailed(AdvanceError advanceError) {
        goToMainActivity();

        Log.d(TAG, "Splash ad failed");
        Toast.makeText(this, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {

        Log.d(TAG, "Splash ad clicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAdSkip() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 100);

        Log.d(TAG, "Splash ad skip");
        Toast.makeText(this, "跳过广告", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdTimeOver() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, 100);

        Log.d(TAG, "Splash ad timeOver");
        Toast.makeText(this, "倒计时结束，关闭广告", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onPause() {
        super.onPause();
        // 穿山甲处理逻辑,h5类型的广告，跳转后返回不会回调onAdSkip或者onAdTimeOver，会导致无法跳转首页，需要在这里额外处理跳转逻辑
        canJump = TextUtils.equals(sdkId, "3");
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
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private void next() {
        if (canJump) {
            goToMainActivity();
        } else {
            canJump = true;
        }
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
