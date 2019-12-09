package com.bayescom.advancesdkdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.Settings;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bayesadvance.AdvanceConfig;
import com.bayesadvance.AdvanceSplash;
import com.bayesadvance.AdvanceSplashListener;
import com.bayesadvance.model.SdkSupplier;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends Activity implements AdvanceSplashListener,WeakHandler.IHandler {
    private AdvanceSplash advanceSplash;
    private final WeakHandler mHandler = new WeakHandler(this);
    private static final int AD_TIME_OUT = 500;
    private static final int MSG_GO_MAIN = 1;
    private boolean canJump=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FrameLayout adContainer = findViewById(R.id.splash_container);
        TextView skipView = findViewById(R.id.skip_view);

        advanceSplash = new AdvanceSplash(this, Constants.mediaId, Constants.splashAdspotId, adContainer, skipView);
        //Android10 适配OAID
        advanceSplash.setOaid("");
        ImageView logoView = new ImageView(this);
        Glide.with(this).load(R.mipmap.yourlogo).into(logoView);
        //设置开屏底部logo
        advanceSplash.setBayesLogoView(logoView);
        //设置是否使用缓存策略
        advanceSplash.setUseCache(true);
        advanceSplash.setSkipText("%d s|跳过")
                .setCsjAcceptedSize(1080, 1920)//设置穿山甲广告图片偏好尺寸(如果接入穿山甲的话
                .setAdListener(this);
        //开屏位置推荐开启缓存设置
        advanceSplash.setUseCache(true);
        //设置打底sdk参数（当策略服务有问题的话，会使用 该sdk的参数)
        advanceSplash.setDefaultSdkSupplier(new SdkSupplier("12121x","1212xxxx","xxyyyxxyy",AdvanceConfig.SDK_TAG_BAYES));
        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
            advanceSplash.loadAd();
        }

    }

    @Override
    public void onAdShow() {
        Log.d("DEMO", "Splash ad show");
        Toast.makeText(this,"广告展示成功",3).show();
    }

    @Override
    public void onAdFailed() {
        Log.d("DEMO", "Splash ad failed");
        Toast.makeText(this,"广告加载失败",3).show();
        goToMainActivity();


    }

    @Override
    public void onAdClicked() {
        Log.d("DEMO", "Splash ad clicked");
        Toast.makeText(this,"广告点击",3).show();

    }

    @Override
    public void onAdClose() {
        Log.d("DEMO", "Splash ad closed");
        Toast.makeText(this,"广告关闭",3).show();

        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN,500);
    }

    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            advanceSplash.loadAd();

        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            advanceSplash.loadAd();

        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */

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

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
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
    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        this.finish();
    }
    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            next();
        }
    }
}
