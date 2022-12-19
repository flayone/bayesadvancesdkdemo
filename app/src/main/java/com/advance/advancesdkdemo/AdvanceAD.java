package com.advance.advancesdkdemo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.advance.advancesdkdemo.custom.HuaWeiSplashAdapter;
import com.advance.advancesdkdemo.custom.XiaoMiSplashAdapter;
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
    AdvanceBaseAdspot baseAD;
    Activity mActivity;
    String sdkId;

    //小米渠道是否需要添加为自定义渠道
    public boolean cusXiaoMi = false;
    //华为渠道是否需要添加为自定义渠道
    public boolean cusHuaWei = false;

    /**
     * 初始化广告处理类
     *
     * @param activity 页面上下文
     */
    public AdvanceAD(Activity activity) {
        mActivity = activity;
    }


    /**
     * 添加自定义渠道，注意一定要在广告初始化以后再调用！
     *
     * @param sdkID   SDK渠道id。具体值需联系运营获取对应接入渠道的id。
     * @param adapter 继承与基类adapter的自定义adapter
     */
    public void addCustomAdapter(String sdkID, AdvanceBaseCustomAdapter adapter) {
        if (baseAD != null) {
            baseAD.addCustomSupplier(sdkID, adapter);
        }
    }

    /**
     * 初始化advance sdk
     *
     * @param context 上下文内容，一般是传入application的context
     */
    public static void initAD(Context context) {
        //可选：根据自身需求控制隐私项
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

        //必要配置：初始化聚合SDK，三个参数依次为context上下文，appId媒体id，isDebug调试模式开关
        AdvanceSDK.initSDK(context, Constants.APP_ID, BuildConfig.DEBUG);
        //推荐配置：允许Mercury预缓存素材
        MercuryAD.needPreLoadMaterial(true);
        //接入tanx配置项，当glide不兼容时必填
        AdvanceSDK.setTanxImgLoader(new MyImageLoader());
    }

    /**
     * 加载开屏广告
     *
     * @param adContainer 广告承载布局，不可为空
     * @param callBack    跳转回调，在回调中进行跳转主页或其他操作
     */
    public void loadSplash(String id, final ViewGroup adContainer, final SplashCallBack callBack) {
        //开屏初始化；adspotId代表广告位id，adContainer为广告容器，skipView不需要自定义可以为null
        final AdvanceSplash advanceSplash = new AdvanceSplash(mActivity, id, adContainer, null);
        baseAD = advanceSplash;
        //注意！！：如果开屏页是fragment或者dialog实现，这里需要置为true。不设置时默认值为false，代表开屏和首页为两个不同的activity
//        advanceSplash.setShowInSingleActivity(true);
//        建议：设置底部logo布局及高度值（单位px）
        advanceSplash.setLogoLayout(R.layout.splash_logo_layout, mActivity.getResources().getDimensionPixelSize(R.dimen.logo_layout_height));
        //必须：设置开屏核心回调事件的监听器。
        advanceSplash.setAdListener(new AdvanceSplashListener() {
            /**
             * @param id 代表当前被选中的策略id，值为"1" 代表mercury策略 ，值为"2" 代表广点通策略， 值为"3" 代表穿山甲策略
             */
            @Override
            public void onSdkSelected(String id) {
                //给sdkId赋值用来判断被策略选中的是哪个SDK
                sdkId = id;

            }

            @Override
            public void onAdLoaded() {

                logAndToast(mActivity, "广告加载成功");
            }

            @Override
            public void jumpToMain() {
                if (callBack != null)
                    callBack.jumpMain();
            }

            @Override
            public void onAdShow() {


                logAndToast(mActivity, "广告展示成功");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }


            @Override
            public void onAdSkip() {
                logAndToast(mActivity, "跳过广告");
            }

            @Override
            public void onAdTimeOver() {
                logAndToast(mActivity, "倒计时结束，关闭广告");
            }
        });
        if (cusXiaoMi) {
            //此处自定义的渠道id值，需要联系我们获取。
            advanceSplash.addCustomSupplier("小米SDK渠道id", XiaoMiSplashAdapter.class.getName());
        }
        if (cusHuaWei) {
            advanceSplash.addCustomSupplier("华为SDK渠道id", HuaWeiSplashAdapter.class.getName());
        }
        //必须：请求广告
        advanceSplash.loadStrategy();
    }


    /**
     * 开屏跳转回调
     */
    public interface SplashCallBack {
        void jumpMain();
    }

    /**
     * 加载并展示banner广告
     *
     * @param adContainer banner广告的承载布局
     */
    public void loadBanner(String id, final ViewGroup adContainer) {
        AdvanceBanner advanceBanner = new AdvanceBanner(mActivity, adContainer, id);
        baseAD = advanceBanner;
        //如果集成穿山甲，这里必须配置，具体尺寸要和穿山甲后台中的"代码位尺寸"宽高比例一致，值单位为dp，这里示例使用的广告位宽高比为640：100
        int adWidth = ScreenUtil.px2dip(mActivity, ScreenUtil.getScreenWidth(mActivity));
        int adHeight = (int) (((double) adWidth / (double) 640) * 100);
        //设置穿山甲布局尺寸，宽度全屏，高度传入0代表自适应；也可填入具体dp值，尺寸要和穿山甲后台中的"代码位尺寸"宽高比例一致，值单位为dp。
        advanceBanner.setCsjExpressViewAcceptedSize(adWidth, adHeight);
        //推荐：核心事件监听回调
        advanceBanner.setAdListener(new AdvanceBannerListener() {
            @Override
            public void onDislike() {
                logAndToast(mActivity, "广告关闭");

                adContainer.removeAllViews();
            }

            @Override
            public void onAdShow() {
                logAndToast(mActivity, "广告展现");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {
            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }


            @Override
            public void onAdLoaded() {
                logAndToast(mActivity, "广告加载成功");
            }

        });
        advanceBanner.loadStrategy();
    }


    /**
     * 加载并展示插屏广告。
     * 也可以选择性先提前加载，然后在合适的时机再调用展示方法
     */
    public void loadInterstitial(String id) {
        //初始化
        final AdvanceInterstitial advanceInterstitial = new AdvanceInterstitial(mActivity, id);
        baseAD = advanceInterstitial;
        //注意：穿山甲是否为"新插屏广告"，默认为true
//        advanceInterstitial.setCsjNew(false);
        //推荐：核心事件监听回调
        advanceInterstitial.setAdListener(new AdvanceInterstitialListener() {

            @Override
            public void onAdReady() {
                logAndToast(mActivity, "广告就绪");

                // 大多数情况下可以直接展示
                // 如果有业务需求，可以提前加载广告，保存广告对象，需要时再调用show
                if (baseAD != null) {
                    baseAD.show();
                }
            }

            @Override
            public void onAdClose() {
                logAndToast(mActivity, "广告关闭");
            }


            @Override
            public void onAdShow() {
                logAndToast(mActivity, "广告展示");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {

            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }
        });
        advanceInterstitial.loadStrategy();

    }

    /**
     * 加载并展示激励视频广告。
     * 也可以选择性先提前加载，然后在合适的时机再调用展示方法
     */
    public void loadReward(String id) {
        //初始化，注意需要时再初始化，不要复用。
        final AdvanceRewardVideo advanceRewardVideo = new AdvanceRewardVideo(mActivity, id);
        baseAD = advanceRewardVideo;
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(new AdvanceRewardVideoListener() {
            @Override
            public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
                logAndToast(mActivity, "广告加载成功");

                // 如果有业务需求，可以提前加载广告，在需要的时候调用show进行展示
                // 为了方便理解，这里在收到广告后直接调用广告展示，有可能会出现一段时间的缓冲状态。
                if (baseAD != null) {
                    //展示广告
                    baseAD.show();
                }
            }


            @Override
            public void onAdShow() {
                logAndToast(mActivity, "广告展示");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {

            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }


            @Override
            public void onVideoCached() {
                logAndToast(mActivity, "广告缓存成功");
            }

            @Override
            public void onVideoComplete() {
                logAndToast(mActivity, "视频播放完毕");
            }

            @Override
            public void onVideoSkip() {

            }

            @Override
            public void onAdClose() {
                logAndToast(mActivity, "广告关闭");
            }

            @Override
            public void onAdReward() {
                logAndToast(mActivity, "激励发放");
            }

            @Override
            public void onRewardServerInf(RewardServerCallBackInf inf) {
                //广点通和穿山甲支持回调服务端激励验证信息，详见RewardServerCallBackInf中字段信息
                logAndToast(mActivity, "onRewardServerInf" + inf);
            }
        });
        advanceRewardVideo.loadStrategy();
    }

    /**
     * 加载并展示全屏视频广告。
     * 也可以选择先提前加载，然后在合适的时机再调用展示方法
     */
    public void loadFullVideo(String id) {
        //初始化
        AdvanceFullScreenVideo advanceFullScreenVideo = new AdvanceFullScreenVideo(mActivity, id);
        baseAD = advanceFullScreenVideo;
        //推荐：核心事件监听回调
        advanceFullScreenVideo.setAdListener(new AdvanceFullScreenVideoListener() {
            @Override
            public void onAdLoaded(AdvanceFullScreenItem advanceFullScreenItem) {
                logAndToast(mActivity, "广告加载成功");

                // 如果有业务需求，可以提前加载广告，在需要的时候调用show进行展示
                // 为了方便理解，这里在收到广告后直接调用广告展示，有可能会出现一段时间的缓冲状态。
                if (baseAD != null)
                    baseAD.show();
            }

            @Override
            public void onAdClose() {
                logAndToast(mActivity, "广告关闭");
            }

            @Override
            public void onVideoComplete() {
                logAndToast(mActivity, "视频播放结束");
            }

            @Override
            public void onVideoSkipped() {
                logAndToast(mActivity, "跳过视频");
            }

            @Override
            public void onVideoCached() {
                //广告缓存成功，可以在此记录状态，但要注意：不一定所有的广告会返回该回调
                logAndToast(mActivity, "广告缓存成功");
            }

            @Override
            public void onAdShow() {
                logAndToast(mActivity, "广告展示");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {

            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }
        });
        advanceFullScreenVideo.loadStrategy();
    }

    boolean hasNativeShow = false;
    boolean isNativeLoading = false;

    /**
     * 加载并展示原生模板信息流广告
     *
     * @param adContainer 广告的承载布局
     */
    public void loadNativeExpressAndShow(final ViewGroup adContainer) {
        if (hasNativeShow) {
            LogUtil.d("loadNativeExpress hasNativeShow");
            return;
        }
        if (isNativeLoading) {
            LogUtil.d("loadNativeExpress isNativeLoading");
            return;
        }
        isNativeLoading = true;

        if (adContainer.getChildCount() > 0) {
            adContainer.removeAllViews();
        }

        //初始化
        final AdvanceNativeExpress advanceNativeExpress = new AdvanceNativeExpress(mActivity, Constants.TestIds.nativeExpressAdspotId);
        baseAD = advanceNativeExpress;
        //必须：设置广告父布局
        advanceNativeExpress.setAdContainer(adContainer);
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(new AdvanceNativeExpressListener() {
            @Override
            public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {
                if (baseAD != null) {
                    baseAD.show();
                }
            }

            @Override
            public void onAdRenderSuccess(View view) {
                logAndToast(mActivity, "广告渲染成功");
            }


            @Override
            public void onAdClose(View view) {
                logAndToast(mActivity, "广告关闭");
            }

            @Override
            public void onAdShow(View view) {
                hasNativeShow = true;
                isNativeLoading = false;
                logAndToast(mActivity, "广告展示");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                isNativeLoading = false;
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {

            }

            @Override
            public void onAdRenderFailed(View view) {
                logAndToast(mActivity, "广告渲染失败");
            }

            @Override
            public void onAdClicked(View view) {
                logAndToast(mActivity, "广告点击");
            }

        });
        //必须
        advanceNativeExpress.loadStrategy();
    }


    AdvanceNativeExpress advanceNativeExpress;
    //分步加载的信息流判断信息
    boolean hasSplitNativeShow = false;
    boolean isSplitNativeLoading = false;

    /**
     * 仅加载信息流广告，分步加载信息流方法
     *
     * @param id
     */
    public void loadNativeExpressOnly(String id, LoadCallBack callBack) {
        if (hasSplitNativeShow) {
            LogUtil.d("loadNativeExpress hasSplitNativeShow");
            return;
        }

        if (isSplitNativeLoading) {
            LogUtil.d("loadNativeExpress isSplitNativeLoading");
            return;
        }
        isSplitNativeLoading = true;

        //初始化
        advanceNativeExpress = new AdvanceNativeExpress(mActivity, id);
        baseAD = advanceNativeExpress;
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(new AdvanceNativeExpressListener() {
            @Override
            public void onAdLoaded(java.util.List<AdvanceNativeExpressAdItem> list) {
                logAndToast(mActivity, "广告加载成功");
                if (callBack != null) {
                    callBack.adSuccess();
                }
            }

            @Override
            public void onAdRenderSuccess(android.view.View view) {
                logAndToast(mActivity, "广告渲染成功");
            }


            @Override
            public void onAdClose(android.view.View view) {
                logAndToast(mActivity, "广告关闭");
            }

            @Override
            public void onAdShow(android.view.View view) {
                hasSplitNativeShow = true;
                isSplitNativeLoading = false;
                logAndToast(mActivity, "广告展示");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                isSplitNativeLoading = false;
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
            }

            @Override
            public void onSdkSelected(String id) {

            }

            @Override
            public void onAdRenderFailed(android.view.View view) {
                logAndToast(mActivity, "广告渲染失败");
            }

            @Override
            public void onAdClicked(android.view.View view) {
                logAndToast(mActivity, "广告点击");
            }

        });
        //必须
        advanceNativeExpress.loadStrategy();

    }

    public void showNativeExpress(ViewGroup adContainer) {
        //必须 ，设置广告展示用布局页面
        advanceNativeExpress.setAdContainer(adContainer);
        //必须，展示广告
        advanceNativeExpress.show();

    }

    public AdvanceDraw advanceDraw;

    /**
     * 加载draw信息流广告
     *
     * @param id
     * @param adContainer
     */
    public void loadDraw(String id, ViewGroup adContainer) {
        //初始化
        advanceDraw = new AdvanceDraw(mActivity, id);
        baseAD = advanceDraw;
        //必须：设置广告承载布局
        advanceDraw.setAdContainer(adContainer);
        //推荐：监听核心事件
        advanceDraw.setAdListener(new AdvanceDrawListener() {
            @Override
            public void onAdLoaded() {
                if (advanceDraw != null) {
                    advanceDraw.show();
                }
            }

            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
            }

            @Override
            public void onSdkSelected(String id) {

            }
        });
        advanceDraw.loadStrategy();
    }

    /**
     * 广告加载回调
     */
    public interface LoadCallBack {
        void adSuccess();
    }


    /**
     * 销毁广告
     */
    public void destroy() {
        if (baseAD != null) {
            baseAD.destroy();
            baseAD = null;
        }
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
