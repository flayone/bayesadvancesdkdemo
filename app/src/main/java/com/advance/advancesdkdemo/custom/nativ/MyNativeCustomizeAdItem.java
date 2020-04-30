package com.advance.advancesdkdemo.custom.nativ;

public interface MyNativeCustomizeAdItem {
    void showAd();

    //获取渠道id 用来确定广告属于哪个SDK
    String getSupplierId();
}
