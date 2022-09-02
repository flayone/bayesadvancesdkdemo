package com.advance.advancesdkdemo.advance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.AdvanceBaseAdspot;
import com.advance.AdvanceDraw;
import com.advance.AdvanceDrawListener;
import com.advance.AdvanceFullScreenItem;
import com.advance.AdvanceFullScreenVideo;
import com.advance.AdvanceFullScreenVideoListener;
import com.advance.AdvanceInterstitial;
import com.advance.AdvanceInterstitialListener;
import com.advance.AdvanceNativeExpress;
import com.advance.AdvanceNativeExpressAdItem;
import com.advance.AdvanceNativeExpressListener;
import com.advance.AdvanceRewardVideo;
import com.advance.AdvanceRewardVideoItem;
import com.advance.AdvanceRewardVideoListener;
import com.advance.AdvanceSDK;
import com.advance.AdvanceSplash;
import com.advance.AdvanceSplashListener;
import com.advance.RewardServerCallBackInf;
import com.advance.advancesdkdemo.BuildConfig;
import com.advance.advancesdkdemo.Constants;
import com.advance.advancesdkdemo.R;
import com.advance.custom.AdvanceBaseCustomAdapter;
import com.advance.itf.AdvancePrivacyController;
import com.advance.model.AdvanceError;
import com.advance.utils.LogUtil;
import com.advance.utils.ScreenUtil;
import com.mercury.sdk.core.config.MercuryAD;

import java.util.List;

/**
 * Advance SDK广告加载逻辑统一处理类
 */
public class AdvanceAD {
    Activity mActivity;

    /**
     * 初始化广告处理类
     *
     * @param activity 页面上下文
     */
    public AdvanceAD(Activity activity) {
        mActivity = activity;
    }

    /**
     * 初始化advance sdk
     *
     * @param context 上下文内容，一般是传入application的context
     */
    public static void initAD(Context context) {
        //必要配置：初始化聚合SDK，三个参数依次为context上下文，appId媒体id，isDebug调试模式开关
        AdvanceSDK.initSDK(context, Constants.APP_ID, BuildConfig.DEBUG);
        //推荐配置：允许Mercury预缓存素材
        MercuryAD.needPreLoadMaterial(true);
        //tanx配置优化项，当glide不兼容时必填
        AdvanceSDK.setTanxImgLoader(new MyImageLoader());
        //可选：根据自身需求控制隐私项，设置为false时，SDK将不采集相应信息
        AdvanceSDK.setPrivacyController(new AdvancePrivacyController() {
            @Override
            public boolean isCanUseLocation() {
                return super.isCanUseLocation();
            }

            @Override
            public Location getLocation() {
                return super.getLocation();
            }

            @Override
            public boolean isCanUsePhoneState() {
                return super.isCanUsePhoneState();
            }

            @Override
            public String getDevImei() {
                return super.getDevImei();
            }

            @Override
            public String[] getImeis() {
                return super.getImeis();
            }

            @Override
            public String getDevAndroidID() {
                return super.getDevAndroidID();
            }

            @Override
            public String getDevMac() {
                return super.getDevMac();
            }

            @Override
            public boolean isCanUseWriteExternal() {
                return super.isCanUseWriteExternal();
            }

            @Override
            public boolean isCanUseWifiState() {
                return super.isCanUseWifiState();
            }

            @Override
            public boolean canUseOaid() {
                return super.canUseOaid();
            }

            @Override
            public boolean canUseMacAddress() {
                return super.canUseMacAddress();
            }

            @Override
            public boolean canUseNetworkState() {
                return super.canUseNetworkState();
            }

            @Override
            public String getDevOaid() {
                return super.getDevOaid();
            }

            @Override
            public boolean alist() {
                return super.alist();
            }

            @Override
            public List<String> getInstalledPackages() {
                return super.getInstalledPackages();
            }
        });

    }

    /**
     * 统一处理打印日志，并且toast提示。
     *
     * @param context 上下文
     * @param msg     需要显示的内容
     */
    public static void logAndToast(Context context, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d("[AdvanceAD][logAndToast]", msg);
            //如果不想弹出toast可以在此注释掉下面代码
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
