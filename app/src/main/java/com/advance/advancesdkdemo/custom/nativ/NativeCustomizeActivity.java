package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.ADManager;
import com.advance.advancesdkdemo.R;
import com.advance.model.AdvanceSupplierID;
import com.advance.model.SdkSupplier;

import java.util.List;

public class NativeCustomizeActivity extends Activity implements MyNativeCustomizeListener {

    MyNativeCustomizeAd nativeCustomizeAd;
    FrameLayout fl;
    String Tag = NativeCustomizeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_container);
        fl = findViewById(R.id.fl_ad);

        //创建自己的自渲染广告位,第二个参数为mercury后台申请的自渲染广告位id
        nativeCustomizeAd = new MyNativeCustomizeAd(this, "");
        //必须：设置广告载体
        nativeCustomizeAd.setAdContainer(fl);
        //推荐：设置是否开启策略缓存模式
        nativeCustomizeAd.enableStrategyCache(true);
//       推荐： 添加自定义的核心事件回调listener
        nativeCustomizeAd.setListener(this);
//        必须：设置打底广告，无策略时，会先走这里设置的打底广告。下面是各个渠道的测试用打底广告。
        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("4090398440079274", AdvanceSupplierID.GDT));
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("10002805", AdvanceSupplierID.MERCURY);
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier( "10002806",  AdvanceSupplierID.MERCURY));
//        注意！！！：如果是使用自定义渠道的广告做打底，，需要使用下面的SdkSupplier初始化方法。
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier( "自定义sdk渠道媒体id","自定义sdk渠道广告位id" , "自定义sdk渠道id"));
//        请求广告
        nativeCustomizeAd.loadAd();
    }


    @Override
    public void onAdClose(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdClose" + item.getSupplierId());
        fl.removeAllViews();
    }

    @Override
    public void onAdShow(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdShow" + item.getSupplierId());
    }

    @Override
    public void onAdFailed() {
        Log.d(Tag, "onAdFailed");
    }

    @Override
    public void onAdClicked(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdClicked" + item.getSupplierId());

    }

    @Override
    public void onAdLoaded(List<MyNativeCustomizeAdItem> list) {
        Log.d(Tag, "onAdLoaded" + list);

        //获取到广告调用封装的showAd方法来展示广告
        if (list != null && list.size() > 0) {
            list.get(0).showAd();
        }
    }
}
