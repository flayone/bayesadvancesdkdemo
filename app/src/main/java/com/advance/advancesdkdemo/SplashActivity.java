package com.advance.advancesdkdemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
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
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;
import com.mercury.sdk.core.config.LargeADCutType;
import com.mercury.sdk.core.config.MercuryAD;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity implements AdvanceSplashListener, WeakHandler.IHandler {
    private AdvanceSplash advanceSplash;
    private final WeakHandler mHandler = new WeakHandler(this);
    private static final int MSG_GO_MAIN = 1;
    private boolean canJump = false;
    private String sdkId;
    TextView skipView;
    LinearLayout logo;
    private String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_custom_logo);
        FrameLayout adContainer = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        logo = findViewById(R.id.ll_asc_logo);

        //自由选择：设置mercury素材展示模式，LargeADCutType.CUT_BOTTOM 代表对过长广告素材，保持宽度不变底部进行剪切。默认为LargeADCutType.DEFAULT 不对素材做剪切处理
        MercuryAD.setLargeADCutType(LargeADCutType.CUT_BOTTOM);

        //开屏初始化；adContainer为广告容器，skipView不需要自定义可以为null
        advanceSplash = new AdvanceSplash(this, ADManager.getInstance().getSplashAdspotId(), adContainer, skipView);
        //可选：设置mercury开屏加载时占位图
        advanceSplash.setHolderImage(ContextCompat.getDrawable(this, R.mipmap.background));
        //可选：设置mercury开屏logo资源
        advanceSplash.setLogoImage(ContextCompat.getDrawable(this, R.mipmap.logo));
        //自由选择：是否强制展示logo，默认false即大图小手机下会不展示logo
        MercuryAD.setSplashForceShowLogo(true);
        //可选，自定义强制显示的开屏logo高度，单位dp，默认-1不限制高度，跟随素材高度
        MercuryAD.setSplashForceLogoHeight(100);
        //可选，设置开屏页面的底色,默认无色透明
        MercuryAD.setSplashBackgroundColor(ContextCompat.getColor(this, R.color.adv_white));
        //可选：设置跳过字体，穿山甲广告尺寸，核心事件回调
        advanceSplash.setSkipText("跳过 %d ")
                //可选：设置穿山甲广告图片偏好尺寸(如果接入穿山甲的话
                .setCsjAcceptedSize(1080, 1920);
        //推荐：设置开屏核心回调事件的监听器。
        advanceSplash.setAdListener(this);
        //推荐：设置是否将获取到的SDK选择策略进行缓存，有助于缩短开屏广告加载时间，如果有包段包天需求建议设置为false
        advanceSplash.enableStrategyCache(true);
        //必须：设置打底sdk参数（当策略服务有问题的话，会使用 该sdk的参数)，SdkSupplier（"对应渠道平台申请的广告位id", 渠道平台id标识）
        advanceSplash.setDefaultSdkSupplier(new SdkSupplier("887301946", AdvanceSupplierID.CSJ));
        // 如果targetSDKVersion >= 23，需要申请好权限,android 10 以上可以不申请权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29) {
            checkAndRequestPermission();
        } else {
            advanceSplash.loadAd();
        }
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
        //Mercury SDK 支持内置logo，这里不再展示自定义的logo布局
        if ("1".equals(sdkId)) {
            logo.setVisibility(View.GONE);
        } else {
            logo.setVisibility(View.VISIBLE);
        }
        //强烈建议：skipView只有在广告展示出来以后才将背景色进行填充，默认加载时设置成透明状态，这样展现效果较佳
        if (skipView != null)
            skipView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.background_circle));

        Log.d(TAG, "Splash ad show");
        Toast.makeText(this, "广告展示成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdFailed() {
        goToMainActivity();

        Log.d(TAG, "Splash ad failed");
        Toast.makeText(this, "广告加载失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdClicked() {

        Log.d(TAG, "Splash ad clicked");
        Toast.makeText(this, "广告点击", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAdSkip() {
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, 100);

        Log.d(TAG, "Splash ad skip");
        Toast.makeText(this, "跳过广告", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAdTimeOver() {
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, 100);

        Log.d(TAG, "Splash ad timeOver");
        Toast.makeText(this, "倒计时结束，关闭广告", Toast.LENGTH_SHORT).show();
    }


    /**
     * ----------非常重要----------
     * <p>
     * Android6.0以上的权限适配简单示例：
     * <p>
     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
     * <p>
     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用即可。
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
            next();
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


    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            next();
        }
    }
}
