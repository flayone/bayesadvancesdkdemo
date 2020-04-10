package com.advance.advancesdkdemo.custom.nativ;

import java.util.List;

public interface MyNativeCustomizeListener {
    void onAdClose(MyNativeCustomizeAdItem item);

    void onAdShow(MyNativeCustomizeAdItem item);

    void onAdFailed();

    void onAdClicked(MyNativeCustomizeAdItem item);

    void onAdLoaded(List<MyNativeCustomizeAdItem> list);
}
