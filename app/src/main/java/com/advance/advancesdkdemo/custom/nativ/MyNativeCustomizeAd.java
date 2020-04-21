package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.widget.FrameLayout;

import com.advance.AdvanceBaseAdspot;
import com.advance.AdvanceConfig;
import com.advance.utils.LogUtil;

import java.util.List;

public class MyNativeCustomizeAd extends AdvanceBaseAdspot {

    private FrameLayout adContainer;

    public MyNativeCustomizeAd(Activity activity, String mediaId, String adspotId) {
        super(activity, mediaId, adspotId);
    }

    private MyNativeCustomizeListener listener;

    public void setListener(MyNativeCustomizeListener listener) {
        this.listener = listener;
    }

    public FrameLayout getAdContainer() {
        return adContainer;
    }

    public void setAdContainer(FrameLayout adContainer) {
        this.adContainer = adContainer;
    }

    @Override
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
                    initMercuryNat();
                } else if (AdvanceConfig.SDK_TAG_GDT.equals(currentSdkSupplier.sdkTag)) {
                    initGdtNat();
                } else if (AdvanceConfig.SDK_TAG_CSJ.equals(currentSdkSupplier.sdkTag)) {
                    initCsjNat();
                } else if ("baidu".equals(currentSdkSupplier.sdkTag)) { //百度渠道
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

    private void initMercuryNat() {
        try {
            MyMercuryNCAdapter adapter = new MyMercuryNCAdapter(activity, this, currentSdkSupplier);
            adapter.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
            //广告初始化失败
            onFailed();
        }
    }

    private void initGdtNat() {
        try {
            MyGdtNCAdapter adapter = new MyGdtNCAdapter(activity, this, currentSdkSupplier);
            adapter.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
            //广告初始化失败
            onFailed();
        }
    }

    private void initCsjNat() {
        try {
            MyCsjNCAdapter adapter = new MyCsjNCAdapter(activity, this, currentSdkSupplier);
            adapter.loadAd();
        } catch (Exception e) {
            e.printStackTrace();
            //广告初始化失败
            onFailed();
        }
    }


    // ---------- !!! 这里开始到最后 是设置自定义的 My***Adapter 中部分统一的事件回调和事件上报操作


    //广告下发成功，需要调用report方法进行成功上报，否则聚合系统无发统计到广告信息
    public void onLoaded(List<MyNativeCustomizeAdItem> list) {
        reportAdLoaded();
        if (null != listener) {
            listener.onAdLoaded(list);
        }
    }

    //广告展示成功，需要调用report方法进行成功上报，否则聚合系统无发统计到广告信息
    public void onShow(MyNativeCustomizeAdItem item) {
        reportAdShow();
        if (null != listener) {
            listener.onAdShow(item);
        }
    }

    //广告加载失败，需要调用report方法进行上报，否则聚合系统无发统计到广告信息
    public void onFailed() {
        reportAdFailed();
        //失败后重新去选择SDK策略,先不回调失败
        selectSdkSupplier();
    }

    //广告点击，需要调用report方法进行上报，否则聚合系统无发统计到广告信息
    public void onClicked(MyNativeCustomizeAdItem item) {
        reportAdClicked();
        if (null != listener) {
            listener.onAdClicked(item);
        }
    }

    //广告关闭
    public void onClosed(MyNativeCustomizeAdItem item) {
        if (null != listener) {
            listener.onAdClose(item);
        }
    }


    // ---------- !!! 统一的事件回调和事件上报操作结束

}
