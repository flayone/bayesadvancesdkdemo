package com.advance.advancesdkdemo.custom.banner;

import android.app.Activity;
import android.view.ViewGroup;

import com.advance.AdvanceBannerListener;
import com.advance.AdvanceBaseAdspot;
import com.advance.AdvanceConfig;
import com.advance.BannerSetting;
import com.advance.utils.LogUtil;

public class MyBannerAd extends AdvanceBaseAdspot {
    private ViewGroup adContainer;
    private AdvanceBannerListener listener;//这里也可以根据自己需求自定义，这里使用了默认的 AdvanceBannerListener。

    public MyBannerAd(Activity activity, ViewGroup adContainer, String mediaId, String adspotId) {
        super(activity, mediaId, adspotId);
        this.adContainer = adContainer;
    }

    public void setAdListener(AdvanceBannerListener listener) {
        this.listener = listener;
    }


    public void selectSdkSupplier() {
        try {
            if (suppliers == null || suppliers.isEmpty()) {
                LogUtil.AdvanceLog("No SDK");
                if (null != listener) {
                    listener.onAdFailed();
                }
            } else {
                currentSdkSupplier = suppliers.get(0);
                LogUtil.AdvanceLog("select sdk:" + currentSdkSupplier.id);
                suppliers.remove(0);
                //策略成功下发上报
                reportAdvanceLoaded();
                //根据策略选中的sdk标志，初始化各个SDK广告位
                if (AdvanceConfig.SDK_TAG_MERCURY.equals(currentSdkSupplier.sdkTag)) {
                    initMercuryBanner();

                } else if (AdvanceConfig.SDK_TAG_GDT.equals(currentSdkSupplier.sdkTag)) {
                    initGdtBanner();

                } else if (AdvanceConfig.SDK_TAG_CSJ.equals(currentSdkSupplier.sdkTag)) {
                    initCsjBanner();
                } else if ("其他渠道tag".equals(currentSdkSupplier.sdkTag)) {
                    //进行其他渠道广告位的初始化创建、比如百度SDK，inmobi等等
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != listener) {
                listener.onAdFailed();
            }
        }
    }

    @Override
    public void selectSdkSupplierFailed() {
        if (listener != null) {
            listener.onAdFailed();
        }

    }

    /**
     * 初始化时可以选择使用自己封装adapter，也可以选用聚合内已实现的adapter
     **/
    private void initMercuryBanner() {
        try {
            //建议使用自定义的adapter，那么需要新建一个MyMercuryBannerAdapter 去做广告的一些适配及加载等操作
            MyMercuryBannerAdapter mercuryBannerAdapter = new MyMercuryBannerAdapter(activity, adContainer, this, currentSdkSupplier);

//       如果不想自定义adapter，可以使用聚合内已实现的adapter，需要设置 Banner 的一些必须属性和回调，在相应回调事件中进行事件上报和部分自定义值设定
//            MercuryBannerAdapter mercuryBannerAdapter = new MercuryBannerAdapter(activity, adContainer, getBannerSetting() , currentSdkSupplier);
            mercuryBannerAdapter.loadAd();
        } catch (Throwable e) {
            e.printStackTrace();
            onFailed();
        }


    }

    /**
     * 初始化时可以选择使用自己封装adapter，也可以选用聚合内已实现的adapter
     **/
    private void initGdtBanner() {
        try {
            //建议使用自定义的adapter
            MyGdtBannerAdapter gdtBannerAdapter = new MyGdtBannerAdapter(activity, adContainer, this, currentSdkSupplier);
//            如果不想自定义adapter，可以使用聚合内已实现的adapter，需要设置 Banner 的一些必须属性和回调，在相应回调事件中进行事件上报和部分自定义值设定
//            GdtBannerAdapter gdtBannerAdapter = new GdtBannerAdapter(activity, adContainer, getBannerSetting() , currentSdkSupplier);
            gdtBannerAdapter.loadAd();
        } catch (Throwable e) {
            e.printStackTrace();
            onFailed();
        }
    }

    /**
     * 初始化时可以选择使用自己封装adapter，也可以选用聚合内已实现的adapter
     **/

    private void initCsjBanner() {
        try {
            //使用自定义的adapter
            MyCsjBannerAdapter csjBannerAdapter = new MyCsjBannerAdapter(activity, adContainer, this, currentSdkSupplier);
//            如果不想自定义adapter，可以使用聚合内已实现的adapter，需要设置 Banner 的一些必须属性和回调，在相应回调事件中进行事件上报和部分自定义值设定
//            CsjBannerAdapter csjBannerAdapter = new CsjBannerAdapter(activity, adContainer, getBannerSetting() , currentSdkSupplier);
            csjBannerAdapter.loadAd();
        } catch (Throwable e) {
            e.printStackTrace();
            onFailed();
        }
    }

    /**
     * ！！！如果自己去自定义adapter，这里完全可以删除
     * 不想自定义adapter的时候需要，需要在这里先设置banner的属性和回调处理
     *
     * @return 聚合SDK封装好的adapter需要的BannerSetting设置项
     */
    private BannerSetting getBannerSetting() {
        return new BannerSetting() {
            @Override
            public void adapterDidDislike() { // csj回调了dislike逻辑
                doDislike();
            }

            //设置广点通、穿山甲 banner 刷新间隔时长
            @Override
            public int getRefreshInterval() {
                return 30;
            }

            //穿山甲部分设置：这个参数设置即可，不影响个性化模板广告的size，单位Px
            @Override
            public int getCsjAcceptedSizeWidth() {
                return 640;
            }

            //穿山甲部分设置：这个参数设置即可，不影响个性化模板广告的size，单位Px
            @Override
            public int getCsjAcceptedSizeHeight() {
                return 100;
            }

            //穿山甲部分设置必选参数 设置穿山甲期望个性化模板广告view的size,单位dp
            @Override
            public int getCsjExpressViewAcceptedWidth() {
                return 640;
            }

            //穿山甲部分设置必选参数 设置穿山甲期望个性化模板广告view的size,单位dp
            @Override
            public int getCsjExpressViewAcceptedHeight() {
                return 100;
            }

            //统一回调： 广告下发成功
            @Override
            public void adapterDidSucceed() {
                onLoaded();
            }

            //统一回调： 广告展示成功
            @Override
            public void adapterDidShow() {
                onShow();
            }

            //统一回调： 广告加载失败
            @Override
            public void adapterDidClicked() {
                onClicked();
            }

            //统一回调： 广告被点击
            @Override
            public void adapterDidFailed() {
                onFailed();
            }
        };
    }


    // ---------- !!! 这里开始到最后 是设置自定义的 My***Adapter 中部分统一的事件回调和事件上报操作


    public void doDislike() {
        if (null != listener) {
            listener.onDislike();
        }
    }


    //广告下发成功，需要调用report方法进行成功上报，否则聚合系统无发统计到广告信息
    public void onLoaded() {
        reportAdLoaded();
    }

    //广告展示成功，需要调用report方法进行成功上报，否则聚合系统无发统计到广告信息
    public void onShow() {
        reportAdShow();
        if (null != listener) {
            listener.onAdShow();
        }
    }

    //广告加载失败，需要调用report方法进行上报，否则聚合系统无发统计到广告信息
    public void onFailed() {
        reportAdFailed();
        //失败后重新去选择SDK策略,先不回调失败
        selectSdkSupplier();
    }

    //广告点击，需要调用report方法进行上报，否则聚合系统无发统计到广告信息
    public void onClicked() {
        reportAdClicked();
        if (null != listener) {
            listener.onAdClicked();
        }
    }
    // ---------- !!! 统一的事件回调和事件上报操作结束

}