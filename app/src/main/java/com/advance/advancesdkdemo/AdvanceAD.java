package com.advance.advancesdkdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.advance.AdvanceBanner;
import com.advance.AdvanceBannerListener;
import com.advance.AdvanceBaseAdspot;
import com.advance.AdvanceConfig;
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
import com.advance.model.AdvanceError;
import com.advance.supplier.baidu.AdvanceBDManager;
import com.advance.supplier.csj.CsjNativeExpressAdItem;
import com.advance.utils.LogUtil;
import com.advance.utils.ScreenUtil;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.mercury.sdk.core.config.MercuryAD;

import java.util.List;

/**
 * Advance SDK广告加载逻辑统一处理类
 */
public class AdvanceAD {
    AdvanceBaseAdspot baseAD;
    Activity mActivity;

    String sdkId;
    boolean canJump = false;

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
    }

    /**
     * 加载开屏广告
     *
     * @param adContainer   广告承载布局，不可为空
     * @param logoContainer 底部logo布局，可以为空
     * @param skipView      跳过按钮，可以为空
     * @param callBack      跳转回调，在回调中进行跳转主页或其他操作
     */
    public void loadSplash(final ViewGroup adContainer, final ViewGroup logoContainer, final TextView skipView, final SplashCallBack callBack) {
        //开屏初始化；adspotId代表广告位id，adContainer为广告容器，skipView不需要自定义可以为null
        final AdvanceSplash advanceSplash = new AdvanceSplash(mActivity, Constants.TestIds.splashAdspotId, adContainer, skipView);
        baseAD = advanceSplash;
        //必须：设置开屏核心回调事件的监听器。
        advanceSplash.setAdListener(new AdvanceSplashListener() {
            /**
             * @param id 代表当前被选中的策略id，值为"1" 代表mercury策略 ，值为"2" 代表广点通策略， 值为"3" 代表穿山甲策略
             */
            @Override
            public void onSdkSelected(String id) {
                //给sdkId赋值用来判断被策略选中的是哪个SDK
                sdkId = id;

                logAndToast(mActivity, "策略选中SDK id = " + id);
            }

            @Override
            public void onAdLoaded() {
                if (logoContainer != null) {
                    //穿山甲广告加载成功到展现时间很快，所以最好在这里进行logo布局的展示
                    if ("3".equals(sdkId)) {
                        logoContainer.setVisibility(View.VISIBLE);
                    } else {
                        logoContainer.setVisibility(View.GONE);
                    }
                }

                logAndToast(mActivity, "广告加载成功");
            }

            @Override
            public void onAdShow() {
                canJump = true;
                //logo展示建议：广告展示的时候再展示logo，其他时刻都是展示的全屏的background图片
                if (adContainer != null)
                    adContainer.setBackgroundColor(Color.WHITE);
                if (logoContainer != null)
                    logoContainer.setVisibility(View.VISIBLE);

                //强烈建议：skipView只有在广告展示出来以后才将背景色进行填充，默认加载时设置成透明状态，这样展现效果较佳
                if (skipView != null)
                    skipView.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.background_circle));

                logAndToast(mActivity, "广告展示成功");
            }

            @Override
            public void onAdFailed(AdvanceError advanceError) {
                logAndToast(mActivity, "广告加载失败 code=" + advanceError.code + " msg=" + advanceError.msg);
                canJump = true;
                if (callBack != null)
                    callBack.jumpMain();
            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }


            @Override
            public void onAdSkip() {
                logAndToast(mActivity, "跳过广告");
                splashNext(callBack);

            }

            @Override
            public void onAdTimeOver() {
                logAndToast(mActivity, "倒计时结束，关闭广告");
                splashNext(callBack);

            }
        });
        //必须：请求广告
        advanceSplash.loadStrategy();
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是下载类广告时只会下载App。
     */
    private void splashNext(final SplashCallBack callBack) {
        //建议延迟100ms执行跳转首页，主要是部分机型上反应比较慢，广点通广告时可能还没有吊起落地页，但回调了广告关闭方法，此时会发生先跳首页再打开广告落地页的异常情况，所以需要进行100ms的延迟。
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canJump) {
                    if (callBack != null) {
                        callBack.jumpMain();
                    }
                } else {
                    canJump = true;
                }
            }
        }, 100);
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
    public void loadBanner(final ViewGroup adContainer) {
        AdvanceBanner advanceBanner = new AdvanceBanner(mActivity, adContainer, Constants.TestIds.bannerAdspotId);
        baseAD = advanceBanner;
        //设置穿山甲布局尺寸，宽度全屏，高度自适应
        advanceBanner.setCsjExpressViewAcceptedSize(ScreenUtil.px2dip(mActivity, ScreenUtil.getScreenWidth(mActivity)), 0);
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
                logAndToast(mActivity, "策略选中SDK id = " + id);
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
    public void loadInterstitial() {
        //初始化
        final AdvanceInterstitial advanceInterstitial = new AdvanceInterstitial(mActivity, Constants.TestIds.interstitialAdspotId);
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
                if (advanceInterstitial != null) {
                    advanceInterstitial.show();
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
                logAndToast(mActivity, "onSdkSelected = " + id);
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
    public void loadReward() {
        //初始化，注意需要时再初始化，不要复用。
        AdvanceRewardVideo advanceRewardVideo = new AdvanceRewardVideo(mActivity, Constants.TestIds.rewardAdspotId);
        baseAD = advanceRewardVideo;
        //按需必填，注意：如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
        advanceRewardVideo.setCsjExpressSize(500, 500);
        //按需填写，如果在广点通后台创建的为激励视频2.0，设置值为true，否则false
        advanceRewardVideo.setGdtExpress(true);
        //设置通用事件监听器
        advanceRewardVideo.setAdListener(new AdvanceRewardVideoListener() {
            @Override
            public void onAdLoaded(AdvanceRewardVideoItem advanceRewardVideoItem) {
                logAndToast(mActivity, "广告加载成功");

                // 如果有业务需求，可以提前加载广告，保存这里的advanceRewardVideoItem 对象，在需要的时候调用show进行展示
                // 为了方便理解，这里在收到广告后直接调用广告展示，有可能会出现一段时间的缓冲状态。
                if (advanceRewardVideoItem != null) {
                    //展示广告
                    advanceRewardVideoItem.showAd();
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
                logAndToast(mActivity, "onSdkSelected = " + id);
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
     * 也可以选择性先提前加载，然后在合适的时机再调用展示方法
     */
    public void loadFullVideo() {
        //初始化
        AdvanceFullScreenVideo advanceFullScreenVideo = new AdvanceFullScreenVideo(mActivity, Constants.TestIds.fullScreenVideoAdspotId);
        baseAD = advanceFullScreenVideo;
        //注意：如果穿山甲版本号大于3.2.5.1，模板广告需要设置期望个性化模板广告的大小,单位dp,全屏视频场景，只要设置的值大于0即可
        advanceFullScreenVideo.setCsjExpressSize(500, 500);
        //推荐：核心事件监听回调
        advanceFullScreenVideo.setAdListener(new AdvanceFullScreenVideoListener() {
            @Override
            public void onAdLoaded(AdvanceFullScreenItem advanceFullScreenItem) {
                logAndToast(mActivity, "广告加载成功");

                // 如果有业务需求，可以提前加载广告，保存这里的advanceFullScreenItem 对象，在需要的时候调用show进行展示
                // 为了方便理解，这里在收到广告后直接调用广告展示，有可能会出现一段时间的缓冲状态。
                if (advanceFullScreenItem != null)
                    advanceFullScreenItem.showAd();
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
                logAndToast(mActivity, "onSdkSelected = " + id);
            }

            @Override
            public void onAdClicked() {
                logAndToast(mActivity, "广告点击");
            }
        });
        advanceFullScreenVideo.loadStrategy();
    }

    boolean isGdtExpress2 = false;
    AdvanceNativeExpressAdItem advanceNativeExpressAdItem;
    boolean hasNativeShow = false;
    boolean isNativeLoading = false;

    /**
     * 加载并展示原生模板信息流广告
     *
     * @param adContainer 广告的承载布局
     */
    public void loadNativeExpress(final ViewGroup adContainer) {
        if (hasNativeShow) {
            LogUtil.d("loadNativeExpress hasNativeShow");
            return;
        }
        if (advanceNativeExpressAdItem != null) {
            if (adContainer.getChildCount() > 0 && adContainer.getChildAt(0) == advanceNativeExpressAdItem.getExpressAdView()) {
                return;
            }
        }
        if (isNativeLoading) {
            LogUtil.d("loadNativeExpress isNativeLoading");
            return;
        }
        isNativeLoading = true;

        if (adContainer.getChildCount() > 0) {
            adContainer.removeAllViews();
        }

        AdvanceBDManager.getInstance().nativeExpressContainer = adContainer;

        //初始化
        final AdvanceNativeExpress advanceNativeExpress = new AdvanceNativeExpress(mActivity, Constants.TestIds.nativeExpressAdspotId);
        baseAD = advanceNativeExpress;
        //推荐：核心事件监听回调
        advanceNativeExpress.setAdListener(new AdvanceNativeExpressListener() {
            @Override
            public void onAdLoaded(List<AdvanceNativeExpressAdItem> list) {

                if (null == list || list.isEmpty()) {
                    Log.d("DEMO", "NO AD RESULT");
                } else {
                    advanceNativeExpressAdItem = list.get(0);
                    if (advanceNativeExpressAdItem == null) {
                        Log.d("DEMO", "NO AD RESULT");
                        return;
                    }
                    logAndToast(mActivity, "广告加载成功");

                    //穿山甲需要设置dislike逻辑，要在选中回调里移除广告
                    if (AdvanceConfig.SDK_ID_CSJ.equals(advanceNativeExpressAdItem.getSdkId())) {
                        CsjNativeExpressAdItem csjNativeExpressAdItem = (CsjNativeExpressAdItem) advanceNativeExpressAdItem;
                        csjNativeExpressAdItem.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
                            @Override
                            public void onShow() {

                            }

                            @Override
                            public void onSelected(int i, String s, boolean enforce) {
                                if (adContainer != null)
                                    adContainer.removeAllViews();
                            }

                            @Override
                            public void onCancel() {

                            }

//                    @Override
//                    public void onRefuse() {
//
//                    }
                        });
                    }

                    //从实际执行结果中获取是否是广点通模板2.0类型广告
                    isGdtExpress2 = AdvanceConfig.SDK_ID_GDT.equals(advanceNativeExpressAdItem.getSdkId()) && advanceNativeExpress.isGdtExpress2();

                    //广点通模板2.0不可以在这里可以直接添加视图，否则无法展示，应该在onAdRenderSuccess中添加视图
                    if (!isGdtExpress2) {
                        adContainer.removeAllViews();
                        adContainer.setVisibility(View.VISIBLE);
                        adContainer.addView(advanceNativeExpressAdItem.getExpressAdView());
                    }
                    //render以后才会进行广告渲染， 广告可见才会产生曝光，否则将无法产生收益。
                    advanceNativeExpressAdItem.render();
                }
            }

            @Override
            public void onAdRenderSuccess(View view) {
                logAndToast(mActivity, "广告渲染成功");

                //需要对回调进行主线程切换，防止回调在非主线程导致崩溃
                boolean isMainThread = Looper.myLooper() == Looper.getMainLooper();
                if (isMainThread) {
                    renderGdt2(adContainer);
                } else {
                    //如果是非主线程，需要强制切换到主线程来进行初始化
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.AdvanceLog("force to main thread run render");
                            renderGdt2(adContainer);
                        }
                    });
                }
            }


            @Override
            public void onAdClose(View view) {
                //移除布局
                adContainer.removeAllViews();
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
                logAndToast(mActivity, "onSdkSelected = " + id);
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
        advanceNativeExpress.loadStrategy();
    }

    private void renderGdt2(ViewGroup adContainer) {
        Log.d("NativeExpressActivity", "renderGdt2  adContainer = " + adContainer);

        if (adContainer == null) {
            return;
        }
        //广点通模板2.0 需要在RenderSuccess以后再加载视图
        if (advanceNativeExpressAdItem != null && advanceNativeExpressAdItem.getSdkId().equals(AdvanceConfig.SDK_ID_GDT) && isGdtExpress2) {
            adContainer.removeAllViews();
            adContainer.setVisibility(View.VISIBLE);

            // 广告可见才会产生曝光，否则将无法产生收益。
            adContainer.addView(advanceNativeExpressAdItem.getExpressAdView());
        }
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
    public void logAndToast(Context context, String msg) {
        Log.d("[DemoUtil][logAndToast]", msg);
        //如果不想弹出toast可以在此注释掉下面代码
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
