package com.advance.advancesdkdemo.custom.nativ;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.FrameLayout;

import com.advance.AdvanceConfig;
import com.advance.advancesdkdemo.R;
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

        //创建自己的自渲染广告位，第二个参数为媒体id，第三个参数为广告位id
        nativeCustomizeAd = new MyNativeCustomizeAd(this, "后台获取的媒体id", "后台获取的广告位id");
        //必须：设置广告载体
        nativeCustomizeAd.setAdContainer(fl);
//       非必须： 添加自定义的listener
        nativeCustomizeAd.setListener(this);
//        必须：设置打底广告，app第一次打开时，会先走这里设置的打底广告，下面有各个平台的打底广告可以做测试用。
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("1101152570", "4090398440079274", null, AdvanceConfig.SDK_TAG_GDT));
        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("100171", "10002805", "e1d0d3aaf95d3f1980367e75bc41141d", AdvanceConfig.SDK_TAG_MERCURY));
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("100171", "10002806",  "e1d0d3aaf95d3f1980367e75bc41141d", AdvanceConfig.SDK_TAG_MERCURY));
//        nativeCustomizeAd.setDefaultSdkSupplier(new SdkSupplier("5001121", "901121737", null, AdvanceConfig.SDK_TAG_CSJ));
//        请求广告
        nativeCustomizeAd.loadAd();
    }


    @Override
    public void onAdClose(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdClose" + item.getSDKTag());
        fl.removeAllViews();
    }

    @Override
    public void onAdShow(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdShow" + item.getSDKTag());
    }

    @Override
    public void onAdFailed() {
        Log.d(Tag, "onAdFailed");

    }

    @Override
    public void onAdClicked(MyNativeCustomizeAdItem item) {
        Log.d(Tag, "onAdClicked" + item.getSDKTag());

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
